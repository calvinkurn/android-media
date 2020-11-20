package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.feature.filter.domain.GetProductManageFilterOptionsUseCase
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ITEM_CATEGORIES_INDEX
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ITEM_ETALASE_INDEX
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ITEM_OTHER_FILTER_INDEX
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ITEM_SORT_INDEX
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipsAdapter.Companion.MAXIMUM_CHIPS
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductManageFilterViewModel @Inject constructor(
        private val getProductManageFilterOptionsUseCase: GetProductManageFilterOptionsUseCase,
        private val userSession: UserSessionInterface,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _filterOptionsResponse = MutableLiveData<Result<FilterOptionsResponse>>()
    val filterOptionsResponse: LiveData<Result<FilterOptionsResponse>>
        get() = _filterOptionsResponse

    private val _filterData = MutableLiveData<MutableList<FilterUiModel>>()
    val filterData: LiveData<MutableList<FilterUiModel>>
        get() = _filterData

    private var selectedSort: FilterDataUiModel? = null
    private var selectedEtalase: FilterDataUiModel? = null

    fun getData(shopId: String, isNeedToReloadEtalaseData: Boolean) {
        launchCatchError(block = {
            val combinedResponse = withContext(dispatcher.io) {
                getProductManageFilterOptionsUseCase.isNeedToReloadEtalaseData = isNeedToReloadEtalaseData
                getProductManageFilterOptionsUseCase.params = GetProductManageFilterOptionsUseCase.createRequestParams(shopId, isMyShop(shopId))
                getProductManageFilterOptionsUseCase.executeOnBackground()
            }
            combinedResponse.let {
                _filterOptionsResponse.postValue(Success(it))
            }
        }) {
            _filterOptionsResponse.postValue(Fail(it))
        }
    }

    fun updateData(filterData: List<FilterUiModel>) {
        _filterData.value = (filterData.toMutableList())
        findSelectedData(filterData.subList(ITEM_SORT_INDEX, ITEM_CATEGORIES_INDEX))
    }

    fun updateSpecificData(filterUiModel: FilterUiModel, index: Int) {
        val currentValue = _filterData.value?.toMutableList()
        currentValue?.let {
            it[index] = filterUiModel
        }
        findSelectedData(listOf(filterUiModel))
        _filterData.value = currentValue
    }

    fun updateSelect(filterData: FilterDataUiModel) {
        val currentData = _filterData.value?.toMutableList()
        val dataToSelect = getDataFromList(currentData?.slice(ITEM_CATEGORIES_INDEX..ITEM_OTHER_FILTER_INDEX), filterData)
        dataToSelect?.let {
            it.third.select = !filterData.select
            if(it.first) {
                currentData?.getOrNull(dataToSelect.second)?.data?.sortByDescending { data -> data.select }
            }
        }
        _filterData.value = currentData
    }

    fun updateSelect(filterData: FilterDataUiModel, title: String) {
        val currentData = _filterData.value?.toMutableList()
        if (title == ProductManageFilterMapper.SORT_HEADER) {
            selectedSort?.let {
                val selectedPair = getSortFromList(currentData?.getOrNull(ITEM_SORT_INDEX), it)
                selectedPair?.let { pair ->
                    pair.second.select = !pair.second.select
                    if (pair.second == filterData) {
                        selectedSort = null
                        _filterData.value = currentData
                        return
                    }
                }
            }
            val dataToSelect = getSortFromList(currentData?.getOrNull(ITEM_SORT_INDEX), filterData)
            dataToSelect?.let {
                it.second.select = !filterData.select
                selectedSort = it.second
                if(it.first) {
                    currentData?.getOrNull(ITEM_SORT_INDEX)?.data?.sortByDescending { data -> data.select }
                }
            }
        } else {
            selectedEtalase?.let {
                val selectedPair = getDataFromList(currentData?.subList(ITEM_ETALASE_INDEX, ITEM_CATEGORIES_INDEX), it)
                selectedPair?.let { triple ->
                    triple.third.select = !triple.third.select
                    if (triple.third == filterData) {
                        selectedEtalase = null
                        _filterData.value = currentData
                        return
                    }
                }
            }
            val dataToSelect = getDataFromList(currentData?.subList(ITEM_ETALASE_INDEX, ITEM_CATEGORIES_INDEX), filterData)
            dataToSelect?.let {
                it.third.select = !filterData.select
                selectedEtalase = it.third
                if(it.first) {
                    currentData?.getOrNull(ITEM_ETALASE_INDEX)?.data?.sortByDescending { data -> data.select }
                }
            }
        }
        _filterData.value = currentData
    }

    fun updateShow(filterUiModel: FilterUiModel) {
        val currentData = _filterData.value?.toMutableList()
        currentData?.let {
            val filterIndexOfData = it.indexOf(filterUiModel)
            it[filterIndexOfData].isChipsShown = !filterUiModel.isChipsShown
            _filterData.value = it
        }
    }

    fun updateShow(isShow: List<Boolean>) {
        val currentData = _filterData.value?.toMutableList()
        currentData?.let {
            it.forEachIndexed { index, filterViewModel ->
                filterViewModel.isChipsShown = isShow[index]
            }
            _filterData.value = it
        }
    }

    fun clearSelected() {
        val clearedData = _filterData.value
        clearedData?.forEach { filterViewModel ->
            filterViewModel.data.forEach { filterData ->
                filterData.select = false
            }
        }
        selectedEtalase = null
        selectedSort = null
        _filterData.value = clearedData
    }

    private fun isMyShop(shopId: String) = userSession.shopId == shopId

    private fun getDataFromList(currentData: List<FilterUiModel>?, data: FilterDataUiModel): Triple<Boolean, Int, FilterDataUiModel>? {
        currentData?.forEachIndexed { index, filterViewModel ->
            filterViewModel.data.forEach { filterData ->
                if (filterData.id == data.id) {
                    val needSort = filterViewModel.data.indexOf(filterData) > MAXIMUM_CHIPS - 1
                    return Triple(needSort, index + ITEM_CATEGORIES_INDEX, filterData)
                }
            }
        }
        return null
    }

    private fun getSortFromList(sortData: FilterUiModel?, data: FilterDataUiModel): Pair<Boolean, FilterDataUiModel>? {
        sortData?.data?.forEach {
            if (it.id == data.id && it.value == data.value) {
                val needSort = sortData.data.indexOf(it) > MAXIMUM_CHIPS - 1
                return Pair(needSort, it)
            }
        }
        return null
    }

    private fun findSelectedData(filterViewModels: List<FilterUiModel>) {
        filterViewModels.forEach {
            if(it.title == ProductManageFilterMapper.SORT_HEADER) {
                it.data.forEach { filterDataModel ->
                    if(filterDataModel.select) {
                        selectedSort = filterDataModel
                    }
                }
            } else {
                it.data.forEach { filterDataModel ->
                    if(filterDataModel.select) {
                        selectedEtalase = filterDataModel
                    }
                }
            }
        }
    }

    fun getSelectedFilters(): List<FilterDataUiModel> {
        return _filterData.value?.flatMap { filterDataUiModels ->
            filterDataUiModels.data.filter { filterDataUiModel ->
                filterDataUiModel.select
            }
        }.orEmpty()
    }
}