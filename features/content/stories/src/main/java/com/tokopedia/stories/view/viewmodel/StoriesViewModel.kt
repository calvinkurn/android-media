package com.tokopedia.stories.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.stories.R
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.model.StoriesSource
import com.tokopedia.stories.domain.model.StoriesTrackActivityActionType
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.PAUSE
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.RESUME
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.getRandomNumber
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.stories.view.viewmodel.state.BottomSheetStatusDefault
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType
import com.tokopedia.stories.view.viewmodel.state.ProductBottomSheetUiState
import com.tokopedia.stories.view.viewmodel.state.StoriesUiState
import com.tokopedia.stories.view.viewmodel.state.isAnyShown
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoriesViewModel @AssistedInject constructor(
    @Assisted private val args: StoriesArgsModel,
    @Assisted private val handle: SavedStateHandle,
    private val repository: StoriesRepository,
    private val userSession: UserSessionInterface,
) : ViewModel() {

    @AssistedFactory
    interface Factory  {
        fun create(args: StoriesArgsModel, handle: SavedStateHandle) : StoriesViewModel
    }

    private val bottomSheetStatus = MutableStateFlow(BottomSheetStatusDefault)
    private val products = MutableStateFlow(ProductBottomSheetUiState.Empty)

    private val _storiesMainData = MutableStateFlow(StoriesUiModel())

    private val _storiesEvent = MutableSharedFlow<StoriesUiEvent>(extraBufferCapacity = 100)
    val storiesEvent: Flow<StoriesUiEvent>
        get() = _storiesEvent

    private val _groupPos = MutableStateFlow(-1)
    private val _detailPos = MutableStateFlow(-1)
    private val _resetValue = MutableStateFlow(-1)

    val mGroup: StoriesGroupItem
        get() {
            val groupPosition = _groupPos.value
            return if (groupPosition < 0) StoriesGroupItem()
            else {
                if (groupPosition >= _storiesMainData.value.groupItems.size) StoriesGroupItem()
                else _storiesMainData.value.groupItems[groupPosition]
            }
        }

    val mDetail: StoriesDetailItem
        get() {
            val groupPosition = _groupPos.value
            val detailPosition = _detailPos.value
            return if (groupPosition < 0 || detailPosition < 0) StoriesDetailItem()
            else {
                when {
                    groupPosition >= _storiesMainData.value.groupItems.size -> StoriesDetailItem()
                    detailPosition >= _storiesMainData.value.groupItems[groupPosition].detail.detailItems.size -> StoriesDetailItem()
                    else -> _storiesMainData.value.groupItems[groupPosition].detail.detailItems[detailPosition]
                }
            }
        }

    private val _impressedGroupHeader = mutableListOf<StoriesGroupHeader>()
    val impressedGroupHeader: List<StoriesGroupHeader>
        get() = _impressedGroupHeader

    private val mStoriesMainData: StoriesUiModel
        get() = _storiesMainData.value

    private val mGroupPos: Int
        get() = _groupPos.value

    private val mDetailPos: Int
        get() = _detailPos.value

    private val mGroupSize: Int
        get() = _storiesMainData.value.groupItems.size

    private val mGroupItem: StoriesGroupItem
        get() {
            val groupPosition = _groupPos.value
            return if (groupPosition < 0) StoriesGroupItem()
            else {
                if (_storiesMainData.value.groupItems.size <= groupPosition) StoriesGroupItem()
                else _storiesMainData.value.groupItems[groupPosition]
            }
        }

    private val mDetailSize: Int
        get() {
            val groupPosition = _groupPos.value
            return if (groupPosition < 0) 0
            else {
                if (groupPosition >= _storiesMainData.value.groupItems.size) 0
                else _storiesMainData.value.groupItems[groupPosition].detail.detailItems.size
            }
        }

    val storyId : String
        get() {
            val currentItem = mGroupItem.detail.detailItems
            return currentItem.getOrNull(mGroupItem.detail.selectedDetailPosition)?.id.orEmpty()
        }

    val isProductAvailable : Boolean
        get() {
            val currentItem = mGroupItem.detail.detailItems
            val productCount = currentItem.getOrNull(mGroupItem.detail.selectedDetailPosition)?.productCount
            return productCount?.isNotEmpty() == true || productCount != "0"
        }

    val isAnyBottomSheetShown : Boolean
        get() = bottomSheetStatus.value.isAnyShown

    private val mResetValue: Int
        get() = _resetValue.value

    private var mLatestTrackPosition = -1

    val storiesState: Flow<StoriesUiState>
        get() = combine(
            _storiesMainData,
            bottomSheetStatus,
            products,
        ) { storiesMainData, sheetStatus, product ->
            StoriesUiState(
                storiesMainData = storiesMainData,
                bottomSheetStatus = sheetStatus,
                productSheet = product,
            )
        }

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SetMainData -> handleMainData(action.selectedGroup)
            is StoriesUiAction.SelectGroup -> handleSelectGroup(action.selectedGroup, action.showAnimation)
            is StoriesUiAction.CollectImpressedGroup -> handleCollectImpressedGroup(action.data)
            StoriesUiAction.SetInitialData -> handleSetInitialData()
            StoriesUiAction.NextDetail -> handleNext()
            StoriesUiAction.PreviousDetail -> handlePrevious()
            StoriesUiAction.PauseStories -> handleOnPauseStories()
            StoriesUiAction.ResumeStories -> handleOnResumeStories()
            StoriesUiAction.OpenKebabMenu -> handleOpenKebab()
            is StoriesUiAction.DismissSheet -> handleDismissSheet(action.type)
            StoriesUiAction.ShowDeleteDialog -> handleShowDialogDelete()
            StoriesUiAction.OpenProduct -> handleOpenProduct()
            is StoriesUiAction.ProductAction -> handleProductAction(action.action, action.product)
            StoriesUiAction.FetchProduct -> getProducts()
            is StoriesUiAction.ShowVariantSheet -> handleVariantSheet(action.product)
            StoriesUiAction.DeleteStory -> handleDeleteStory()
            StoriesUiAction.ContentIsLoaded -> handleContentIsLoaded()
            StoriesUiAction.SaveInstanceStateData -> handleSaveInstanceStateData()
            is StoriesUiAction.Navigate -> handleNav(action.appLink)
            else -> {}
        }
    }

    private fun handleSetInitialData() {
        val storiesMainData = handle.get<StoriesUiModel>(SAVED_INSTANCE_STORIES_MAIN_DATA)
        val groupPosition =  handle.get<Int>(SAVED_INSTANCE_STORIES_GROUP_POSITION) ?: 0
        val detailPosition = handle.get<Int>(SAVED_INSTANCE_STORIES_DETAIL_POSITION) ?: 0

        if (storiesMainData == null) launchRequestInitialData()
        else {
            _storiesMainData.value = storiesMainData
            _groupPos.value = groupPosition
            _detailPos.value = detailPosition
        }
    }

    private fun handleSaveInstanceStateData() {
        handle[SAVED_INSTANCE_STORIES_MAIN_DATA] = mStoriesMainData
        handle[SAVED_INSTANCE_STORIES_GROUP_POSITION] = mGroupPos
        handle[SAVED_INSTANCE_STORIES_DETAIL_POSITION] = mDetailPos
    }

    private fun handleMainData(selectedGroup: Int) {
        mLatestTrackPosition = -1
        _groupPos.update { selectedGroup }
        viewModelScope.launchCatchError(block = {
            setInitialData()
            setCachingData()
        }) { exception ->
            _storiesEvent.emit(StoriesUiEvent.ErrorDetailPage(exception))
        }
    }

    private fun handleSelectGroup(position: Int, showAnimation: Boolean) {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.SelectGroup(position, showAnimation))
        }
    }

    private fun handleCollectImpressedGroup(data: StoriesGroupHeader) {
        val isExist = impressedGroupHeader.find { it.groupId == data.groupId }!= null
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

    private fun handleOnResumeStories() {
        updateDetailData(event = RESUME, isSameContent = true)
    }

    private fun handleContentIsLoaded() {
        updateDetailData(event = RESUME, isSameContent = true)
        checkAndHitTrackActivity()
    }

    private suspend fun setInitialData() {
        val isCached = mGroupItem.detail.detailItems.isNotEmpty()
        val currentDetail = if (isCached) mGroupItem.detail
        else {
            val detailData = requestStoriesDetailData(mGroup.groupId)
            updateMainData(detail = detailData, groupPosition = mGroupPos)
            detailData
        }

        updateDetailData(position = currentDetail.selectedDetailPositionCached, isReset = true)
        if (currentDetail == StoriesDetail()) _storiesEvent.emit(StoriesUiEvent.EmptyDetailPage)
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

        val prevGroupId = prevGroupItem.groupId

        val prevGroupData = requestStoriesDetailData(prevGroupId)
        updateMainData(detail = prevGroupData, groupPosition = prevGroupPos)
    }

    private suspend fun fetchAndCacheNextGroupDetail() {
        val nextGroupPos = mGroupPos.plus(1)
        val nextGroupItem = mStoriesMainData.groupItems.getOrNull(nextGroupPos) ?: return
        val isNextGroupCached = nextGroupItem.detail.detailItems.isNotEmpty()
        if (isNextGroupCached) return

        val nextGroupId = nextGroupItem.groupId

        val nextGroupData = requestStoriesDetailData(nextGroupId)
        updateMainData(detail = nextGroupData, groupPosition = nextGroupPos)
    }

    private fun updateMainData(detail: StoriesDetail, groupPosition: Int) {
        _storiesMainData.update { group ->
            group.copy(
                selectedGroupId = mGroup.groupId,
                selectedGroupPosition = mGroupPos,
                groupHeader = group.groupHeader.mapIndexed { index, storiesGroupHeader ->
                    storiesGroupHeader.copy(isSelected = index == mGroupPos)
                },
                groupItems = group.groupItems.mapIndexed { index, storiesGroupItemUiModel ->
                    if (index == groupPosition) {
                        storiesGroupItemUiModel.copy(
                            detail = detail.copy(
                                selectedGroupId = storiesGroupItemUiModel.groupId,
                            )
                        )
                    } else storiesGroupItemUiModel
                }
            )
        }
    }

    private fun handleOpenKebab() {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.OpenKebab)
            bottomSheetStatus.update { bottomSheet ->
                bottomSheet.mapValues {
                    if (it.key == BottomSheetType.Kebab)
                        true
                    else it.value
                }
            }
        }
    }

    private fun handleDismissSheet(bottomSheetType: BottomSheetType) {
        bottomSheetStatus.update { bottomSheet ->
            bottomSheet.mapValues {
                if (it.key == bottomSheetType)
                    false
                else it.value
            }
        }
    }

    private fun handleShowDialogDelete() {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.ShowDeleteDialog)
        }
    }

    private fun handleOpenProduct() {
        if (bottomSheetStatus.value.isAnyShown || !isProductAvailable) return

        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.OpenProduct)
            bottomSheetStatus.update { bottomSheet ->
                bottomSheet.mapValues {
                    if (it.key == BottomSheetType.Product)
                        true
                    else it.value
                }
            }
        }
    }

    private fun getProducts() {
        viewModelScope.launchCatchError(block = {
            products.update { product -> product.copy(resultState = ResultState.Loading) }
            val productList = repository.getStoriesProducts(authorId, storyId, mGroupItem.groupName)
            products.update { productList }
        }, onError = {
            products.update { product -> product.copy(resultState = ResultState.Fail(it)) } //TODO() change result state?
        })
    }

    private fun addToCart(product: ContentTaggedProductUiModel, action: StoriesProductAction) {
        requiredLogin {
            viewModelScope.launchCatchError(block = {
                val response = repository.addToCart(
                    productId = product.id,
                    price = product.finalPrice,
                    shopId = authorId,
                    productName = product.title
                )

                if (response) {
                    if (action == StoriesProductAction.ATC)
                        _storiesEvent.emit(StoriesUiEvent.ProductSuccessEvent(action, R.string.stories_product_atc_success))
                    else _storiesEvent.emit(StoriesUiEvent.NavigateEvent(appLink = ApplinkConst.CART))
                } else throw MessageErrorException()

            }, onError = { _storiesEvent.emit(StoriesUiEvent.ShowErrorEvent(it)) })
        }
    }

    private fun handleProductAction(action: StoriesProductAction, product: ContentTaggedProductUiModel) {
        requiredLogin {
            addToCart(product, action)
        }
    }

    private fun handleVariantSheet(product: ContentTaggedProductUiModel) {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.ShowVariantSheet(product))
            bottomSheetStatus.update { bottomSheet ->
                bottomSheet.mapValues {
                    if (it.key == BottomSheetType.GVBS)
                        true
                    else it.value
                }
            }
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
        viewModelScope.launchCatchError(block = {
            val response = repository.deleteStory(storyId)
            if (response) {
                //Remove story~
                val newList = _storiesMainData.value.groupItems.map {
                    if (it == mGroupItem) it.copy(detail = it.detail.copy(detailItems = it.detail.detailItems.filterNot { item -> item.id == storyId }))
                    else it
                }
                _storiesMainData.update {
                        group -> group.copy(groupItems = newList)
                }
            } else {
                _storiesEvent.emit(StoriesUiEvent.ShowErrorEvent(MessageErrorException()))
            }
        }, onError = { _storiesEvent.emit(StoriesUiEvent.ShowErrorEvent(it)) })
    }

    private fun updateDetailData(
        position: Int = mDetailPos,
        event: StoriesDetailItemUiEvent = PAUSE,
        isReset: Boolean = false,
        isSameContent: Boolean = false,
    ) {
        if (position == -1) return
        _detailPos.update { position }
        val positionCached = mGroupItem.detail.selectedDetailPositionCached
        val currentDetail = mGroupItem.detail.copy(
            selectedGroupId = mGroup.groupId,
            selectedDetailPosition = position,
            selectedDetailPositionCached = if (positionCached <= position) position else positionCached,
            detailItems = mGroupItem.detail.detailItems.map { item ->
                item.copy(
                    event = event,
                    resetValue = if (isReset) {
                        _resetValue.update { it.getRandomNumber() }
                        mResetValue
                    } else mResetValue,
                    isSameContent = isSameContent,
                )
            }
        )

        updateMainData(detail = currentDetail, groupPosition = mGroupPos)
    }

    private fun launchRequestInitialData() {
        viewModelScope.launchCatchError(block = {
            _storiesMainData.value = requestStoriesInitialData()
            _groupPos.value = mStoriesMainData.selectedGroupPosition

            if (mGroup == StoriesGroupItem()) _storiesEvent.emit(StoriesUiEvent.EmptyGroupPage)
        }) { exception ->
            _storiesEvent.emit(StoriesUiEvent.ErrorGroupPage(exception))
        }
    }

    private fun checkAndHitTrackActivity() {
        viewModelScope.launchCatchError(block = {
            val detailItem = mGroupItem.detail
            if (mDetailPos <= mLatestTrackPosition) return@launchCatchError
            mLatestTrackPosition = mDetailPos
            val trackerId = detailItem.detailItems[mLatestTrackPosition].meta.activityTracker
            requestSetStoriesTrackActivity(trackerId)
        }) { exception ->
            _storiesEvent.emit(StoriesUiEvent.ErrorSetTracking(exception))
        }
    }

    private suspend fun requestStoriesInitialData(): StoriesUiModel {
        val request = StoriesRequestModel(
            authorID = args.authorId,
            authorType = args.authorType,
            source = args.source,
            sourceID = args.sourceId,
        )
        return repository.getStoriesInitialData(request)
    }

    private suspend fun requestStoriesDetailData(sourceId: String): StoriesDetail {
        val request = StoriesRequestModel(
            authorID = args.authorId,
            authorType = args.authorType,
            source = StoriesSource.STORY_GROUP.value,
            sourceID = sourceId,
        )
        return repository.getStoriesDetailData(request)
    }

    private suspend fun requestSetStoriesTrackActivity(trackerId: String): Boolean {
        val request = StoriesTrackActivityRequestModel(
            id = trackerId,
            action = StoriesTrackActivityActionType.LAST_SEEN.value,
        )
        return repository.setStoriesTrackActivity(request)
    }

    private fun handleNav(appLink: String) {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.NavigateEvent(appLink))
        }
    }

    companion object {
        const val SAVED_INSTANCE_STORIES_MAIN_DATA = "stories_main_data"
        const val SAVED_INSTANCE_STORIES_GROUP_POSITION = "stories_group_position"
        const val SAVED_INSTANCE_STORIES_DETAIL_POSITION = "stories_detail_position"
    }

}
