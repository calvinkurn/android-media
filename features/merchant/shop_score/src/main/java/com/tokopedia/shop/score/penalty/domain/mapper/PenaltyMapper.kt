package com.tokopedia.shop.score.penalty.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.ACTIVE_PENALTY_DETAIL
import com.tokopedia.shop.score.common.ShopScoreConstant.FINISHED_IN
import com.tokopedia.shop.score.common.ShopScoreConstant.ON_GOING
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_PENALTY_DATE_TEXT
import com.tokopedia.shop.score.common.ShopScoreConstant.PENALTY_DONE
import com.tokopedia.shop.score.common.ShopScoreConstant.POINTS_NOT_YET_DEDUCTED
import com.tokopedia.shop.score.common.ShopScoreConstant.SINCE
import com.tokopedia.shop.score.common.ShopScoreConstant.SINCE_FINISH_PENALTY_DETAIL
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_LATEST
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_LATEST_VALUE
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_OLDEST
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_OLDEST_VALUE
import com.tokopedia.shop.score.common.ShopScoreConstant.START
import com.tokopedia.shop.score.common.ShopScoreConstant.START_ACTIVE_PENALTY_DETAIL
import com.tokopedia.shop.score.common.ShopScoreConstant.TITLE_SORT
import com.tokopedia.shop.score.common.ShopScoreConstant.TITLE_TYPE_PENALTY
import com.tokopedia.shop.score.penalty.domain.response.*
import com.tokopedia.shop.score.penalty.presentation.model.*
import javax.inject.Inject
import kotlin.math.abs

