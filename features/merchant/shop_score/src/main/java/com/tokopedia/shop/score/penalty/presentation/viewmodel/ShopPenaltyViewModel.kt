package com.tokopedia.shop.score.penalty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.common.*
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_LATEST
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_OLDEST
import com.tokopedia.shop.score.common.ShopScoreConstant.TITLE_TYPE_PENALTY
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailParam
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.penalty.presentation.model.*
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class ShopPenaltyViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getShopPenaltyDetailMergeUseCase: Lazy<GetShopPenaltyDetailMergeUseCase>,
    private val getShopPenaltyDetailUseCase: Lazy<GetShopPenaltyDetailUseCase>,
    private val penaltyMapper: PenaltyMapper
) : BaseViewModel(dispatchers.main) {

    private val _penaltyPageData = MutableLiveData<Result<PenaltyDataWrapper>>()
    val penaltyPageData: LiveData<Result<PenaltyDataWrapper>>
        get() = _penaltyPageData

    private val _filterPenaltyData = MutableLiveData<Result<List<PenaltyFilterUiModel>>>()
    val filterPenaltyData: LiveData<Result<List<PenaltyFilterUiModel>>>
        get() = _filterPenaltyData

    val shopPenaltyDetailMediator =
        MediatorLiveData<Result<Triple<List<ItemPenaltyUiModel>, Boolean, Boolean>>>()
    val shopPenaltyDetailData: LiveData<Result<Triple<List<ItemPenaltyUiModel>, Boolean, Boolean>>>
        get() = shopPenaltyDetailMediator

    private val _updateSortFilterSelected =
        MutableLiveData<Result<List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>>>()
    val updateSortSelectedPeriod: LiveData<Result<List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>>>
        get() = _updateSortFilterSelected

    private val _resetFilterResult = MutableLiveData<Result<List<PenaltyFilterUiModel>>>()
    val resetFilterResult: LiveData<Result<List<PenaltyFilterUiModel>>> = _resetFilterResult

    private val _updateFilterSelected =
        MutableLiveData<Result<Pair<List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>, String>>>()
    val updateFilterSelected: LiveData<Result<Pair<List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>, String>>> =
        _updateFilterSelected

    private var penaltyFilterUiModel = mutableListOf<PenaltyFilterUiModel>()
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
        penaltyFilterList: List<PenaltyFilterUiModel>,
        sortFilterItemPeriodList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>
    ) {
        this.itemSortFilterWrapperList = sortFilterItemPeriodList.toMutableList()
        this.penaltyFilterUiModel = penaltyFilterList.toMutableList()
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

    fun getPenaltyFilterUiModelList() = penaltyFilterUiModel
    fun getStartDate() = startDate
    fun getEndDate() = endDate

    fun getDataPenalty() {
        launchCatchError(block = {
            val penaltyDetailMerge = withContext(dispatchers.io) {
                getShopPenaltyDetailMergeUseCase.get().setParams(
                    startDate = startDateSummary,
                    endDate = endDateSummary,
                    typeId = typeId,
                    sort = sortBy
                )
                getShopPenaltyDetailMergeUseCase.get().executeOnBackground()
            }

            penaltyFilterUiModel = penaltyDetailMerge.penaltyFilterList.toMutableList()

            itemSortFilterWrapperList =
                penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterUiModel)
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
                getShopPenaltyDetailUseCase.get().params = GetShopPenaltyDetailUseCase.crateParams(
                    ShopScorePenaltyDetailParam(
                        page = page,
                        startDate = startDate,
                        endDate = endDate,
                        typeID = typeId,
                        sort = sortBy
                    )
                )
                penaltyMapper.mapToItemPenaltyList(
                    getShopPenaltyDetailUseCase.get().executeOnBackground()
                )
            }
            shopPenaltyDetailMediator.value = Success(penaltyDetail)
        }, onError = {
            shopPenaltyDetailMediator.value = Fail(it)
        })
    }

    fun getFilterPenalty(filterPenaltyList: List<PenaltyFilterUiModel>) {
        launch {
            penaltyFilterUiModel = filterPenaltyList.toMutableList()
            _filterPenaltyData.value = Success(penaltyFilterUiModel)
        }
    }

    fun updateSortFilterSelected(titleFilter: String, chipType: String) {
        launch {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            val mapTransformUpdateSortFilterSelected =
                penaltyMapper.transformUpdateSortFilterSelected(
                    penaltyFilterUiModel,
                    itemSortFilterWrapperList,
                    titleFilter,
                    updateChipsSelected
                )
            penaltyFilterUiModel = mapTransformUpdateSortFilterSelected.first
            itemSortFilterWrapperList = mapTransformUpdateSortFilterSelected.second
            _updateSortFilterSelected.value = Success(itemSortFilterWrapperList)
        }
    }

    fun updateFilterSelected(titleFilter: String, chipType: String, position: Int) {
        launch {
            val updateChipsSelected = chipType == ChipsUnify.TYPE_SELECTED
            val mapTransformUpdateFilterSelected = penaltyMapper.transformUpdateFilterSelected(
                titleFilter,
                updateChipsSelected,
                position,
                penaltyFilterUiModel,
                itemSortFilterWrapperList
            )

            penaltyFilterUiModel = mapTransformUpdateFilterSelected.first
            itemSortFilterWrapperList = mapTransformUpdateFilterSelected.second
            val chipsUiModelList = penaltyMapper.getChipsFilterList(titleFilter, penaltyFilterUiModel)
            _updateFilterSelected.value = Success(Pair(chipsUiModelList, titleFilter))
        }
    }

    fun resetFilterSelected() {
        launch {
            penaltyFilterUiModel.map { penaltyFilterUiModel ->
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
            _resetFilterResult.value = Success(penaltyFilterUiModel)
        }
    }
}