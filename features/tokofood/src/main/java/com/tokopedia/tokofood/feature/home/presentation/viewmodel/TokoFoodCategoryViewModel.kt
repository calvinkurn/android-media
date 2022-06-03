package com.tokopedia.tokofood.feature.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.addErrorState
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.addLoadingCategoryIntoList
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.addProgressBar
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.mapCategoryLayoutList
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.removeProgressBar
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoFoodCategoryViewModel @Inject constructor(
    private val tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val layoutList: LiveData<Result<TokoFoodListUiModel>>
        get() = _categoryLayoutList
    val loadMore: LiveData<Result<TokoFoodListUiModel>>
        get() = _categoryLoadMore

    private val _categoryLayoutList = MutableLiveData<Result<TokoFoodListUiModel>>()
    private val _categoryLoadMore = MutableLiveData<Result<TokoFoodListUiModel>>()

    private val categoryLayoutItemList :MutableList<Visitable<*>> = mutableListOf()
    private var pageKey = ""

    fun getLoadingState() {
        categoryLayoutItemList.clear()
        categoryLayoutItemList.addLoadingCategoryIntoList()
        val data = TokoFoodListUiModel(
            items = categoryLayoutItemList,
            state = TokoFoodLayoutState.LOADING
        )
        _categoryLayoutList.postValue(Success(data))
    }

    fun getErrorState(throwable: Throwable) {
        categoryLayoutItemList.clear()
        categoryLayoutItemList.addErrorState(throwable)
        val data = TokoFoodListUiModel(
            items = categoryLayoutItemList,
            state = TokoFoodLayoutState.HIDE
        )
        _categoryLayoutList.postValue(Success(data))
    }

    fun getCategoryLayout(localCacheModel: LocalCacheModel, option: Int = 0,
                          sortBy: Int = 0, cuisine: String = "", page: String = "") {
        launchCatchError(block = {
            setPageKey(page)
            categoryLayoutItemList.clear()
            val categoryResponse = withContext(dispatchers.io) {
                tokoFoodMerchantListUseCase.execute(
                    localCacheModel = localCacheModel,
                    option = option,
                    sortBy = sortBy,
                    cuisine = cuisine,
                    pageKey = pageKey)
            }

            setPageKey(categoryResponse.data.nextPageKey)
            categoryLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
            val data = TokoFoodListUiModel(
                items = categoryLayoutItemList,
                state = TokoFoodLayoutState.SHOW
            )

            _categoryLayoutList.postValue(Success(data))
        }){
            _categoryLayoutList.postValue(Fail(it))
        }
    }

    fun onScrollProductList(containsLastItemIndex: Int?, itemCount: Int, localCacheModel: LocalCacheModel, option: Int = 0,
                            sortBy: Int = 0, cuisine: String = "") {
        val lastItemIndex = itemCount - 1
        val scrolledToLastItem = (containsLastItemIndex == lastItemIndex
                && containsLastItemIndex.isMoreThanZero()
                && itemCount.isMoreThanZero())
        val hasNextPage = pageKey.isNotEmpty()
        val layoutList = categoryLayoutItemList.toMutableList()
        val isLoading = layoutList.firstOrNull { it is TokoFoodProgressBarUiModel } != null
        val isError = layoutList.firstOrNull { it is TokoFoodErrorStateUiModel } != null

        if(scrolledToLastItem && hasNextPage && !isLoading && !isError) {
            showProgressBar()
            loadMoreMerchant(localCacheModel = localCacheModel,
                option = option,
                sortBy = sortBy,
                cuisine = cuisine
            )
        }
    }

    private fun setPageKey(pageNew:String) {
        pageKey = pageNew
    }

    private fun showProgressBar(){
        categoryLayoutItemList.addProgressBar()
        val data = TokoFoodListUiModel(
            items = categoryLayoutItemList,
            state = TokoFoodLayoutState.UPDATE
        )
        _categoryLayoutList.postValue(Success(data))
    }

    private fun loadMoreMerchant(localCacheModel: LocalCacheModel, option: Int = 0,
                                 sortBy: Int = 0, cuisine: String = "") {
        launchCatchError(block = {
            val categoryResponse = withContext(dispatchers.io) {
                tokoFoodMerchantListUseCase.execute(
                    localCacheModel = localCacheModel,
                    option = option,
                    sortBy = sortBy,
                    cuisine = cuisine,
                    pageKey = pageKey)
            }

            setPageKey(categoryResponse.data.nextPageKey)
            categoryLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
            categoryLayoutItemList.removeProgressBar()
            val data = TokoFoodListUiModel(
                items = categoryLayoutItemList,
                state = TokoFoodLayoutState.LOAD_MORE
            )

            _categoryLoadMore.postValue(Success(data))
        }){

        }
    }

}