open class PenaltyMapper @Inject constructor(@ApplicationContext val context: Context?) {

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
                POINTS_NOT_YET_DEDUCTED -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = R.string.title_point_have_not_been_deducted,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isBold = true,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = R.string.title_on_going,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_68,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = R.string.title_penalty_over,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_68,
                        )
                    )
                }
                ON_GOING -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            titleStepper = R.string.title_point_have_not_been_deducted,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_68,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = R.string.title_on_going,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isBold = true,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = R.string.title_penalty_over,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                        )
                    )
                }
                PENALTY_DONE -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            titleStepper = R.string.title_point_have_not_been_deducted,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            titleStepper = R.string.title_on_going,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isDividerShow = true
                        )
                    )
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            titleStepper = R.string.title_penalty_over,
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isBold = true
                        )
                    )
                }
            }
        }
    }

    fun mapToPenaltyData(
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        sortBy: Int,
        typeId: Int,
        dateFilter: Pair<String, String>
    ): PenaltyDataWrapper {
        val penaltyTypes =
            shopScorePenaltySummaryWrapper.shopScorePenaltyTypesResponse?.result ?: emptyList()
        return PenaltyDataWrapper(
            penaltyVisitableList = mapToItemVisitablePenaltyList(
                shopScorePenaltySummaryWrapper, shopScorePenaltyDetailResponse,
                dateFilter, typeId
            ),
            penaltyFilterList = mapToPenaltyFilterBottomSheet(penaltyTypes, sortBy, typeId)
        )
    }

    private fun mapToCardShopPenalty(penaltySummary: ShopScorePenaltySummary.Result): ItemCardShopPenaltyUiModel {
        return ItemCardShopPenaltyUiModel(
            totalPenalty = penaltySummary.penaltyAmount,
            hasPenalty = penaltySummary.penaltyAmount.isMoreThanZero(),
            deductionPoints = penaltySummary.penalty
        )
    }

    private fun mapToDetailPenaltyFilter(dateFilter: Pair<String, String>): ItemPeriodDetailPenaltyUiModel {
        val startDate = DateFormatUtils.formatDate(
            PATTERN_PENALTY_DATE_PARAM,
            PATTERN_PENALTY_DATE_TEXT,
            dateFilter.first
        )
        val endDate = DateFormatUtils.formatDate(
            PATTERN_PENALTY_DATE_PARAM,
            PATTERN_PENALTY_DATE_TEXT,
            dateFilter.second
        )
        return ItemPeriodDetailPenaltyUiModel(periodDetail = "$startDate - $endDate")
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
                    POINTS_NOT_YET_DEDUCTED, PENALTY_DONE -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_NN500
                    }
                    ON_GOING -> {
                        com.tokopedia.unifyprinciples.R.color.Unify_RN600
                    }
                    else -> {
                        null
                    }
                }

                val (prefixDatePenaltyDetail, endDateText) = when (it.status) {
                    ON_GOING -> {
                        Pair(
                            ACTIVE_PENALTY_DETAIL,
                            "$FINISHED_IN ${
                                DateFormatUtils.formatDate(
                                    PATTERN_PENALTY_DATE_PARAM,
                                    PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyExpirationDate
                                )
                            }"
                        )
                    }
                    PENALTY_DONE -> {
                        Pair(
                            SINCE_FINISH_PENALTY_DETAIL,
                            "$SINCE ${
                                DateFormatUtils.formatDate(
                                    PATTERN_PENALTY_DATE_PARAM,
                                    PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyExpirationDate
                                )
                            }"
                        )
                    }
                    POINTS_NOT_YET_DEDUCTED -> {
                        Pair(
                            START_ACTIVE_PENALTY_DETAIL,
                            "$START ${
                                DateFormatUtils.formatDate(
                                    PATTERN_PENALTY_DATE_PARAM,
                                    PATTERN_PENALTY_DATE_TEXT,
                                    it.penaltyStartDate
                                )
                            }"
                        )
                    }
                    else -> Pair("", "")
                }

                val endDateDetail = when (it.status) {
                    ON_GOING, PENALTY_DONE -> {
                        DateFormatUtils.formatDate(
                            PATTERN_PENALTY_DATE_PARAM,
                            PATTERN_PENALTY_DATE_TEXT,
                            it.penaltyExpirationDate
                        )
                    }
                    POINTS_NOT_YET_DEDUCTED -> {
                        DateFormatUtils.formatDate(
                            PATTERN_PENALTY_DATE_PARAM,
                            PATTERN_PENALTY_DATE_TEXT,
                            it.penaltyStartDate
                        )
                    }
                    else -> ""
                }

                val descStatusPenaltyDetail = when (it.status) {
                    POINTS_NOT_YET_DEDUCTED -> R.string.desc_point_have_not_been_deducted
                    ON_GOING -> R.string.desc_on_going_status_penalty
                    PENALTY_DONE -> R.string.desc_done_status_penalty
                    else -> null
                }

                val scoreAbs = abs(it.score).toString()

                add(
                    ItemPenaltyUiModel(
                        statusPenalty = it.status,
                        deductionPoint = scoreAbs,
                        startDate = DateFormatUtils.formatDate(
                            PATTERN_PENALTY_DATE_PARAM,
                            PATTERN_PENALTY_DATE_TEXT,
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
        shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
        shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
        dateFilter: Pair<String, String>,
        typeId: Int
    ): Triple<List<BasePenaltyPage>, Boolean, Boolean> {
        val visitablePenaltyPage = mutableListOf<BasePenaltyPage>()

        visitablePenaltyPage.apply {
            shopScorePenaltySummaryWrapper.shopScorePenaltySummaryResponse?.result?.let {
                add(
                    mapToCardShopPenalty(it)
                )
            }
            add(mapToDetailPenaltyFilter(dateFilter))
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
        penaltyTypes: List<ShopScorePenaltyTypes.Result>,
        sortBy: Int, typeId: Int
    ): List<PenaltyFilterUiModel> {
        return mutableListOf<PenaltyFilterUiModel>().apply {
            add(
                PenaltyFilterUiModel(
                    title = TITLE_SORT,
                    isDividerVisible = true,
                    chipsFilterList = mapToChipsSortFilter(sortBy)
                )
            )
            add(
                PenaltyFilterUiModel(
                    title = TITLE_TYPE_PENALTY,
                    chipsFilterList = mapToChipsTypePenaltyFilter(penaltyTypes, typeId)
                )
            )
        }
    }

    fun mapToChipsSortFilter(sortBy: Int): List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>().apply {
            add(
                PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(
                    title = SORT_LATEST,
                    isSelected = sortBy == SORT_LATEST_VALUE,
                    value = SORT_LATEST_VALUE
                )
            )
            add(
                PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(
                    title = SORT_OLDEST,
                    isSelected = sortBy == SORT_OLDEST_VALUE,
                    value = SORT_OLDEST_VALUE
                )
            )
        }
    }

    private fun mapToChipsTypePenaltyFilter(
        penaltyTypes: List<ShopScorePenaltyTypes.Result>,
        typeId: Int
    ): List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>().apply {
            penaltyTypes.map {
                add(
                    PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(
                        it.name,
                        isSelected = it.id == typeId.toString(),
                        value = it.id.toIntOrZero()
                    )
                )
            }
        }
    }

    fun mapToSortFilterItemFromPenaltyList(penaltyFilterList: List<PenaltyFilterUiModel>): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        val mapItemSortFilterWrapper =
            mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()
        penaltyFilterList.find { it.title == TITLE_TYPE_PENALTY }?.chipsFilterList?.map {
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
        penaltyFilterUiModel: MutableList<PenaltyFilterUiModel>,
        itemSortFilterWrapperList: MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>
    ): Pair<MutableList<PenaltyFilterUiModel>, MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>> {
        penaltyFilterUiModel.find { it.title == titleFilter }?.chipsFilterList?.mapIndexed { index, chipsFilterPenaltyUiModel ->
            if (index == position) {
                chipsFilterPenaltyUiModel.isSelected = !updateChipsSelected
                itemSortFilterWrapperList.getOrNull(index)?.isSelected = !updateChipsSelected
            } else {
                chipsFilterPenaltyUiModel.isSelected = false
                itemSortFilterWrapperList.getOrNull(index)?.isSelected = false
            }
        }
        return Pair(penaltyFilterUiModel, itemSortFilterWrapperList)
    }

    fun transformUpdateSortFilterSelected(
        penaltyFilterUiModel: MutableList<PenaltyFilterUiModel>,
        itemSortFilterWrapperList: MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>,
        titleFilter: String,
        updateChipsSelected: Boolean
    ): Pair<MutableList<PenaltyFilterUiModel>, MutableList<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>> {
        penaltyFilterUiModel.find { it.title == TITLE_TYPE_PENALTY }?.chipsFilterList?.mapIndexed { index, chipsFilterPenaltyUiModel ->
            if (chipsFilterPenaltyUiModel.title == titleFilter) {
                chipsFilterPenaltyUiModel.isSelected = !updateChipsSelected
                itemSortFilterWrapperList.getOrNull(index)?.isSelected = !updateChipsSelected
            } else {
                chipsFilterPenaltyUiModel.isSelected = false
                itemSortFilterWrapperList.getOrNull(index)?.isSelected = false
            }
        }
        return Pair(penaltyFilterUiModel, itemSortFilterWrapperList)
    }

    fun getChipsFilterList(
        titleFilter: String,
        penaltyFilterUiModel: MutableList<PenaltyFilterUiModel>,
    ): List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return penaltyFilterUiModel.find { it.title == titleFilter }?.chipsFilterList ?: emptyList()
    }
}