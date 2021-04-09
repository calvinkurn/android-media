package com.tokopedia.shop.score.penalty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyTypesParam
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopScorePenaltyTypesUseCase
import com.tokopedia.shop.score.penalty.presentation.model.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopPenaltyViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val getShopScorePenaltyTypesUseCase: GetShopScorePenaltyTypesUseCase,
        private val penaltyMapper: PenaltyMapper
) : BaseViewModel(dispatchers.main) {

    private val _penaltyPageData = MutableLiveData<Result<PenaltyDataWrapper>>()
    val penaltyPageData: LiveData<Result<PenaltyDataWrapper>>
        get() = _penaltyPageData

    private val _filterPenaltyData = MutableLiveData<Result<List<PenaltyFilterUiModel>>>()
    val filterPenaltyData: LiveData<Result<List<PenaltyFilterUiModel>>>
        get() = _filterPenaltyData

    private val _updateSortFilterSelected = MutableLiveData<Result<List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>>>()
    val updateSortFilterSelected: LiveData<Result<List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>>>
        get() = _updateSortFilterSelected

    private val _resetFilterResult = MutableLiveData<Result<List<PenaltyFilterUiModel>>>()
    val resetFilterResult: LiveData<Result<List<PenaltyFilterUiModel>>> = _resetFilterResult

    private val _updateFilterSelected = MutableLiveData<Result<Pair<List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>, String>>>()
    val updateFilterSelected: LiveData<Result<Pair<List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>, String>>> = _updateFilterSelected

    private var penaltyFilterUiModel = mutableListOf<PenaltyFilterUiModel>()
    private var itemSortFilterWrapperList = mutableListOf<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>()

    fun setItemSortFilterWrapperList(penaltyFilterList: List<PenaltyFilterUiModel>, sortFilterItemList: List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>) {
        this.itemSortFilterWrapperList = sortFilterItemList.toMutableList()
        this.penaltyFilterUiModel = penaltyFilterList.toMutableList()
    }

    fun getPenaltyFilterUiModelList() = penaltyFilterUiModel

    fun getSortFilterItemList() = itemSortFilterWrapperList

    fun getDataPenalty() {
        launchCatchError(block = {
            getShopScorePenaltyTypesUseCase.requestParams = GetShopScorePenaltyTypesUseCase.createParams(ShopScorePenaltyTypesParam())
            val penaltyData = penaltyMapper.mapToPenaltyData(getShopScorePenaltyTypesUseCase.executeOnBackground())

            if (itemSortFilterWrapperList.isNullOrEmpty()) {
                itemSortFilterWrapperList = penaltyData.itemDetailPenaltyFilterUiModel?.itemSortFilterWrapperList?.toMutableList() ?: mutableListOf()
            } else {
                penaltyData.itemDetailPenaltyFilterUiModel?.itemSortFilterWrapperList = itemSortFilterWrapperList
            }
            _penaltyPageData.postValue(Success(penaltyData))
        }, onError = {
            _penaltyPageData.postValue(Fail(it))
        })
    }

    fun getFilterPenalty(itemFilterTypePenalty: List<FilterTypePenaltyUiModelWrapper.ItemFilterTypePenalty>, sortBy: Pair<String, Boolean>) {
        launchCatchError(block = {
            val filterPenaltyList = penaltyMapper.mapToPenaltyFilterBottomSheet()
            filterPenaltyList.find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.chipsFilerList?.mapIndexed { index, data ->
                data.isSelected = itemFilterTypePenalty.getOrNull(index)?.isSelected ?: false
                data.title = itemFilterTypePenalty.getOrNull(index)?.title ?: ""
            }
            filterPenaltyList.find { it.title == ShopScoreConstant.TITLE_SORT }?.chipsFilerList?.find { it.title == sortBy.first }?.isSelected = sortBy.second
            penaltyFilterUiModel = filterPenaltyList.toMutableList()
            _filterPenaltyData.postValue(Success(penaltyFilterUiModel))
        }, onError = {
            _filterPenaltyData.postValue(Fail(it))
        })
    }

    fun updateSortFilterSelected(titleFilter: String, chipType: String) {
        launchCatchError(block = {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            itemSortFilterWrapperList.find { it.sortFilterItem?.title == titleFilter }?.isSelected = updateChipsSelected
            _updateSortFilterSelected.value = Success(itemSortFilterWrapperList)
        }, onError = {
            _updateSortFilterSelected.value = Fail(it)
        })
    }

    fun updateFilterSelected(titleFilter: String, chipType: String, position: Int) {
        launchCatchError(block = {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            penaltyFilterUiModel.find { it.title == titleFilter }?.chipsFilerList?.mapIndexed { index, chipsFilterPenaltyUiModel ->
                if (index == position) {
                    chipsFilterPenaltyUiModel.isSelected = !updateChipsSelected
                } else {
                    chipsFilterPenaltyUiModel.isSelected = false
                }
            }
            val chipsUiModelList = penaltyFilterUiModel.find { it.title == titleFilter }?.chipsFilerList
                    ?: listOf()
            _updateFilterSelected.value = Success(Pair(chipsUiModelList, titleFilter))
        }, onError = {
            _updateFilterSelected.value = Fail(it)
        })
    }

    fun updateFilterManySelected(titleFilter: String, chipType: String, chipTitle: String) {
        launchCatchError(block = {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            penaltyFilterUiModel.find { it.title == titleFilter }
                    ?.chipsFilerList?.find { it.title == chipTitle }?.isSelected = !updateChipsSelected
            val chipsUiModelList = penaltyFilterUiModel.find { it.title == titleFilter }?.chipsFilerList
                    ?: listOf()
            _updateFilterSelected.value = Success(Pair(chipsUiModelList, titleFilter))
        }, onError = {
            _updateFilterSelected.value = Fail(it)
        })
    }

    fun resetFilterSelected() {
        launchCatchError(block = {
            penaltyFilterUiModel.map { penaltyFilterUiModel ->
                penaltyFilterUiModel.chipsFilerList.map {
                    it.isSelected = false
                }
            }
            _resetFilterResult.value = Success(penaltyFilterUiModel)
        }, onError = {
            _resetFilterResult.value = Fail(it)
        })
    }
}