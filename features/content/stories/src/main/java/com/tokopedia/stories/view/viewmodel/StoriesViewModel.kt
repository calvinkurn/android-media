package com.tokopedia.stories.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase.Companion.isVisit
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.stories.R
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.domain.model.StoriesSource
import com.tokopedia.stories.domain.model.StoriesTrackActivityActionType
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.uimodel.AuthorType
import com.tokopedia.stories.utils.StoriesPreference
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.BUFFERING
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.PAUSE
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.RESUME
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesType
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.getRandomNumber
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.stories.view.viewmodel.state.BottomSheetStatusDefault
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType
import com.tokopedia.stories.view.viewmodel.state.ProductBottomSheetUiState
import com.tokopedia.stories.view.viewmodel.state.StoriesUiState
import com.tokopedia.stories.view.viewmodel.state.StoryReportStatusInfo
import com.tokopedia.stories.view.viewmodel.state.TimerStatusInfo
import com.tokopedia.stories.view.viewmodel.state.isAnyShown
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoriesViewModel @AssistedInject constructor(
    @Assisted private val args: StoriesArgsModel,
    private val repository: StoriesRepository,
    val userSession: UserSessionInterface,
    private val sharedPref: StoriesPreference
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(args: StoriesArgsModel): StoriesViewModel
    }

    private val _storiesMainDataState = MutableStateFlow(StoriesUiModel())
    private val _bottomSheetStatusState = MutableStateFlow(BottomSheetStatusDefault)
    private val _productsState = MutableStateFlow(ProductBottomSheetUiState.Empty)
    private val _reportState = MutableStateFlow(StoryReportStatusInfo.Empty)

    private val _storiesEvent = MutableSharedFlow<StoriesUiEvent>(extraBufferCapacity = 100)
    val storiesEvent: Flow<StoriesUiEvent>
        get() = _storiesEvent

    private val _groupPos = MutableStateFlow(-1)
    private val _detailPos = MutableStateFlow(-1)
    private val _resetValue = MutableStateFlow(-1)

    val validAuthorId: String
        get() = mGroup.author.id.ifBlank { args.authorId }

    val mGroup: StoriesGroupItem
        get() {
            val groupPosition = _groupPos.value
            return try {
                _storiesMainDataState.value.groupItems[groupPosition]
            } catch (_: Exception) {
                StoriesGroupItem()
            }
        }

    val mDetail: StoriesDetailItem
        get() {
            val groupPosition = _groupPos.value
            val detailPosition = _detailPos.value
            return try {
                _storiesMainDataState.value
                    .groupItems[groupPosition]
                    .detail.detailItems[detailPosition]
            } catch (_: Exception) {
                StoriesDetailItem()
            }
        }

    private val _impressedGroupHeader = mutableListOf<StoriesGroupHeader>()
    val impressedGroupHeader: List<StoriesGroupHeader>
        get() = _impressedGroupHeader

    private val mStoriesMainData: StoriesUiModel
        get() = _storiesMainDataState.value

    private val mGroupPos: Int
        get() = _groupPos.value

    private val mDetailPos: Int
        get() = _detailPos.value

    private val mGroupSize: Int
        get() = _storiesMainDataState.value.groupItems.size

    private val mDetailSize: Int
        get() {
            val groupPosition = _groupPos.value
            return try {
                _storiesMainDataState.value.groupItems[groupPosition]
                    .detail.detailItems.size
            } catch (_: Exception) {
                0
            }
        }

    val storyId: String
        get() = mDetail.id

    val isProductAvailable: Boolean
        get() {
            val currentItem = mGroup.detail.detailItems
            return currentItem.getOrNull(mGroup.detail.selectedDetailPosition)?.isProductAvailable == true
        }

    val isAnyBottomSheetShown: Boolean
        get() = _bottomSheetStatusState.value.isAnyShown

    private val mResetValue: Int
        get() = _resetValue.value

    private var mLatestTrackPosition = -1

    private var mIsPageSelected = false

    val storiesState: StateFlow<StoriesUiState> = combine(
        _storiesMainDataState,
        _productsState,
        _timerState,
        _reportState
    ) { storiesMainData, product, timerState, reportState ->
        StoriesUiState(
            storiesMainData = storiesMainData,
            productSheet = product,
            timerStatus = timerState,
            reportState = reportState,
            canShowGroup = args.source != StoriesSource.BROWSE_WIDGET.value
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StoriesUiState.Empty
    )

    private val _timerState: Flow<TimerStatusInfo>
        get() = combine(
            _bottomSheetStatusState,
            _storiesMainDataState,
            _groupPos,
            _detailPos
        ) { bottomSheet, mainData, groupPosition, detailPosition ->
            val listOfDetail =
                mainData.groupItems.getOrNull(groupPosition)?.detail?.detailItems ?: emptyList()
            val detail = listOfDetail.getOrNull(detailPosition) ?: StoriesDetailItem()
            TimerStatusInfo(
                event = if (bottomSheet.isAnyShown) PAUSE else detail.event,
                story = TimerStatusInfo.Companion.StoryTimer(
                    id = detail.id,
                    itemCount = listOfDetail.size,
                    resetValue = detail.resetValue,
                    duration = detail.content.duration,
                    position = detailPosition
                )
            )
        }

    val userId: String
        get() = userSession.userId.ifEmpty { "0" }

    private val _userReportReasonList =
        MutableLiveData<Result<List<PlayUserReportReasoningUiModel>>>()
    val userReportReasonList: Result<List<PlayUserReportReasoningUiModel>>
        get() = _userReportReasonList.value ?: Success(emptyList())

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SetMainData -> handleMainData(action.selectedGroup)
            is StoriesUiAction.SelectGroup -> handleSelectGroup(
                action.selectedGroup,
                action.showAnimation
            )

            is StoriesUiAction.CollectImpressedGroup -> handleCollectImpressedGroup(action.data)
            is StoriesUiAction.Navigate -> handleNav(action.appLink)
            is StoriesUiAction.DismissSheet -> handleDismissSheet(action.type)
            is StoriesUiAction.ProductAction -> handleProductAction(action.action, action.product)
            is StoriesUiAction.ShowVariantSheet -> handleVariantSheet(action.product)
            is StoriesUiAction.UpdateStoryDuration -> handleUpdateDuration(action.duration)
            is StoriesUiAction.OpenBottomSheet -> handleOpenBottomSheet(action.type)
            is StoriesUiAction.SelectReportReason -> handleSelectReportReason(action.reason)
            is StoriesUiAction.ResumeStories -> handleOnResumeStories(action.forceResume)
            StoriesUiAction.SetInitialData -> handleSetInitialData()
            StoriesUiAction.NextDetail -> handleNext()
            StoriesUiAction.PreviousDetail -> handlePrevious()
            StoriesUiAction.PauseStories -> handleOnPauseStories()
            StoriesUiAction.OpenKebabMenu -> handleOpenKebab()
            StoriesUiAction.TapSharing -> handleSharing()
            StoriesUiAction.ShowDeleteDialog -> handleShowDialogDelete()
            StoriesUiAction.OpenProduct -> handleOpenProduct()
            StoriesUiAction.FetchProduct -> handleGetProducts()
            StoriesUiAction.DeleteStory -> handleDeleteStory()
            StoriesUiAction.ContentIsLoaded -> handleContentIsLoaded()
            StoriesUiAction.PageIsSelected -> handlePageIsSelected()
            StoriesUiAction.VideoBuffering -> handleVideoBuffering()
            StoriesUiAction.OpenReport -> handleOpenReport()
            StoriesUiAction.ResetReportState -> handleReportState(StoryReportStatusInfo.ReportState.None) {}
            StoriesUiAction.HasSeenDurationCoachMark -> handleHasSeenDurationCoachMark()
        }
    }

    private fun getReportReasonList() {
        viewModelScope.launchCatchError(block = {
            if (userReportReasonList is Success && (userReportReasonList as Success).data.isNotEmpty()) return@launchCatchError

            val reasonList = repository.getReportReasonList()
            _userReportReasonList.value = Success(reasonList)
        }) {
            _userReportReasonList.value = Fail(it)
        }
    }

    fun submitReport(description: String, timestamp: Long) {
        viewModelScope.launchCatchError(block = {
            val submitReportResult = repository.submitReport(
                storyDetail = mDetail,
                reasonId = _reportState.value.report.selectedReason?.reasoningId.orZero(),
                timestamp = timestamp,
                reportDesc = description
            )

            _reportState.update { statusInfo ->
                statusInfo.copy(
                    state = StoryReportStatusInfo.ReportState.Submitted,
                    report = statusInfo.report.copy(
                        submitStatus = if (submitReportResult) {
                            Success(Unit)
                        } else {
                            Fail(
                                MessageErrorException()
                            )
                        }
                    )
                )
            }
        }) {
            _reportState.update { statusInfo ->
                statusInfo.copy(
                    state = StoryReportStatusInfo.ReportState.Submitted,
                    report = statusInfo.report.copy(
                        submitStatus = Fail(it)
                    )
                )
            }
        }
    }

    private fun handleSetInitialData() {
        viewModelScope.launchCatchError(block = {
            _storiesMainDataState.value = requestStoriesInitialData()
            _groupPos.value = mStoriesMainData.selectedGroupPosition

            if (mGroup == StoriesGroupItem()) _storiesEvent.emit(StoriesUiEvent.EmptyGroupPage)
        }) { exception ->
            _storiesEvent.emit(
                StoriesUiEvent.ErrorGroupPage(exception) {
                    handleSetInitialData()
                }
            )
        }

        viewModelScope.launch { repository.setHasAckStoriesFeature() }
    }

    private fun handleMainData(selectedGroup: Int) {
        mIsPageSelected = false
        mLatestTrackPosition = -1
        _groupPos.update { selectedGroup }
        viewModelScope.launchCatchError(block = {
            setInitialData()
            setCachingData()
        }) { exception ->
            _storiesEvent.emit(
                StoriesUiEvent.ErrorDetailPage(exception) {
                    handleMainData(_groupPos.value)
                }
            )
        }
    }

    private fun handleSelectGroup(position: Int, showAnimation: Boolean) {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.SelectGroup(position, showAnimation))
        }
    }

    private fun handleCollectImpressedGroup(data: StoriesGroupHeader) {
        val isExist = impressedGroupHeader.find { it.groupId == data.groupId } != null
        if (isExist) return
        _impressedGroupHeader.add(data)
    }

    private fun handleNext() {
        val newGroupPosition = mGroupPos.plus(1)
        val newDetailPosition = mDetailPos.plus(1)

        when {
            newDetailPosition < mDetailSize -> updateDetailData(position = newDetailPosition)
            newGroupPosition < mGroupSize -> handleSelectGroup(position = newGroupPosition, true)
            else -> viewModelScope.launch { _storiesEvent.emit(StoriesUiEvent.FinishedAllStories) }
        }
    }

    private fun handlePrevious() {
        val newGroupPosition = mGroupPos.minus(1)
        val newDetailPosition = mDetailPos.minus(1)

        when {
            newDetailPosition > -1 -> updateDetailData(position = newDetailPosition)
            newGroupPosition > -1 -> handleSelectGroup(position = newGroupPosition, true)
            else -> updateDetailData(isReset = true)
        }
    }

    private fun handleOnPauseStories() {
        updateDetailData(event = PAUSE, isSameContent = true)
    }

    private fun handleOnResumeStories(forceResume: Boolean) {
        val event = if (forceResume) {
            RESUME
        } else if (mDetail.isContentLoaded && mIsPageSelected) {
            RESUME
        } else {
            PAUSE
        }

        updateDetailData(
            event = event,
            isSameContent = true
        )
    }

    private fun handlePageIsSelected() {
        mIsPageSelected = true
        updateDetailData(event = if (mDetail.isContentLoaded) RESUME else PAUSE)
    }

    private fun handleContentIsLoaded() {
        updateDetailData(event = if (mIsPageSelected) RESUME else PAUSE, isSameContent = true)
        checkAndHitTrackActivity()
        setupOnboard()

        run {
            if (mDetailPos == mDetailSize - 1) {
                val type = mGroup.type
                if (type != StoriesType.Author) return@run
                setHasSeenAllStories(mGroup.author.id, mGroup.author.type)
            }
        }

        if (mGroupPos != mGroupSize - 1 || mDetailPos != mDetailSize - 1) return
        setHasSeenAllStories(args.authorId, AuthorType.getByType(args.authorType))
    }

    private fun setHasSeenAllStories(authorId: String, authorType: AuthorType) {
        viewModelScope.launch {
            repository.setHasSeenAllStories(authorId, authorType)
        }
    }

    private fun handleOpenReport() {
        handleReportState(StoryReportStatusInfo.ReportState.OnSelectReason) {
            handleOpenBottomSheet(BottomSheetType.Report)
        }
    }

    private fun handleReportState(
        newState: StoryReportStatusInfo.ReportState,
        nextAction: () -> Unit
    ) {
        _reportState.update { statusInfo ->
            statusInfo.copy(
                state = newState
            )
        }
        nextAction()
    }

    private fun handleSelectReportReason(item: PlayUserReportReasoningUiModel.Reasoning) {
        _reportState.update { statusInfo ->
            statusInfo.copy(
                state = StoryReportStatusInfo.ReportState.OnSubmit,
                report = statusInfo.report.copy(
                    selectedReason = item
                )
            )
        }

        handleOpenBottomSheet(BottomSheetType.SubmitReport)
    }

    private suspend fun setInitialData() {
        val isCached = mGroup.detail.detailItems.isNotEmpty()
        val currentDetail = if (isCached) {
            mGroup.detail
        } else {
            val detailData = requestStoriesDetailData(mGroup)
            updateMainData(detail = detailData, groupPosition = mGroupPos)
            detailData
        }

        val cachedPos = currentDetail.selectedDetailPositionCached
        val maxPos = mDetailSize.minus(1)
        val position = if (cachedPos > maxPos) maxPos else cachedPos
        updateDetailData(position = position, isReset = true)

        if (currentDetail == StoriesDetail()) {
            _storiesEvent.emit(StoriesUiEvent.EmptyDetailPage)
            return
        }

        val currentDetailItems = currentDetail.detailItems.getOrNull(position) ?: return
        val authorIdToCheck = when (currentDetailItems.author.type) {
            AuthorType.Seller -> userSession.shopId
            else -> userSession.userId
        }

        if (
            currentDetailItems.category == StoriesDetailItem.StoryCategory.Manual &&
            !repository.hasSeenManualStoriesDurationCoachmark() &&
            currentDetailItems.author.id == authorIdToCheck
        ) {
            _storiesEvent.emit(StoriesUiEvent.ShowStoriesTimeCoachmark)
        }
    }

    private fun setCachingData() {
        viewModelScope.launchCatchError(block = {
            val prevRequest = async { fetchAndCachePreviousGroupDetail() }
            val nextRequest = async { fetchAndCacheNextGroupDetail() }
            prevRequest.await()
            nextRequest.await()
        }) { exception ->
            _storiesEvent.emit(StoriesUiEvent.ErrorFetchCaching(exception))
        }
    }

    private suspend fun fetchAndCachePreviousGroupDetail() {
        val prevGroupPos = mGroupPos.minus(1)
        val prevGroupItem = mStoriesMainData.groupItems.getOrNull(prevGroupPos) ?: return
        val isPrevGroupCached = prevGroupItem.detail.detailItems.isNotEmpty()
        if (isPrevGroupCached) return

        val prevGroupData = requestStoriesDetailData(prevGroupItem)
        updateMainData(detail = prevGroupData, groupPosition = prevGroupPos)
    }

    private suspend fun fetchAndCacheNextGroupDetail() {
        val nextGroupPos = mGroupPos.plus(1)
        val nextGroupItem = mStoriesMainData.groupItems.getOrNull(nextGroupPos) ?: return
        val isNextGroupCached = nextGroupItem.detail.detailItems.isNotEmpty()
        if (isNextGroupCached) return

        val nextGroupData = requestStoriesDetailData(nextGroupItem)
        updateMainData(detail = nextGroupData, groupPosition = nextGroupPos)
    }

    private fun updateMainData(
        groupHeader: List<StoriesGroupHeader> = emptyList(),
        groupItems: List<StoriesGroupItem> = emptyList(),
        detail: StoriesDetail,
        groupPosition: Int
    ) {
        _storiesMainDataState.update { group ->
            group.copy(
                selectedGroupId = mGroup.groupId,
                selectedGroupPosition = mGroupPos,
                groupHeader = groupHeader.ifEmpty {
                    group.groupHeader.mapIndexed { index, storiesGroupHeader ->
                        storiesGroupHeader.copy(isSelected = index == mGroupPos)
                    }
                },
                groupItems = groupItems.ifEmpty {
                    group.groupItems.mapIndexed { index, storiesGroupItemUiModel ->
                        if (index == groupPosition) {
                            storiesGroupItemUiModel.copy(
                                detail = detail.copy(
                                    selectedGroupId = storiesGroupItemUiModel.groupId
                                )
                            )
                        } else {
                            storiesGroupItemUiModel
                        }
                    }
                }
            )
        }
    }

    private fun handleShowDialogDelete() {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.ShowDeleteDialog)
        }
    }

    private fun handleDismissSheet(bottomSheetType: BottomSheetType) {
        _bottomSheetStatusState.update { bottomSheet ->
            bottomSheet.mapValues {
                if (it.key == bottomSheetType) {
                    false
                } else {
                    it.value
                }
            }
        }
    }

    private fun handleOpenKebab() {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.OpenKebab)
            _bottomSheetStatusState.update { bottomSheet ->
                bottomSheet.mapValues { it.key == BottomSheetType.Kebab }
            }

            getReportReasonList()
        }
    }

    private fun handleOpenBottomSheet(type: BottomSheetType) {
        viewModelScope.launch {
            _bottomSheetStatusState.update { bottomSheet ->
                bottomSheet.mapValues { it.key == type }
            }
        }
    }

    private fun handleOpenProduct() {
        if (_bottomSheetStatusState.value.isAnyShown || !isProductAvailable) return
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.OpenProduct)
            _bottomSheetStatusState.update { bottomSheet ->
                bottomSheet.mapValues { it.key == BottomSheetType.Product }
            }
        }
    }

    private fun handleSharing() {
        viewModelScope.launch {
            val data = mDetail.share
            _storiesEvent.emit(StoriesUiEvent.TapSharing(data))
            _bottomSheetStatusState.update { bottomSheet ->
                bottomSheet.mapValues { it.key == BottomSheetType.Sharing }
            }
        }
    }

    private fun handleVariantSheet(product: ContentTaggedProductUiModel) {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.ShowVariantSheet(product))
        }
    }

    private fun handleGetProducts() {
        viewModelScope.launchCatchError(block = {
            _productsState.update { product -> product.copy(resultState = ResultState.Loading) }
            val productList =
                repository.getStoriesProducts(
                    validAuthorId,
                    storyId,
                    mGroup.groupName
                )
            _productsState.value = productList
            trackVisitContent(
                ids = productList.products.map(ContentTaggedProductUiModel::id),
                event = BroadcasterReportTrackViewerUseCase.Companion.Event.ProductChanges
            )
        }) { exception ->
            _productsState.update { product -> product.copy(resultState = ResultState.Fail(exception)) }
        }
    }

    private fun handleProductAction(
        action: StoriesProductAction,
        product: ContentTaggedProductUiModel
    ) {
        requiredLogin {
            addToCart(product, action)
        }
    }

    private fun addToCart(product: ContentTaggedProductUiModel, action: StoriesProductAction) {
        requiredLogin {
            viewModelScope.launchCatchError(block = {
                val response = repository.addToCart(
                    productId = product.id,
                    price = product.finalPrice,
                    shopId = validAuthorId,
                    productName = product.title
                )
                if (!response) throw MessageErrorException()

                _storiesEvent.emit(
                    if (action == StoriesProductAction.Atc) {
                        StoriesUiEvent.ProductSuccessEvent(
                            action,
                            R.string.stories_product_atc_success
                        )
                    } else {
                        StoriesUiEvent.NavigateEvent(appLink = ApplinkConst.CART)
                    }
                )
            }) { _storiesEvent.emit(StoriesUiEvent.ShowErrorEvent(it)) }
        }
    }

    private fun requiredLogin(fn: (isLoggedIn: Boolean) -> Unit) {
        if (userSession.isLoggedIn) {
            fn(true)
        } else {
            viewModelScope.launch {
                _storiesEvent.emit(
                    StoriesUiEvent.Login { fn(false) }
                )
            }
        }
    }

    private fun handleDeleteStory() {
        viewModelScope.launch { repository.deleteStory(storyId) }

        val removedItem = mGroup.detail.detailItems.filterNot { it.id == storyId }
        val mainData = _storiesMainDataState.value

        val newDetail = if (removedItem.isEmpty()) {
            StoriesDetail()
        } else {
            mGroup.detail.copy(detailItems = removedItem)
        }

        val newGroupHeader = if (removedItem.isEmpty()) {
            mainData.groupHeader.filterNot { it.groupId == mGroup.detail.selectedGroupId }
        } else {
            emptyList()
        }
        val newGroupItems = if (removedItem.isEmpty()) {
            mainData.groupItems.filterNot { it.groupId == mGroup.detail.selectedGroupId }
        } else {
            emptyList()
        }

        updateMainData(
            groupHeader = newGroupHeader,
            groupItems = newGroupItems,
            detail = newDetail,
            groupPosition = mGroupPos
        )

        moveToOtherStories()
    }

    private fun handleNav(appLink: String) {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.NavigateEvent(appLink))
        }
    }

    private fun handleUpdateDuration(duration: Int) {
        updateMainData(
            detail = mGroup.detail.copy(
                detailItems = mGroup.detail.detailItems.mapIndexed { index, storiesDetailItem ->
                    if (index == mDetailPos) {
                        storiesDetailItem.copy(
                            content = storiesDetailItem.content.copy(
                                duration = duration
                            )
                        )
                    } else {
                        storiesDetailItem
                    }
                }
            ),
            groupPosition = mGroupPos
        )
    }

    private fun handleVideoBuffering() {
        updateMainData(
            detail = mGroup.detail.copy(
                detailItems = mGroup.detail.detailItems.mapIndexed { index, storiesDetailItem ->
                    if (index == mDetailPos) {
                        storiesDetailItem.copy(
                            event = BUFFERING
                        )
                    } else {
                        storiesDetailItem
                    }
                }
            ),
            groupPosition = mGroupPos
        )
    }

    private fun moveToOtherStories() {
        val newGroupPosition = mGroupPos.plus(1)
        when {
            mDetailPos < mDetailSize -> updateDetailData(position = mDetailPos)
            newGroupPosition < mGroupSize -> handleSelectGroup(position = newGroupPosition, true)
            else -> viewModelScope.launch { _storiesEvent.emit(StoriesUiEvent.FinishedAllStories) }
        }
    }

    private fun updateDetailData(
        position: Int = mDetailPos,
        event: StoriesDetailItemUiEvent = PAUSE,
        isReset: Boolean = false,
        isSameContent: Boolean = false
    ) {
        if (position == -1) return
        _detailPos.update { position }
        val positionCached = mGroup.detail.selectedDetailPositionCached
        val currentDetail = mGroup.detail.copy(
            selectedGroupId = mGroup.groupId,
            selectedDetailPosition = position,
            selectedDetailPositionCached = if (positionCached <= position) position else positionCached,
            detailItems = mGroup.detail.detailItems.map { item ->
                item.copy(
                    event = event,
                    resetValue = if (isReset) {
                        _resetValue.update { it.getRandomNumber() }
                        mResetValue
                    } else {
                        mResetValue
                    },
                    isContentLoaded = isSameContent,
                    status = item.status
                )
            }
        )

        updateMainData(detail = currentDetail, groupPosition = mGroupPos)
    }

    private fun checkAndHitTrackActivity() {
        viewModelScope.launchCatchError(block = {
            val detailItem = mGroup.detail
            if (mDetailPos <= mLatestTrackPosition) return@launchCatchError
            mLatestTrackPosition = mDetailPos
            val trackerId = detailItem.detailItems[mLatestTrackPosition].meta.activityTracker
            requestSetStoriesTrackActivity(trackerId)
            trackVisitContent(event = BroadcasterReportTrackViewerUseCase.Companion.Event.Visit)
        }) { exception ->
            _storiesEvent.emit(StoriesUiEvent.ErrorSetTracking(exception))
        }
    }

    private val trackedProductIds = mutableListOf<String>()
    private fun trackVisitContent(ids: List<String> = emptyList(), event: BroadcasterReportTrackViewerUseCase.Companion.Event) {
        val hasChanged = ids.filterNot { trackedProductIds.contains(it) }.isNotEmpty()
        if (hasChanged || event.isVisit) {
            trackedProductIds.clear()
            ids.map { trackedProductIds.add(it) }
        } else { return }
        viewModelScope.launchCatchError(block = { repository.trackContent(storyId, ids, event)}) {}
    }

    private suspend fun requestStoriesInitialData(): StoriesUiModel {
        return repository.getStoriesInitialData(
            authorId = args.authorId,
            authorType = args.authorType,
            source = args.source,
            sourceId = args.sourceId,
            entryPoint = args.entryPoint,
            categoryIds = args.categoryIds,
            productIds = args.productIds
        )
    }

    private suspend fun requestStoriesDetailData(group: StoriesGroupItem): StoriesDetail {
        return if (args.source == StoriesSource.BROWSE_WIDGET.value) {
            repository.getStoriesDetailData(
                authorId = group.author.id,
                authorType = group.author.type.type,
                source = args.source,
                sourceId = args.sourceId,
                entryPoint = args.entryPoint,
                categoryIds = emptyList(),
                productIds = emptyList()
            )
        } else {
            repository.getStoriesDetailData(
                authorId = args.authorId,
                authorType = args.authorType,
                source = StoriesSource.STORY_GROUP.value,
                sourceId = group.groupId,
                entryPoint = args.entryPoint,
                categoryIds = args.categoryIds,
                productIds = args.productIds
            )
        }
    }

    private suspend fun requestSetStoriesTrackActivity(trackerId: String): Boolean {
        val request = StoriesTrackActivityRequestModel(
            id = trackerId,
            action = StoriesTrackActivityActionType.LAST_SEEN.value
        )
        return repository.setStoriesTrackActivity(request)
    }

    private fun handleHasSeenDurationCoachMark() {
        viewModelScope.launch {
            repository.setHasSeenManualStoriesDurationCoachmark()
        }
    }

    private fun setupOnboard() {
        if (!sharedPref.hasVisit()) {
            viewModelScope.launch {
                handleOnPauseStories()
                _storiesEvent.emit(StoriesUiEvent.OnboardShown)
            }
            sharedPref.setHasVisit(true)
        }
    }
}
