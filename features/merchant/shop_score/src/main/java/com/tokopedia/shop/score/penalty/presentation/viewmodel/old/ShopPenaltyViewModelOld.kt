package com.tokopedia.shop.score.penalty.presentation.viewmodel.old

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_LATEST
import com.tokopedia.shop.score.common.format
import com.tokopedia.shop.score.common.getNowTimeStamp
import com.tokopedia.shop.score.common.getPastDaysPenaltyTimeStamp
import com.tokopedia.shop.score.penalty.domain.old.mapper.PenaltyMapperOld
import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailMergeUseCaseOld
import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailUseCaseOld
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailParam
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyDataWrapperOld
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyFilterUiModelOld
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class ShopPenaltyViewModelOld @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getShopPenaltyDetailMergeUseCaseOld: Lazy<GetShopPenaltyDetailMergeUseCaseOld>,
    private val getShopPenaltyDetailUseCaseOld: Lazy<GetShopPenaltyDetailUseCaseOld>,
    private val penaltyMapperOld: PenaltyMapperOld
) : BaseViewModel(dispatchers.main) {

    private val _penaltyPageData = MutableLiveData<Result<PenaltyDataWrapperOld>>()
    val penaltyPageData: LiveData<Result<PenaltyDataWrapperOld>>
        get() = _penaltyPageData

    private val _filterPenaltyData = MutableLiveData<Result<List<PenaltyFilterUiModelOld>>>()
    val filterPenaltyData: LiveData<Result<List<PenaltyFilterUiModelOld>>>
        get() = _filterPenaltyData

    val shopPenaltyDetailMediator =
        MediatorLiveData<Result<Triple<List<ItemPenaltyUiModel>, Boolean, Boolean>>>()
    val shopPenaltyDetailData: LiveData<Result<Triple<List<ItemPenaltyUiModel>, Boolean, Boolean>>>
        get() = shopPenaltyDetailMediator

    private val _updateSortFilterSelected =
        MutableLiveData<Result<List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>>>()
    val updateSortSelectedPeriod: LiveData<Result<List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>>>
        get() = _updateSortFilterSelected

    private val _resetFilterResult = MutableLiveData<Result<List<PenaltyFilterUiModelOld>>>()
    val resetFilterResult: LiveData<Result<List<PenaltyFilterUiModelOld>>> = _resetFilterResult

    private val _updateFilterSelected =
        MutableLiveData<Result<Pair<List<ChipsFilterPenaltyUiModel>, String>>>()
    val updateFilterSelected: LiveData<Result<Pair<List<ChipsFilterPenaltyUiModel>, String>>> =
        _updateFilterSelected

    private var penaltyFilterUiModelOld = mutableListOf<PenaltyFilterUiModelOld>()
    private var itemSortFilterWrapperList =
        mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()

    private val _typeFilterData = MutableLiveData<Int>()
    private val _sortTypeFilterData = MutableLiveData<Pair<Int, Int>>()
    private val _dateFilterData = MutableLiveData<Pair<String, String>>()

    private var startDate =
        format(getPastDaysPenaltyTimeStamp().time, ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
    private var endDate = format(getNowTimeStamp(), ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
    private var startDateSummary =
        format(getPastDaysPenaltyTimeStamp().time, ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
    private var endDateSummary =
        format(getNowTimeStamp(), ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
    private var typeId = 0
    private var sortBy = 0

    init {
        initPenaltyDetail()
    }

    fun setItemSortFilterWrapperList(
        penaltyFilterList: List<PenaltyFilterUiModelOld>,
        sortFilterItemPeriodList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>
    ) {
        this.itemSortFilterWrapperList = sortFilterItemPeriodList.toMutableList()
        this.penaltyFilterUiModelOld = penaltyFilterList.toMutableList()
    }

    fun setDateFilterData(dateFilter: Pair<String, String>) {
        _dateFilterData.value = Pair(dateFilter.first, dateFilter.second)
    }

    fun setTypeFilterData(typeId: Int) {
        _typeFilterData.value = typeId
    }

    fun setSortTypeFilterData(sortTypeFilter: Pair<Int, Int>) {
        _sortTypeFilterData.value = sortTypeFilter
    }

    fun getPenaltyFilterUiModelList() = penaltyFilterUiModelOld
    fun getStartDate() = startDate
    fun getEndDate() = endDate

    fun getDataPenalty() {
        launchCatchError(block = {
            val penaltyDetailMerge = withContext(dispatchers.io) {
                getShopPenaltyDetailMergeUseCaseOld.get().setParams(
                    startDate = startDateSummary,
                    endDate = endDateSummary,
                    typeId = typeId,
                    sort = sortBy
                )
                getShopPenaltyDetailMergeUseCaseOld.get().executeOnBackground()
            }

            penaltyFilterUiModelOld = penaltyDetailMerge.penaltyFilterList.toMutableList()

            itemSortFilterWrapperList =
                penaltyMapperOld.mapToSortFilterItemFromPenaltyList(penaltyFilterUiModelOld)
                    .toMutableList()
            _penaltyPageData.value = Success(penaltyDetailMerge)
        }, onError = {
            _penaltyPageData.value = Fail(it)
        })
    }

    private fun initPenaltyDetail() {
        shopPenaltyDetailMediator.addSource(_sortTypeFilterData) {
            sortBy = it.first
            typeId = it.second
            getPenaltyDetailListNext()
        }

        shopPenaltyDetailMediator.addSource(_typeFilterData) {
            typeId = it
            getPenaltyDetailListNext()
        }

        shopPenaltyDetailMediator.addSource(_dateFilterData) {
            startDate = it.first
            endDate = it.second
            getPenaltyDetailListNext()
        }
    }

    fun getPenaltyDetailListNext(page: Int = 1) {
        launchCatchError(block = {
            val penaltyDetail = withContext(dispatchers.io) {
                getShopPenaltyDetailUseCaseOld.get().params = GetShopPenaltyDetailUseCaseOld.crateParams(
                    ShopScorePenaltyDetailParam(
                        page = page,
                        startDate = startDate,
                        endDate = endDate,
                        typeID = typeId,
                        sort = sortBy
                    )
                )
                penaltyMapperOld.mapToItemPenaltyList(
                    getShopPenaltyDetailUseCaseOld.get().executeOnBackground()
                )
            }
            shopPenaltyDetailMediator.value = Success(penaltyDetail)
        }, onError = {
            shopPenaltyDetailMediator.value = Fail(it)
        })
    }

    fun getFilterPenalty(filterPenaltyList: List<PenaltyFilterUiModelOld>) {
        launch {
            penaltyFilterUiModelOld = filterPenaltyList.toMutableList()
            _filterPenaltyData.value = Success(penaltyFilterUiModelOld)
        }
    }

    fun updateSortFilterSelected(titleFilter: String, chipType: String) {
        launch {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            val mapTransformUpdateSortFilterSelected =
                penaltyMapperOld.transformUpdateSortFilterSelected(
                    penaltyFilterUiModelOld,
                    itemSortFilterWrapperList,
                    titleFilter,
                    updateChipsSelected
                )
            penaltyFilterUiModelOld = mapTransformUpdateSortFilterSelected.first
            itemSortFilterWrapperList = mapTransformUpdateSortFilterSelected.second
            _updateSortFilterSelected.value = Success(itemSortFilterWrapperList)
        }
    }

    fun updateFilterSelected(titleFilter: String, chipType: String, position: Int) {
        launch {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            val mapTransformUpdateFilterSelected = penaltyMapperOld.transformUpdateFilterSelected(
                titleFilter,
                updateChipsSelected,
                position,
                penaltyFilterUiModelOld,
                itemSortFilterWrapperList
            )

            penaltyFilterUiModelOld = mapTransformUpdateFilterSelected.first
            itemSortFilterWrapperList = mapTransformUpdateFilterSelected.second
            val chipsUiModelList = penaltyMapperOld.getChipsFilterList(titleFilter, penaltyFilterUiModelOld)
            _updateFilterSelected.value = Success(Pair(chipsUiModelList, titleFilter))
        }
    }

    fun resetFilterSelected() {
        launch {
            penaltyFilterUiModelOld.map { penaltyFilterUiModel ->
                if (penaltyFilterUiModel.title == ShopScoreConstant.TITLE_SORT) {
                    penaltyFilterUiModel.chipsFilterList.map {
                        it.isSelected = it.title == SORT_LATEST
                    }
                } else {
                    penaltyFilterUiModel.chipsFilterList.map {
                        it.isSelected = false
                    }
                }
            }
            _resetFilterResult.value = Success(penaltyFilterUiModelOld)
        }
    }
}
