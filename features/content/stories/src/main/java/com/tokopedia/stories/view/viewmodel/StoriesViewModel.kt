package com.tokopedia.stories.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.stories.view.model.BottomSheetStatusDefault
import com.tokopedia.stories.view.model.BottomSheetType
import com.tokopedia.stories.view.model.ProductBottomSheetUiState
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.domain.model.StoriesAuthorType
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.model.StoriesSource
import com.tokopedia.stories.utils.getRandomNumber
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent.PAUSE
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent.RESUME
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupItemUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.model.StoriesUiState
import com.tokopedia.stories.view.model.isAnyShown
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val repository: StoriesRepository,
    private val userSession: UserSessionInterface,
) : ViewModel() {

    var mShopId: String = ""

    private val _storiesGroup = MutableStateFlow(StoriesGroupUiModel())
    private val _storiesDetail = MutableStateFlow(StoriesDetailUiModel())
    private val bottomSheetStatus = MutableStateFlow(BottomSheetStatusDefault)
    private val products = MutableStateFlow(ProductBottomSheetUiState.ProductList.Empty)
    private val vouchers = MutableStateFlow(TokopointsCatalogMVCSummaryResponse())

    private val productSheet = combine(products, vouchers) {
        products, vouchers -> ProductBottomSheetUiState(products = products, vouchers = vouchers)
    }

    private val mGroupPos = MutableStateFlow(-1)
    private val mDetailPos = MutableStateFlow(-1)
    private val mResetValue = MutableStateFlow(-1)

    val mGroupId: String
        get() {
            val currPosition = mGroupPos.value
            return if (currPosition < 0) ""
            else _storiesGroup.value.groupItems[currPosition].groupId
        }

    private val mGroupSize: Int
        get() = _storiesGroup.value.groupItems.size

    private val mGroupItem: StoriesGroupItemUiModel
        get() {
            val currPosition = mGroupPos.value
            return if (currPosition < 0) StoriesGroupItemUiModel()
            else _storiesGroup.value.groupItems[currPosition]
        }

    private val mDetailSize: Int
        get() {
            val currPosition = mGroupPos.value
            return _storiesGroup.value.groupItems[currPosition].detail.detailItems.size
        }

    private val _uiEvent = MutableSharedFlow<StoriesUiEvent>(extraBufferCapacity = 100)
    val uiEvent: Flow<StoriesUiEvent>
        get() = _uiEvent

    val uiState = combine(
        _storiesGroup,
        _storiesDetail,
        bottomSheetStatus,
        productSheet,
    ) { storiesCategories, storiesItem, bottomSheet, productSheet ->
        StoriesUiState(
            storiesGroup = storiesCategories,
            storiesDetail = storiesItem,
            bottomSheetStatus = bottomSheet,
            productSheet = productSheet,
        )
    }

    /**
     * TODO()
    val storyId : String
        get()  {
            val currentGroup = _storiesGroup.value.firstOrNull { story -> story.selected }
            return currentGroup?.details?.get(currentGroup.selectedDetail)?.id.orEmpty()
        }
    */

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SetArgumentsData -> handleSetInitialData(action.data)
            is StoriesUiAction.SetGroupMainData -> handleGroupMainData(action.selectedGroup)
            is StoriesUiAction.SetGroup -> handleSetGroup(action.selectedGroup)
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
            else -> {}
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        mShopId = data?.getString(SHOP_ID, "").orEmpty()

        viewModelScope.launchCatchError(block = {
            // TODO handle loading properly
            delay(3000)

            _storiesGroup.value = requestStoriesInitialData()
            mGroupPos.value = _storiesGroup.value.selectedGroupPosition
        }) { exception ->
            Timber.d("fail fetch main data $exception")
        }
    }

    private fun handleGroupMainData(selectedGroup: Int) {
        mGroupPos.update { selectedGroup }
        setInitialDetailData()
    }

    private fun handleSetGroup(position: Int) {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.SelectGroup(position))
        }
    }

    private fun handleNext() {
        val newGroupPosition = mGroupPos.value.plus(1)
        val newDetailPosition = mDetailPos.value.plus(1)

        when {
            newDetailPosition < mDetailSize -> updateDetailData(position = newDetailPosition)
            newGroupPosition < mGroupSize -> handleSetGroup(position = newGroupPosition)
            else -> viewModelScope.launch { _uiEvent.emit(StoriesUiEvent.FinishedAllStories) }
        }
    }

    private fun handlePrevious() {
        val newGroupPosition = mGroupPos.value.minus(1)
        val newDetailPosition = mDetailPos.value.minus(1)

        when {
            newDetailPosition > -1 -> updateDetailData(position = newDetailPosition)
            newGroupPosition > -1 -> handleSetGroup(position = newGroupPosition)
            else -> updateDetailData(event = RESUME, isReset = true)
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
    }

    private fun setInitialDetailData() {
        viewModelScope.launchCatchError(block = {
            val isCached = mGroupItem.detail != StoriesDetailUiModel()

            val detailData = if (isCached) mGroupItem.detail
            else requestStoriesDetailData()

            updateGroupData(detail = detailData)

            // TODO handle loading properly
            delay(3000)

            val isReset = detailData.selectedDetailPositionCached == detailData.detailItems.size.minus(1)
            updateDetailData(
                position = detailData.selectedDetailPositionCached,
                isReset = isReset,
            )
        }) { exception ->
            Timber.d("fail fetch new detail $exception")
        }
    }

    private fun updateGroupData(detail: StoriesDetailUiModel) {
        _storiesGroup.update { group ->
            group.copy(
                selectedGroupId = mGroupId,
                selectedGroupPosition = mGroupPos.value,
                groupHeader = group.groupHeader.mapIndexed { index, storiesGroupHeader ->
                    storiesGroupHeader.copy(isSelected = index == mGroupPos.value)
                },
                groupItems = group.groupItems.mapIndexed { index, storiesGroupItemUiModel ->
                    if (index == mGroupPos.value) {
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
            _uiEvent.emit(StoriesUiEvent.OpenKebab)
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
            _uiEvent.emit(StoriesUiEvent.ShowDeleteDialog)
        }
    }

    private fun handleOpenProduct() {
        if (bottomSheetStatus.value.isAnyShown) return

        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.OpenProduct)
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
            val productsDeferred = asyncCatchError(block = { repository.getStoriesProducts(mShopId, "") }) { emptyList() }
            val vouchersDeferred = asyncCatchError(block = { repository.getMvcWidget(mShopId) }) { TokopointsCatalogMVCSummaryResponse() }
            products.update { productList -> productList.copy(products = productsDeferred.await().orEmpty(), resultState = ResultState.Success) }
            vouchers.update { vouchersDeferred.await() ?: TokopointsCatalogMVCSummaryResponse() }
        }, onError = {
            products.update { product -> product.copy(resultState = ResultState.Fail(it)) } //TODO() change result state?
        })
    }

    private fun addToCart(product: ContentTaggedProductUiModel) {
        requiredLogin {
            viewModelScope.launchCatchError(block = {
                val response = repository.addToCart(
                    productId = product.id,
                    price = product.finalPrice,
                    shopId = "",
                    productName = product.title
                )
            }, onError = {})
        }
    }

    private fun handleProductAction(action: StoriesProductAction, product: ContentTaggedProductUiModel) {
        requiredLogin {
            if (action == StoriesProductAction.Buy) {
                viewModelScope.launch {
                    _uiEvent.emit(StoriesUiEvent.NavigateEvent(appLink = ApplinkConst.CART))
                }
            } else {
                addToCart(product)
            }
        }
    }

    private fun handleVariantSheet(product: ContentTaggedProductUiModel) {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.ShowVariantSheet(product))
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
                _uiEvent.emit(
                    StoriesUiEvent.Login { fn(false) }
                )
            }
        }
    }

    private fun handleDeleteStory() {
        viewModelScope.launchCatchError(block = {
            //repository.deleteStory(storyId)
            //TODO if true emit next story else show toast
        }, onError = {})
    }

    private fun updateDetailData(
        position: Int = mDetailPos.value,
        event: StoriesDetailItemUiEvent = PAUSE,
        isReset: Boolean = false,
        isSameContent: Boolean = false,
    ) {
        mDetailPos.value = position
        val positionCached = mGroupItem.detail.selectedDetailPositionCached
        val currentDetail = mGroupItem.detail.copy(
            selectedGroupId = mGroupId,
            selectedDetailPosition = position,
            selectedDetailPositionCached = if (positionCached <= position) position else positionCached,
            detailItems = mGroupItem.detail.detailItems.map { item ->
                item.copy(
                    event = event,
                    resetValue = if (isReset) {
                        mResetValue.value = mResetValue.value.getRandomNumber()
                        mResetValue.value
                    } else mResetValue.value,
                    isSameContent = isSameContent,
                )
            }
        )

        updateGroupData(detail = currentDetail)
        _storiesDetail.update { currentDetail }
    }

    private suspend fun requestStoriesInitialData(): StoriesGroupUiModel {
        val request = StoriesRequestModel(
            authorID = mShopId,
            authorType = StoriesAuthorType.SHOP.value,
            source = StoriesSource.SHOP_ENTRY_POINT.value,
            sourceID = "",
        )
        return repository.getStoriesInitialData(request)
    }

    private suspend fun requestStoriesDetailData(): StoriesDetailUiModel {
        val request = StoriesRequestModel(
            authorID = mShopId,
            authorType = StoriesAuthorType.SHOP.value,
            source = "",
            sourceID = mGroupItem.groupId,
        )
        return repository.getStoriesDetailData(request)
    }

    companion object {
        private const val SHOP_ID = "shop_id"
    }

}
