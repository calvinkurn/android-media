package com.tokopedia.shop.score.penalty.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.ON_GOING
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_DATE_TEXT
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM
import com.tokopedia.shop.score.common.ShopScoreConstant.PENALTY_DONE
import com.tokopedia.shop.score.common.ShopScoreConstant.POINTS_NOT_YET_DEDUCTED
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_LATEST
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_LATEST_VALUE
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_OLDEST
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_OLDEST_VALUE
import com.tokopedia.shop.score.common.ShopScoreConstant.TITLE_SORT
import com.tokopedia.shop.score.common.ShopScoreConstant.TITLE_TYPE_PENALTY
import com.tokopedia.shop.score.common.formatDate
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltySummaryResponse
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyTypesResponse
import com.tokopedia.shop.score.penalty.presentation.model.*
import javax.inject.Inject

class PenaltyMapper @Inject constructor(@ApplicationContext val context: Context?) {

    fun mapToPenaltyDetailDummy(statusPenalty: String): ShopPenaltyDetailUiModel {
        return ShopPenaltyDetailUiModel(
                titleDetail = "Cash advance",
                dateDetail = "31 Des 2020",
                summaryDetail = "Seller melakukan cash advance pada transaksi INV/20210126/XX/V/553738330",
                deductionPointPenalty = "5",
                statusDate = "31 Des 2020",
                stepperPenaltyDetailList = mapToStepperPenaltyDetail(statusPenalty)
        )
    }

    private fun mapToStepperPenaltyDetail(statusPenalty: String): List<ShopPenaltyDetailUiModel.StepperPenaltyDetail> {
        return mutableListOf<ShopPenaltyDetailUiModel.StepperPenaltyDetail>().apply {
            when (statusPenalty) {
                POINTS_NOT_YET_DEDUCTED -> {
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = context?.getString(R.string.title_point_have_not_been_deducted).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isBold = true,
                            isDividerShow = true
                    ))
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = context?.getString(R.string.title_on_going).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_68,
                            isDividerShow = true
                    ))
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = context?.getString(R.string.title_penalty_over).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_68,
                    ))
                }
                ON_GOING -> {
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            titleStepper = context?.getString(R.string.title_point_have_not_been_deducted).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_68,
                            isDividerShow = true
                    ))
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = context?.getString(R.string.title_on_going).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isBold = true,
                            isDividerShow = true
                    ))
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_N100,
                            titleStepper = context?.getString(R.string.title_penalty_over).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                    ))
                }
                PENALTY_DONE -> {
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            titleStepper = context?.getString(R.string.title_point_have_not_been_deducted).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isDividerShow = true
                    ))
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            titleStepper = context?.getString(R.string.title_on_going).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isDividerShow = true
                    ))
                    add(ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_G500,
                            titleStepper = context?.getString(R.string.title_penalty_over).orEmpty(),
                            colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                            isBold = true
                    ))
                }
            }
        }
    }

    fun mapToPenaltyData(shopScorePenaltySummaryWrapper: ShopPenaltySummaryTypeWrapper,
                         shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail,
                         dateFilter: Pair<String, String>,
                         sortBy: Int,
                         typeId: Int

    ): PenaltyDataWrapper {
        val penaltyTypes = shopScorePenaltySummaryWrapper.shopScorePenaltyTypesResponse?.result
        return PenaltyDataWrapper(
                cardShopPenaltyUiModel = shopScorePenaltySummaryWrapper.shopScorePenaltySummaryResponse?.result?.let { mapToCardShopPenalty(it) },
                itemDetailPenaltyFilterUiModel = penaltyTypes?.let { mapToDetailPenaltyFilter(it, dateFilter, typeId) },
                itemPenaltyUiModel = mapToItemPenaltyList(shopScorePenaltyDetailResponse),
                penaltyFilterList = penaltyTypes?.let { mapToPenaltyFilterBottomSheet(it, sortBy, typeId) }
        )
    }

    private fun mapToCardShopPenalty(penaltySummary: ShopScorePenaltySummaryResponse.ShopScorePenaltySummary.Result): ItemCardShopPenaltyUiModel {
        return ItemCardShopPenaltyUiModel(totalPenalty = penaltySummary.penaltyAmount, hasPenalty = penaltySummary.penaltyAmount.isZero(), deductionPoints = penaltySummary.penalty)
    }

    private fun mapToDetailPenaltyFilter(penaltyTypes: List<ShopScorePenaltyTypesResponse.ShopScorePenaltyTypes.Result>,
                                         dateFilter: Pair<String, String>,
                                         typeId: Int
    ): ItemDetailPenaltyFilterUiModel {
        return ItemDetailPenaltyFilterUiModel(
                periodDetail = "${dateFilter.first.formatDate(PATTERN_PENALTY_DATE_PARAM, PATTERN_DATE_TEXT)} - ${dateFilter.second.formatDate(PATTERN_PENALTY_DATE_PARAM, PATTERN_DATE_TEXT)}",
                itemSortFilterWrapperList = mapToSortFilterPenalty(penaltyTypes, typeId))
    }

    private fun mapToSortFilterPenalty(penaltyTypes: List<ShopScorePenaltyTypesResponse.ShopScorePenaltyTypes.Result>, typeId: Int): List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper> {
        return mutableListOf<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>().apply {
            penaltyTypes.map {
                add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(title = it.name, isSelected = it.id == typeId, idFilter = it.id))
            }
        }
    }

    fun mapToItemPenaltyList(shopScorePenaltyDetailResponse: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail): Triple<List<ItemPenaltyUiModel>, Boolean, Boolean> {
        return Triple(mutableListOf<ItemPenaltyUiModel>().apply {
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
                add(ItemPenaltyUiModel(
                        statusPenalty = it.status,
                        startDate = it.penaltyStartDate.formatDate(PATTERN_PENALTY_DATE_PARAM, PATTERN_DATE_TEXT),
                        endDate = it.penaltyExpirationDate.formatDate(PATTERN_PENALTY_DATE_PARAM, PATTERN_DATE_TEXT),
                        typePenalty = it.typeName,
                        descPenalty = it.reason,
                        colorPenalty = colorTypePenalty
                ))
            }
        }, shopScorePenaltyDetailResponse.hasNext, shopScorePenaltyDetailResponse.hasPrev)
    }

    private fun mapToPenaltyFilterBottomSheet(penaltyTypes: List<ShopScorePenaltyTypesResponse.ShopScorePenaltyTypes.Result>,
                                              sortBy: Int, typeId: Int): List<PenaltyFilterUiModel> {
        return mutableListOf<PenaltyFilterUiModel>().apply {
            add(PenaltyFilterUiModel(title = TITLE_SORT, isDividerVisible = true, chipsFilerList = mapToChipsSortFilter(sortBy)))
            add(PenaltyFilterUiModel(title = TITLE_TYPE_PENALTY, chipsFilerList = mapToChipsTypePenaltyFilter(penaltyTypes, typeId)))
        }
    }

    private fun mapToChipsSortFilter(sortBy: Int): List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>().apply {
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = SORT_LATEST, isSelected = sortBy == SORT_LATEST_VALUE, value = SORT_LATEST_VALUE))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = SORT_OLDEST, isSelected = sortBy == SORT_OLDEST_VALUE, value = SORT_OLDEST_VALUE))
        }
    }

    private fun mapToChipsTypePenaltyFilter(penaltyTypes: List<ShopScorePenaltyTypesResponse.ShopScorePenaltyTypes.Result>, typeId: Int): List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>().apply {
            penaltyTypes.map {
                add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(it.name, isSelected = it.id == typeId, value = it.id))
            }
        }
    }
}