package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.core.common.category.domain.interactor.GetCategoryListUseCase
import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.CombinedResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.domain.GetProductListMetaUseCase
import com.tokopedia.product.manage.feature.filter.domain.ProductManageFilterCombinedUseCase
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ProductManageFilterViewModel @Inject constructor(
        private val productManageFilterCombinedUseCase: ProductManageFilterCombinedUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _combinedResponse = MutableLiveData<Result<CombinedResponse>>()
    val combinedResponse: LiveData<Result<CombinedResponse>>
        get() = _combinedResponse

    private val _filterData = MutableLiveData<MutableList<FilterViewModel>>()
    val filterData: LiveData<MutableList<FilterViewModel>>
        get() = _filterData

    private var selectedSort: FilterDataViewModel? = null
    private var selectedEtalase: FilterDataViewModel? = null

    fun getData(shopId: String) {
        productManageFilterCombinedUseCase.params = ProductManageFilterCombinedUseCase.createRequestParams(shopId, isMyShop(shopId))
        launchCatchError(block = {
            val combinedResponse = productManageFilterCombinedUseCase.executeOnBackground()
            combinedResponse.let {
                _combinedResponse.postValue(Success(it))
            }
        }) {
            Fail(it)
        }
    }

    fun updateData(filterData: List<FilterViewModel>) {
        _filterData.postValue(filterData.toMutableList())
    }

    fun updateSpecificData(filterViewModel: FilterViewModel, index: Int) {
        val currentValue = _filterData.value
        currentValue?.let {
            it[index] = filterViewModel
        }
        _filterData.postValue(currentValue)
    }

    fun updateSelect(filterData: FilterDataViewModel) {
        val currentData = _filterData.value
        val indexes = getIndexOfData(filterData)
        currentData?.get(indexes.first())?.data?.get(indexes.last())?.select = !filterData.select
        _filterData.postValue(currentData)
    }

    fun updateSelect(filterData: FilterDataViewModel, title: String) {
        var selectedIndex = emptyList<Int>()
        val currentData = _filterData.value
        val indexes = getIndexOfData(filterData)
        currentData?.get(indexes.first())?.data?.get(indexes.last())?.select = !filterData.select
        if(title == ProductManageFilterMapper.SORT_HEADER) {
            selectedSort?.let {
                selectedIndex = getIndexOfData(it)
            }
            selectedSort = currentData?.get(indexes.first())?.data?.get(indexes.last())
        } else {
            selectedEtalase?.let {
                selectedIndex = getIndexOfData(it)
            }
            selectedEtalase = currentData?.get(indexes.first())?.data?.get(indexes.last())
        }
        if(selectedIndex.isNotEmpty()) {
            currentData?.get(selectedIndex.first())?.data?.get(selectedIndex.last())?.select = false
        }
        _filterData.postValue(currentData)
    }

    fun updateShow(filterViewModel: FilterViewModel) {
        val currentData = _filterData.value
        currentData?.let {
            val filterIndexOfData = it.indexOf(filterViewModel)
            it[filterIndexOfData].isChipsShown = !filterViewModel.isChipsShown
            _filterData.postValue(it)
        }
    }

    fun clearSelected() {
        val clearedData = _filterData.value
        clearedData?.forEach {filterViewModel ->
            filterViewModel.data.forEach { filterData ->
                filterData.select = false
            }
        }
        selectedEtalase = null
        selectedSort = null
        _filterData.postValue(clearedData)
    }

    private fun isMyShop(shopId: String) = userSession.shopId == shopId

    private fun getIndexOfData(data: FilterDataViewModel): List<Int> {
        var filterIndex = 0
        var filterIndexOfData = 0
        var dataIndexOfData = 0
        val currentData = _filterData.value
        currentData?.forEach {
            if(it.data.indexOf(data) != -1) {
                dataIndexOfData = it.data.indexOf(data)
                filterIndexOfData = filterIndex
            }
            filterIndex++
        }
        return listOf(filterIndexOfData, dataIndexOfData)
    }
}