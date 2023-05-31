package com.tokopedia.shop.score.penalty.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltySummary
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyTypes
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyTickerUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyDataWrapper
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import javax.inject.Inject
import kotlin.math.abs

class PenaltyMapper @Inject constructor(@ApplicationContext val context: Context?) {

    private val MAX_SHOWN_FILTER_CHIPS = 5

    fun mapToPenaltyDetail(itemPenaltyUiModel: ItemPenaltyUiModel): ShopPenaltyDetailUiModel {
        return ShopPenaltyDetailUiModel(
            titleDetail = itemPenaltyUiModel.typePenalty,
            descStatusPenalty = itemPenaltyUiModel.descStatusPenalty,
            startDateDetail = itemPenaltyUiModel.startDate,
            summaryDetail = itemPenaltyUiModel.reasonPenalty,
            deductionPointPenalty = itemPenaltyUiModel.deductionPoint,
            endDateDetail = itemPenaltyUiModel.endDateDetail,
            prefixDateDetail = itemPenaltyUiModel.prefixDatePenalty,
            stepperPenaltyDetailList = mapToStepperPenaltyDetail(itemPenaltyUiModel.statusPenalty)
        )
    }

    private fun mapToStepperPenaltyDetail(statusPenalty: String): List<ShopPenaltyDetailUiModel.StepperPenaltyDetail> {
        return mutableListOf<ShopPenaltyDetailUiModel.StepperPenaltyDetail>().apply {
            when (statusPenalty) {
                ShopScoreConstant.POINTS_NOT_YET_DEDUCTED -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            titleStepper = R.string.title_point_have_not_been_deducted,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN900,
                            isBold = true,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            titleStepper = R.string.title_on_going,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            titleStepper = R.string.title_penalty_over,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                        )
                    )
                }
                ShopScoreConstant.ON_GOING -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            titleStepper = R.string.title_point_have_not_been_deducted,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            titleStepper = R.string.title_on_going,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN900,
                            isBold = true,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            titleStepper = R.string.title_penalty_over,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                        )
                    )
                }
                ShopScoreConstant.PENALTY_DONE -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            titleStepper = R.string.title_point_have_not_been_deducted,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN900,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            titleStepper = R.string.title_on_going,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN900,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            titleStepper = R.string.title_penalty_over,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN900,
                            isBold = true
                        )
                    )
                }
            }
        }
    }

    fun mapToPenaltyData(
        @ShopPenaltyPageType pageType: String,
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        notYetDeductedPenalties: List<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result>?,
        sortBy: Int,
        typeId: Int,
        dateFilter: Pair<String, String>
    ): PenaltyDataWrapper {
        val penaltyTypes =
            shopScorePenaltySummaryWrapper.shopScorePenaltyTypesResponse?.result ?: emptyList()
        return PenaltyDataWrapper(
            penaltyVisitableList = mapToItemVisitablePenaltyList(
                pageType, shopScorePenaltySummaryWrapper, shopScorePenaltyDetailResponse,
                notYetDeductedPenalties, dateFilter, typeId
            ),
            penaltyFilterList = mapToPenaltyFilterBottomSheet(
                shopScorePenaltyDetailResponse,
                penaltyTypes,
                sortBy,
                typeId
            )
        )
    }

    private fun mapToCardShopPenalty(
        penaltySummary: ShopScorePenaltySummary.Result,
        dateFilter: Pair<String, String>
    ): ItemPenaltyPointCardUiModel {
        return ItemPenaltyPointCardUiModel(
            result = penaltySummary,
            date = getFormattedDate(dateFilter)
        )
    }

    private fun mapToNotYetDeductedItem(list: List<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result>): ItemPenaltyInfoNotificationUiModel {
        // TODO: check for cache for shouldShowDot
        return ItemPenaltyInfoNotificationUiModel(
            notificationCount = list.size,
            shouldShowDot = list.size > Int.ZERO
        )
    }

    private fun mapToDetailPenaltyFilter(
        @ShopPenaltyPageType pageType: String,
        dateFilter: Pair<String, String>
    ): ItemPenaltySubsectionUiModel {
        return ItemPenaltySubsectionUiModel(
            date = getFormattedDate(dateFilter),
            pageType = pageType
        )
    }

    private fun getFormattedDate(dateFilter: Pair<String, String>): String {
        val startDate = DateFormatUtils.formatDate(
            ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
            ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
            dateFilter.first
        )
        val endDate = DateFormatUtils.formatDate(
            ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
            ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
            dateFilter.second
        )
        return "$startDate - $endDate"
    }

    private fun mapToSortFilterPenalty(
        penaltyTypes: List<ShopScorePenaltyTypes.Result>,
        typeId: Int
    ): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        return mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>().apply {
            penaltyTypes.map {
                add(
                    ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper(
                        title = it.name,
                        isSelected = it.id == typeId.toString(),
                        idFilter = it.id.toIntOrZero()
                    )
                )
            }
        }
    }

    fun mapToItemPenaltyList(shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail):
        Triple<List<ItemPenaltyUiModel>, Boolean, Boolean> {

        val itemPenaltyList = mutableListOf<ItemPenaltyUiModel>().apply {
            shopScorePenaltyDetailResponse.result.forEach {
                val colorTypePenalty = when (it.status) {
                    ShopScoreConstant.POINTS_NOT_YET_DEDUCTED, ShopScoreConstant.PENALTY_DONE -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_NN500
                    }
                    ShopScoreConstant.ON_GOING -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_RN600
                    }
                    else -> {
                        null
                    }
                }

                val (prefixDatePenaltyDetail, endDateText) = when (it.status) {
                    ShopScoreConstant.ON_GOING -> {
                        Pair(
                            ShopScoreConstant.ACTIVE_PENALTY_DETAIL,
                            "${ShopScoreConstant.FINISHED_IN} ${
                                DateFormatUtils.formatDate(
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyExpirationDate
                                )
                            }"
                        )
                    }
                    ShopScoreConstant.PENALTY_DONE -> {
                        Pair(
                            ShopScoreConstant.SINCE_FINISH_PENALTY_DETAIL,
                            "${ShopScoreConstant.SINCE} ${
                                DateFormatUtils.formatDate(
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyExpirationDate
                                )
                            }"
                        )
                    }
                    ShopScoreConstant.POINTS_NOT_YET_DEDUCTED -> {
                        Pair(
                            ShopScoreConstant.START_ACTIVE_PENALTY_DETAIL,
                            "${ShopScoreConstant.START} ${
                                DateFormatUtils.formatDate(
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                                    ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyStartDate
                                )
                            }"
                        )
                    }
                    else -> Pair("", "")
                }

                val endDateDetail = when (it.status) {
                    ShopScoreConstant.ON_GOING, ShopScoreConstant.PENALTY_DONE -> {
                        DateFormatUtils.formatDate(
                            ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                            ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                            it.penaltyExpirationDate
                        )
                    }
                    ShopScoreConstant.POINTS_NOT_YET_DEDUCTED -> {
                        DateFormatUtils.formatDate(
                            ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                            ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                            it.penaltyStartDate
                        )
                    }
                    else -> ""
                }

                val descStatusPenaltyDetail = when (it.status) {
                    ShopScoreConstant.POINTS_NOT_YET_DEDUCTED -> R.string.desc_point_have_not_been_deducted
                    ShopScoreConstant.ON_GOING -> R.string.desc_on_going_status_penalty
                    ShopScoreConstant.PENALTY_DONE -> R.string.desc_done_status_penalty
                    else -> null
                }

                val scoreAbs = abs(it.score).toString()

                add(
                    ItemPenaltyUiModel(
                        statusPenalty = it.status,
                        deductionPoint = scoreAbs,
                        startDate = DateFormatUtils.formatDate(
                            ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM,
                            ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT,
                            it.createTime
                        ),
                        endDate = endDateText,
                        endDateDetail = endDateDetail,
                        typePenalty = it.typeName,
                        invoicePenalty = it.invoiceNumber,
                        reasonPenalty = it.reason,
                        colorPenalty = colorTypePenalty,
                        prefixDatePenalty = prefixDatePenaltyDetail,
                        descStatusPenalty = descStatusPenaltyDetail
                    )
                )
            }
        }

        return Triple(
            itemPenaltyList,
            shopScorePenaltyDetailResponse.hasPrev,
            shopScorePenaltyDetailResponse.hasNext
        )
    }

    private fun mapToItemVisitablePenaltyList(
        @ShopPenaltyPageType pageType: String,
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        notYetDeductedPenalties: List<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result>?,
        dateFilter: Pair<String, String>,
        typeId: Int
    ): Triple<List<BasePenaltyPage>, Boolean, Boolean> {
        val visitablePenaltyPage = mutableListOf<BasePenaltyPage>()

        visitablePenaltyPage.apply {
            if (pageType == ShopPenaltyPageType.ONGOING) {
                add(ItemPenaltyTickerUiModel)
                if (notYetDeductedPenalties != null) {
                    add(mapToNotYetDeductedItem(notYetDeductedPenalties))
                }
            }
            if (pageType != ShopPenaltyPageType.NOT_YET_DEDUCTED) {
                add(mapToDetailPenaltyFilter(pageType, dateFilter))
            }
            if (pageType == ShopPenaltyPageType.ONGOING) {
                shopScorePenaltySummaryWrapper.shopScorePenaltySummaryResponse?.result?.let {
                    add(
                        mapToCardShopPenalty(it, dateFilter)
                    )
                }
            }
            shopScorePenaltySummaryWrapper.shopScorePenaltyTypesResponse?.result?.let {
                add(
                    ItemSortFilterPenaltyUiModel(
                        itemSortFilterWrapperList = mapToSortFilterPenalty(
                            it,
                            typeId
                        )
                    )
                )
            }

            val itemPenaltyFilterList = mapToItemPenaltyList(shopScorePenaltyDetailResponse).first
            addAll(itemPenaltyFilterList)
        }

        return Triple(
            visitablePenaltyPage,
            shopScorePenaltyDetailResponse.hasPrev,
            shopScorePenaltyDetailResponse.hasNext
        )
    }

    private fun mapToPenaltyFilterBottomSheet(
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        penaltyTypes: List<ShopScorePenaltyTypes.Result>,
        sortBy: Int, typeId: Int
    ): List<BaseFilterPenaltyPage> {
        return mutableListOf<BaseFilterPenaltyPage>().apply {
            val sortFilterChipList = mapToChipsSortFilter(sortBy)
            add(
                PenaltyFilterUiModel(
                    title = ShopScoreConstant.TITLE_SORT,
                    isDividerVisible = true,
                    chipsFilterList = sortFilterChipList,
                    shownFilterList = sortFilterChipList
                )
            )

            add(
                PenaltyFilterDateUiModel(
                    startDate = shopScorePenaltyDetailResponse.startDate,
                    endDate = shopScorePenaltyDetailResponse.endDate,
                    defaultStartDate = shopScorePenaltyDetailResponse.defaultStartDate,
                    defaultEndDate = shopScorePenaltyDetailResponse.defaultEndDate,
                    completeDate = "${shopScorePenaltyDetailResponse.startDate} - ${shopScorePenaltyDetailResponse.endDate}"
                )
            )

            val filterTypeChipList = mapToChipsTypePenaltyFilter(penaltyTypes, typeId)
            add(
                PenaltyFilterUiModel(
                    title = ShopScoreConstant.TITLE_TYPE_PENALTY,
                    chipsFilterList = filterTypeChipList,
                    shownFilterList = filterTypeChipList.take(MAX_SHOWN_FILTER_CHIPS)
                )
            )
        }
    }

    fun mapToChipsSortFilter(sortBy: Int): List<ChipsFilterPenaltyUiModel> {
        return mutableListOf<ChipsFilterPenaltyUiModel>().apply {
            add(
                ChipsFilterPenaltyUiModel(
                    title = ShopScoreConstant.SORT_LATEST,
                    isSelected = sortBy == ShopScoreConstant.SORT_LATEST_VALUE,
                    value = ShopScoreConstant.SORT_LATEST_VALUE
                )
            )
            add(
                ChipsFilterPenaltyUiModel(
                    title = ShopScoreConstant.SORT_OLDEST,
                    isSelected = sortBy == ShopScoreConstant.SORT_OLDEST_VALUE,
                    value = ShopScoreConstant.SORT_OLDEST_VALUE
                )
            )
        }
    }

    private fun mapToChipsTypePenaltyFilter(
        penaltyTypes: List<ShopScorePenaltyTypes.Result>,
        typeId: Int
    ): List<ChipsFilterPenaltyUiModel> {
        return mutableListOf<ChipsFilterPenaltyUiModel>().apply {
            penaltyTypes.map {
                add(
                    ChipsFilterPenaltyUiModel(
                        it.name,
                        isSelected = it.id == typeId.toString(),
                        value = it.id.toIntOrZero()
                    )
                )
            }
        }
    }

    fun mapToSortFilterItemFromPenaltyList(penaltyFilterList: List<BaseFilterPenaltyPage>): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        val mapItemSortFilterWrapper =
            mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()
        penaltyFilterList.filterIsInstance<PenaltyFilterUiModel>().find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.chipsFilterList?.map {
            mapItemSortFilterWrapper.add(
                ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper(
                    title = it.title,
                    isSelected = it.isSelected,
                    idFilter = it.value
                )
            )
        }
        return mapItemSortFilterWrapper
    }

    fun transformUpdateFilterSelected(
        titleFilter: String,
        updateChipsSelected: Boolean,
        position: Int,
        penaltyFilterUiModel: MutableList<BaseFilterPenaltyPage>,
        itemSortFilterWrapperList: MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>
    ): Pair<MutableList<BaseFilterPenaltyPage>, MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>> {
        penaltyFilterUiModel.filterIsInstance<PenaltyFilterUiModel>().find { it.title == titleFilter }?.run {
            chipsFilterList.mapIndexed { index, chipsFilterPenaltyUiModel ->
                if (index == position) {
                    chipsFilterPenaltyUiModel.isSelected = !updateChipsSelected
                    itemSortFilterWrapperList.getOrNull(index)?.isSelected = !updateChipsSelected
                } else {
                    chipsFilterPenaltyUiModel.isSelected = false
                    itemSortFilterWrapperList.getOrNull(index)?.isSelected = false
                }
            }
            val selectedFilterList = chipsFilterList.filter { it.isSelected }
            val selectedFilterSize = selectedFilterList.size
            shownFilterList =
                when {
                    chipsFilterList.size < MAX_SHOWN_FILTER_CHIPS -> chipsFilterList
                    selectedFilterSize < MAX_SHOWN_FILTER_CHIPS -> {
                        val unselectedFilterList = chipsFilterList.filter { !it.isSelected }
                            .take(MAX_SHOWN_FILTER_CHIPS - selectedFilterSize)
                        selectedFilterList + unselectedFilterList
                    }
                    else -> selectedFilterList
                }
        }
        return Pair(penaltyFilterUiModel, itemSortFilterWrapperList)
    }

    fun transformUpdateSortFilterSelected(
        penaltyFilterUiModel: MutableList<BaseFilterPenaltyPage>,
        itemSortFilterWrapperList: MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>,
        titleFilter: String,
        updateChipsSelected: Boolean
    ): Pair<MutableList<BaseFilterPenaltyPage>, MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>> {
        penaltyFilterUiModel.filterIsInstance<PenaltyFilterUiModel>().find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.run {
            chipsFilterList.mapIndexed { index, chipsFilterPenaltyUiModel ->
                if (chipsFilterPenaltyUiModel.title == titleFilter) {
                    chipsFilterPenaltyUiModel.isSelected = !updateChipsSelected
                    itemSortFilterWrapperList.getOrNull(index)?.isSelected = !updateChipsSelected
                } else {
                    chipsFilterPenaltyUiModel.isSelected = false
                    itemSortFilterWrapperList.getOrNull(index)?.isSelected = false
                }
            }
            shownFilterList = chipsFilterList
        }
        return Pair(penaltyFilterUiModel, itemSortFilterWrapperList)
    }

    fun getChipsFilterList(
        titleFilter: String,
        penaltyFilterUiModel: MutableList<BaseFilterPenaltyPage>,
    ): List<ChipsFilterPenaltyUiModel> {
        return penaltyFilterUiModel.filterIsInstance<PenaltyFilterUiModel>()
            .find { it.title == titleFilter }?.chipsFilterList ?: emptyList()
    }
}
