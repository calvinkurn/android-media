package com.tokopedia.tokofood.feature.home.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.addErrorState
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.addLoadingCategoryIntoList
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.addProgressBar
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.mapCategoryEmptyLayout
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.mapCategoryLayoutList
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.removeProgressBar
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListParams
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodUiState
import com.tokopedia.tokofood.feature.home.presentation.uimodel.UiEvent.STATE_ERROR
import com.tokopedia.tokofood.feature.home.presentation.uimodel.UiEvent.STATE_FETCH_LOAD_MORE
import com.tokopedia.tokofood.feature.home.presentation.uimodel.UiEvent.STATE_FETCH_MERCHANT_LIST_DATA
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn

@ExperimentalCoroutinesApi
@FlowPreview
class TokoFoodCategoryViewModel @Inject constructor(
    private val tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _inputState = MutableSharedFlow<TokoFoodUiState>(Int.ONE)

    init {
        _inputState.tryEmit(TokoFoodUiState())
    }

    val flowLayoutList: SharedFlow<Pair<Result<TokoFoodListUiModel>, Boolean>> =
        _inputState.flatMapConcat { inputState ->
            getFlowCategory(inputState).catch {
                if (inputState.uiState == STATE_FETCH_MERCHANT_LIST_DATA) emit(Pair(Fail(it), true))
                else {
                    emit(getRemovalProgressBar())
                    emit(Pair(Fail(it), false))
                }
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val categoryLayoutItemList :MutableList<Visitable<*>> = mutableListOf()
    private var pageKey = INITIAL_PAGE_KEY_MERCHANT

    companion object {
        private const val INITIAL_PAGE_KEY_MERCHANT = "0"
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
    }

    private fun getFlowCategory(inputState: TokoFoodUiState): Flow<Pair<Result<TokoFoodListUiModel>, Boolean>> {
        return flow {
                when (inputState.uiState) {
                    STATE_ERROR -> emit(getErrorState(inputState.throwable))
                    STATE_FETCH_MERCHANT_LIST_DATA -> {
                        emit(getLoadingState())
                        emit(getCategoryLayout(inputState.localCacheModel, inputState.merchantListParamsModel))
                    }
                    STATE_FETCH_LOAD_MORE -> {
                        emit(getProgressBar())
                        emit(getLoadMoreMerchant(inputState.localCacheModel, inputState.merchantListParamsModel))
                    }
                }
            }
    }

    fun setErrorState(throwable: Throwable) {
        _inputState.tryEmit(TokoFoodUiState(uiState = STATE_ERROR, throwable = throwable))
    }

    fun setCategoryLayout(
        localCacheModel: LocalCacheModel, option: Int = Int.ZERO,
        sortBy: Int = Int.ZERO, cuisine: String = "", brandUId: String = ""
    ){
        _inputState.tryEmit(TokoFoodUiState(uiState = STATE_FETCH_MERCHANT_LIST_DATA,
            localCacheModel = localCacheModel,
            merchantListParamsModel = TokoFoodMerchantListParams(
                option, sortBy, cuisine, brandUId
            )
        ))
    }

    fun onScrollProductList(containsLastItemIndex: Int, itemCount: Int, localCacheModel: LocalCacheModel, option: Int = Int.ZERO,
                            sortBy: Int = Int.ZERO, cuisine: String = "", brandUId: String = "") {
        if(shouldLoadMore(containsLastItemIndex, itemCount)) {
            setLoadMoreMerchant(localCacheModel = localCacheModel,
                option = option,
                sortBy = sortBy,
                cuisine = cuisine,
                brandUId = brandUId
            )
        }
    }

    fun isShownEmptyState(): Boolean {
        val layoutList = categoryLayoutItemList.toMutableList()
        val isError = layoutList.firstOrNull { it is TokoFoodErrorStateUiModel } != null
        return isError
    }

    fun shouldLoadMore(containsLastItemIndex: Int, itemCount: Int): Boolean {
        val lastItemIndex = itemCount - Int.ONE
        val scrolledToLastItem = (containsLastItemIndex == lastItemIndex
                && containsLastItemIndex.isMoreThanZero())
        val hasNextPage = pageKey.isNotEmpty()
        val layoutList = categoryLayoutItemList.toMutableList()
        val isLoading = layoutList.firstOrNull { it is TokoFoodProgressBarUiModel } != null
        val isError = layoutList.firstOrNull { it is TokoFoodErrorStateUiModel } != null

        return scrolledToLastItem && hasNextPage && !isLoading && !isError
    }

    private fun setLoadMoreMerchant(
        localCacheModel: LocalCacheModel,
        option: Int,
        sortBy: Int,
        cuisine: String,
        brandUId: String
    ){
        _inputState.tryEmit(TokoFoodUiState(uiState = STATE_FETCH_LOAD_MORE,
            localCacheModel = localCacheModel,
            merchantListParamsModel = TokoFoodMerchantListParams(
                option, sortBy, cuisine, brandUId
            )
        ))
    }

    private fun getLoadingState(): Pair<Result<TokoFoodListUiModel>, Boolean> {
        setPageKey(INITIAL_PAGE_KEY_MERCHANT)
        categoryLayoutItemList.clear()
        categoryLayoutItemList.addLoadingCategoryIntoList()
        val data = Success(TokoFoodListUiModel(
            items = categoryLayoutItemList,
            state = TokoFoodLayoutState.LOADING
        ))
        return Pair(data, false)
    }

    private fun getErrorState(throwable: Throwable): Pair<Result<TokoFoodListUiModel>, Boolean> {
        categoryLayoutItemList.clear()
        categoryLayoutItemList.addErrorState(throwable)
        val data = Success(TokoFoodListUiModel(
            items = categoryLayoutItemList,
            state = TokoFoodLayoutState.HIDE
        ))
        return Pair(data, false)
    }

    private suspend fun getCategoryLayout(localCacheModel: LocalCacheModel,
                                  merchantListParamsModel: TokoFoodMerchantListParams):
            Pair<Result<TokoFoodListUiModel>, Boolean> {
            categoryLayoutItemList.clear()
            val categoryResponse = withContext(dispatchers.io) {
                tokoFoodMerchantListUseCase.execute(
                    localCacheModel = localCacheModel,
                    option = merchantListParamsModel.option,
                    sortBy = merchantListParamsModel.sortBy,
                    cuisine = merchantListParamsModel.cuisine,
                    brandUId = merchantListParamsModel.brandUId,
                    pageKey = pageKey)
            }

            setPageKey(categoryResponse.data.nextPageKey)
            if (categoryResponse.data.merchants.isNotEmpty()) {
                categoryLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
            } else {
                categoryLayoutItemList.mapCategoryEmptyLayout()
            }
            val data = TokoFoodListUiModel(
                items = categoryLayoutItemList,
                state = TokoFoodLayoutState.SHOW
            )

           return  Pair(Success(data), true)
    }

    private fun getProgressBar(): Pair<Result<TokoFoodListUiModel>, Boolean> {
        categoryLayoutItemList.addProgressBar()
        val data = TokoFoodListUiModel(
            items = categoryLayoutItemList,
            state = TokoFoodLayoutState.UPDATE
        )
        return Pair(Success(data), false)
    }

    private suspend fun getLoadMoreMerchant(localCacheModel: LocalCacheModel,
                                         merchantListParamsModel: TokoFoodMerchantListParams):
            Pair<Result<TokoFoodListUiModel>, Boolean>   {
            val categoryResponse = withContext(dispatchers.io) {
                tokoFoodMerchantListUseCase.execute(
                    localCacheModel = localCacheModel,
                    option = merchantListParamsModel.option,
                    sortBy = merchantListParamsModel.sortBy,
                    cuisine = merchantListParamsModel.cuisine,
                    pageKey = pageKey,
                    brandUId = merchantListParamsModel.brandUId
                )
            }

            setPageKey(categoryResponse.data.nextPageKey)
            categoryLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
            categoryLayoutItemList.removeProgressBar()
            val data = TokoFoodListUiModel(
                items = categoryLayoutItemList,
                state = TokoFoodLayoutState.LOAD_MORE
            )

            return Pair(Success(data), false)
    }

    private fun getRemovalProgressBar(): Pair<Result<TokoFoodListUiModel>, Boolean> {
        categoryLayoutItemList.removeProgressBar()
        val data = TokoFoodListUiModel(
            items = categoryLayoutItemList,
            state = TokoFoodLayoutState.LOAD_MORE
        )
        return Pair(Success(data), false)
    }

    private fun setPageKey(pageNew:String) {
        pageKey = pageNew
    }
}