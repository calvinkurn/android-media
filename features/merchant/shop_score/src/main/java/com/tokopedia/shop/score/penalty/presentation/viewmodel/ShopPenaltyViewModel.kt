package com.tokopedia.shop.score.penalty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ItemDetailPenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopPenaltyViewModel @Inject constructor(
        coroutineDispatchers: CoroutineDispatchers,
        private val penaltyMapper: PenaltyMapper
) : BaseViewModel(coroutineDispatchers.main) {

    private val _penaltyPageData = MutableLiveData<Result<List<BasePenaltyPage>>>()
    val penaltyPageData: LiveData<Result<List<BasePenaltyPage>>>
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

    private val penaltyFilterUiModel = mutableListOf<PenaltyFilterUiModel>()
    private var itemSortFilterWrapperList = mutableListOf<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>()

    fun getPenaltyFilterUiModelList() = penaltyFilterUiModel

    fun getSortFilterWrapperList() = itemSortFilterWrapperList

    fun getDataDummyPenalty() {
        launch {
            val penaltyDummyData = penaltyMapper.mapToPenaltyVisitableDummy()
            _penaltyPageData.value = Success(penaltyDummyData)
        }
    }

    fun getFilterPenalty() {
        launchCatchError(block = {
            if (penaltyFilterUiModel.isNullOrEmpty()) {
                penaltyFilterUiModel.addAll(penaltyMapper.mapToPenaltyFilterBottomSheet())
            }
            itemSortFilterWrapperList.forEachIndexed { index, itemSortFilterWrapper ->
                penaltyFilterUiModel.find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }
                        ?.chipsFilerList?.getOrNull(index)?.apply {
                            isSelected = itemSortFilterWrapper.isSelected
                            title = itemSortFilterWrapper.sortFilterItem?.title?.toString().orEmpty()
                        }
            }
            _filterPenaltyData.value = Success(penaltyFilterUiModel)
        }, onError = {
            _filterPenaltyData.value = Fail(it)
        })
    }

    fun updateSortFilterSelected(titleFilter: String, chipType: String) {
        launchCatchError(block = {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            itemSortFilterWrapperList.find { it.sortFilterItem?.title == titleFilter }?.isSelected = !updateChipsSelected
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