package com.tokopedia.shop.score.penalty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.format
import com.tokopedia.shop.score.common.getNowTimeStamp
import com.tokopedia.shop.score.common.getPastDaysPenaltyTimeStamp
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailParam
import com.tokopedia.shop.score.penalty.domain.usecase.GetNotYetDeductedPenaltyUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyDataWrapper
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopPenaltyViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getShopPenaltyDetailMergeUseCase: Lazy<GetShopPenaltyDetailMergeUseCase>,
    private val getShopPenaltyDetailUseCase: Lazy<GetShopPenaltyDetailUseCase>,
    private val getNotYetDeductedPenaltyUseCase: Lazy<GetNotYetDeductedPenaltyUseCase>,
    private val penaltyMapper: PenaltyMapper
) : BaseViewModel(dispatchers.main) {

    private val _penaltyPageData = MutableLiveData<Result<PenaltyDataWrapper>>()
    val penaltyPageData: LiveData<Result<PenaltyDataWrapper>>
        get() = _penaltyPageData

    private val _filterPenaltyData = MutableLiveData<Result<List<BaseFilterPenaltyPage>>>()
    val filterPenaltyData: LiveData<Result<List<BaseFilterPenaltyPage>>>
        get() = _filterPenaltyData

    val shopPenaltyDetailMediator =
        MediatorLiveData<Result<Triple<List<ItemPenaltyUiModel>, Boolean, Boolean>>>()
    val shopPenaltyDetailData: LiveData<Result<Triple<List<ItemPenaltyUiModel>, Boolean, Boolean>>>
        get() = shopPenaltyDetailMediator

    private val _updateSortFilterSelected =
        MutableLiveData<Result<List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>>>()
    val updateSortSelectedPeriod: LiveData<Result<List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>>>
        get() = _updateSortFilterSelected

    private val _resetFilterResult = MutableLiveData<Result<List<BaseFilterPenaltyPage>>>()
    val resetFilterResult: LiveData<Result<List<BaseFilterPenaltyPage>>> = _resetFilterResult

    private val _updateFilterSelected =
        MutableLiveData<Result<Pair<List<ChipsFilterPenaltyUiModel>, String>>>()
    val updateFilterSelected: LiveData<Result<Pair<List<ChipsFilterPenaltyUiModel>, String>>> =
        _updateFilterSelected

    private var penaltyFilterUiModel = mutableListOf<BaseFilterPenaltyPage>()
    private var itemSortFilterWrapperList =
        mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()

    private val _typeFilterData = MutableLiveData<Int>()
    private val _sortTypeFilterData = MutableLiveData<Pair<Int, Int>>()
    private val _dateFilterData = MutableLiveData<Pair<String, String>>()

    private var startDate =
        format(getPastDaysPenaltyTimeStamp().time, ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
    private var endDate = format(getNowTimeStamp(), ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
    private var maxStartDate: String? = null
    private var maxEndDate: String? = null
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
        penaltyFilterList: List<BaseFilterPenaltyPage>,
        sortFilterItemPeriodList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>
    ) {
        this.itemSortFilterWrapperList = sortFilterItemPeriodList.toMutableList()
        this.penaltyFilterUiModel = penaltyFilterList.toMutableList()
    }

    fun setDateFilterData(startDate: String, endDate: String, completeDate: String) {
        this.startDate = startDate
        this.endDate = endDate

        penaltyFilterUiModel =
            penaltyMapper.transformDateFilter(penaltyFilterUiModel, startDate, endDate, completeDate)
    }

    fun setDateFilterData(dateFilter: Pair<String, String>) {
        _dateFilterData.value = Pair(dateFilter.first, dateFilter.second)
    }

    fun setMaxDateFilterData(maxDateFilter: Pair<String, String>) {
        maxStartDate = maxDateFilter.first
        maxEndDate = maxDateFilter.second
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
    fun getMaxStartDate() = maxStartDate
    fun getMaxEndDate() = maxEndDate

    fun getDataPenalty(@ShopPenaltyPageType pageType: String = ShopPenaltyPageType.ONGOING) {
        launchCatchError(block = {
            val penaltyDetailMergeDeffered =
                withContext(dispatchers.io) {
                    async {
                        getShopPenaltyDetailMergeUseCase.get().setParams(
                            startDate = startDateSummary,
                            endDate = endDateSummary,
                            typeId = typeId,
                            sort = sortBy,
                            status = getStatusFromPageType(pageType)
                        )
                        getShopPenaltyDetailMergeUseCase.get().executeOnBackground()
                    }
                }


            val notYetDeductedPenaltyDeferred =
                withContext(dispatchers.io) {
                    async {
                        if (pageType == ShopPenaltyPageType.ONGOING) {
                            getNotYetDeductedPenaltyUseCase.get()
                                .execute(startDateSummary, endDateSummary)
                        } else {
                            null
                        }
                    }
                }

            val penaltyDetailMerge =
                penaltyDetailMergeDeffered.await()
            val notYetDeductedPenalty = notYetDeductedPenaltyDeferred.await()

            val penaltyDataWrapper = penaltyMapper.mapToPenaltyData(
                pageType,
                penaltyDetailMerge.first,
                penaltyDetailMerge.second,
                notYetDeductedPenalty,
                sortBy,
                typeId
            )

            startDate = penaltyDetailMerge.second.startDate
            endDate = penaltyDetailMerge.second.endDate
            maxStartDate = penaltyDetailMerge.second.defaultStartDate
            maxEndDate = penaltyDetailMerge.second.defaultEndDate

            penaltyFilterUiModel = penaltyDataWrapper.penaltyFilterList.toMutableList()

            itemSortFilterWrapperList =
                penaltyMapper.mapToSortFilterItemFromPenaltyList(penaltyFilterUiModel)
                    .toMutableList()
            _penaltyPageData.value = Success(penaltyDataWrapper)
        }, onError = {
            _penaltyPageData.value = Fail(it)
        })
    }

    private fun getStatusFromPageType(status: String): Int {
        return when(status) {
            ShopPenaltyPageType.NOT_YET_DEDUCTED -> ShopScoreConstant.STATUS_NOT_YET_DEDUCTED
            ShopPenaltyPageType.HISTORY -> ShopScoreConstant.STATUS_DONE
            else -> ShopScoreConstant.STATUS_ONGOING
        }
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

    fun getFilterPenalty(filterPenaltyList: List<BaseFilterPenaltyPage>) {
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
                if (penaltyFilterUiModel is PenaltyFilterUiModel) {
                    if (penaltyFilterUiModel.title == ShopScoreConstant.TITLE_SORT) {
                        penaltyFilterUiModel.chipsFilterList.map {
                            it.isSelected = it.title == ShopScoreConstant.SORT_LATEST
                        }
                    } else {
                        penaltyFilterUiModel.chipsFilterList.map {
                            it.isSelected = false
                        }
                    }
                }
            }
            // TODO: Reset date filter
            _resetFilterResult.value = Success(penaltyFilterUiModel)
        }
    }
}

