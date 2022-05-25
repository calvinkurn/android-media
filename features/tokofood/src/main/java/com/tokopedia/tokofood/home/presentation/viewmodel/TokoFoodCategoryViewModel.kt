package com.tokopedia.tokofood.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodCategoryMapper.addLoadingCategoryIntoList
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodCategoryMapper.mapCategoryLayoutList
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodListUiModel
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
        _categoryLayoutList.value = Success(data)
    }

    fun getCategoryLayout(localCacheModel: LocalCacheModel, option: Int = 0,
                          sortBy: Int = 0, page: String = "") {
        launchCatchError(block = {
            setPageKey(page)
            categoryLayoutItemList.clear()
            val categoryResponse = withContext(dispatchers.io) {
                tokoFoodMerchantListUseCase.execute(
                    localCacheModel = localCacheModel,
                    option = option,
                    brandId = "",
                    sortBy = sortBy,
                    orderById = 0,
                    pageKey = pageKey)
            }

            setPageKey(categoryResponse.data.nextPageKey)
            categoryLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
            val data = TokoFoodListUiModel(
                items = categoryLayoutItemList,
                state = TokoFoodLayoutState.SHOW
            )

            _categoryLayoutList.value = Success(data)
        }){

        }
    }

    fun loadMoreMerchant(localCacheModel: LocalCacheModel, option: Int = 0,
                          sortBy: Int = 0) {
        launchCatchError(block = {
            val categoryResponse = withContext(dispatchers.io) {
                tokoFoodMerchantListUseCase.execute(
                    localCacheModel = localCacheModel,
                    option = option,
                    brandId = "",
                    sortBy = sortBy,
                    orderById = 0,
                    pageKey = pageKey)
            }

            setPageKey(categoryResponse.data.nextPageKey)
            categoryLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
            val data = TokoFoodListUiModel(
                items = categoryLayoutItemList,
                state = TokoFoodLayoutState.LOAD_MORE
            )

            _categoryLoadMore.value = Success(data)
        }){

        }
    }



    fun onScrollProductList(index: Int?, itemCount: Int, localCacheModel: LocalCacheModel, option: Int = 0,
                            sortBy: Int = 0) {
        val lastItemIndex = itemCount - 1
        val containsLastItemIndex = index
        val scrolledToLastItem = containsLastItemIndex == lastItemIndex
        val hasNextPage = pageKey.isNotEmpty()

        if(scrolledToLastItem && hasNextPage) {
            loadMoreMerchant(localCacheModel = localCacheModel,
                option = option,
                sortBy = sortBy)
        }
    }

    private fun setPageKey(pageNew:String) {
        pageKey = pageNew
    }
}