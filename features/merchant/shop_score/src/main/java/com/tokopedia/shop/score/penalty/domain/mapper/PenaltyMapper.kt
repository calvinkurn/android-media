package com.tokopedia.shop.score.penalty.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.DELIVERY_IGNORED
import com.tokopedia.shop.score.common.ShopScoreConstant.DELIVERY_REFUSED
import com.tokopedia.shop.score.common.ShopScoreConstant.GUILT_RESOLUTION_CENTER
import com.tokopedia.shop.score.common.ShopScoreConstant.ON_GOING
import com.tokopedia.shop.score.common.ShopScoreConstant.ORDER_IGNORED
import com.tokopedia.shop.score.common.ShopScoreConstant.PENALTY_DONE
import com.tokopedia.shop.score.common.ShopScoreConstant.POINTS_NOT_YET_DEDUCTED
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_LATEST
import com.tokopedia.shop.score.common.ShopScoreConstant.SORT_OLDEST
import com.tokopedia.shop.score.common.ShopScoreConstant.TITLE_SORT
import com.tokopedia.shop.score.common.ShopScoreConstant.TITLE_TYPE_PENALTY
import com.tokopedia.shop.score.penalty.presentation.model.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
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

    fun mapToPenaltyVisitableDummy(): List<BasePenaltyPage> {
        return mutableListOf<BasePenaltyPage>().apply {
            add(mapToCardShopPenalty())
            add(mapToDetailPenaltyFilter())
            addAll(mapToItemPenaltyList())
        }
    }

    private fun mapToCardShopPenalty(): ItemCardShopPenaltyUiModel {
        return ItemCardShopPenaltyUiModel(totalPenalty = 9, hasPenalty = true, deductionPoints = -5)
    }

    private fun mapToDetailPenaltyFilter(): ItemDetailPenaltyFilterUiModel {
        return ItemDetailPenaltyFilterUiModel(periodDetail = "5 july - 6 sept", itemSortFilterWrapperList = mapToSortFilterPenalty())
    }

    private fun mapToSortFilterPenalty(): List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper> {
        return mutableListOf<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>().apply {
            add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    sortFilterItem = SortFilterItem(
                            title = GUILT_RESOLUTION_CENTER,
                            type = ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL
                    ),
                    isSelected = false
            ))
            add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    sortFilterItem = SortFilterItem(
                            title = ORDER_IGNORED,
                            type = ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL
                    ),
                    isSelected = false
            ))
            add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    sortFilterItem = SortFilterItem(
                            title = DELIVERY_IGNORED,
                            type = ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL
                    ),
                    isSelected = false
            ))
            add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    sortFilterItem = SortFilterItem(
                            title = DELIVERY_REFUSED,
                            type = ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL
                    ),
                    isSelected = false
            ))
        }
    }

    private fun mapToItemPenaltyList(): List<ItemPenaltyUiModel> {
        return mutableListOf<ItemPenaltyUiModel>().apply {
            add(ItemPenaltyUiModel(
                    statusPenalty = ON_GOING,
                    statusDate = "27 jan 2021", periodDate = "Selesai di 8 Mei 2021",
                    transactionPenalty = GUILT_RESOLUTION_CENTER,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_RN600
            ))
            add(ItemPenaltyUiModel(statusPenalty = PENALTY_DONE,
                    statusDate = "25 jan 2021", periodDate = "Sejak 10 Mei 2021",
                    transactionPenalty = ORDER_IGNORED,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_NN500
            ))
            add(ItemPenaltyUiModel(statusPenalty = ON_GOING,
                    statusDate = "27 jan 2021", periodDate = "Selesai 30 juli 2021",
                    transactionPenalty = DELIVERY_IGNORED,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_RN600
            ))
            add(ItemPenaltyUiModel(statusPenalty = POINTS_NOT_YET_DEDUCTED,
                    statusDate = "17 jan 2021", periodDate = "Mulai juli 2021",
                    transactionPenalty = DELIVERY_REFUSED,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_NN500
            ))
            add(ItemPenaltyUiModel(
                    statusPenalty = ON_GOING,
                    statusDate = "27 jan 2021", periodDate = "8 Mei 2021",
                    transactionPenalty = GUILT_RESOLUTION_CENTER,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_RN600
            ))
            add(ItemPenaltyUiModel(statusPenalty = PENALTY_DONE,
                    statusDate = "25 jan 2021", periodDate = "Sejak 10 Mei 2021",
                    transactionPenalty = ORDER_IGNORED,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_NN500
            ))
            add(ItemPenaltyUiModel(statusPenalty = ON_GOING,
                    statusDate = "27 jan 2021", periodDate = "30 juli 2021",
                    transactionPenalty = DELIVERY_IGNORED,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_RN600
            ))
            add(ItemPenaltyUiModel(statusPenalty = POINTS_NOT_YET_DEDUCTED,
                    statusDate = "17 jan 2021", periodDate = "24 juli 2021",
                    transactionPenalty = DELIVERY_REFUSED,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_NN500
            ))
            add(ItemPenaltyUiModel(
                    statusPenalty = ON_GOING,
                    statusDate = "27 jan 2021", periodDate = "8 Mei 2021",
                    transactionPenalty = GUILT_RESOLUTION_CENTER,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_RN600
            ))
            add(ItemPenaltyUiModel(statusPenalty = PENALTY_DONE,
                    statusDate = "25 jan 2021", periodDate = "Sejak 10 Mei 2021",
                    transactionPenalty = ORDER_IGNORED,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_NN500
            ))
            add(ItemPenaltyUiModel(statusPenalty = ON_GOING,
                    statusDate = "27 jan 2021", periodDate = "30 juli 2021",
                    transactionPenalty = DELIVERY_REFUSED,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_RN600
            ))
            add(ItemPenaltyUiModel(statusPenalty = POINTS_NOT_YET_DEDUCTED,
                    statusDate = "17 jan 2021", periodDate = "24 juli 2021",
                    transactionPenalty = GUILT_RESOLUTION_CENTER,
                    descPenalty = "INV/20210126/XX/V/553738285",
                    colorPenalty = com.tokopedia.unifyprinciples.R.color.Unify_NN500
            ))
        }
    }

    fun mapToPenaltyFilterBottomSheet(): List<PenaltyFilterUiModel> {
        return mutableListOf<PenaltyFilterUiModel>().apply {
            add(PenaltyFilterUiModel(title = TITLE_SORT, isDividerVisible = true, chipsFilerList = mapToChipsSortFilter()))
            add(PenaltyFilterUiModel(title = TITLE_TYPE_PENALTY, chipsFilerList = mapToChipsTypePenaltyFilter()))
        }
    }

    private fun mapToChipsSortFilter(): List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>().apply {
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = SORT_LATEST))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = SORT_OLDEST))
        }
    }

    private fun mapToChipsTypePenaltyFilter(): List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>().apply {
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = GUILT_RESOLUTION_CENTER))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = ORDER_IGNORED))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = DELIVERY_IGNORED))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = DELIVERY_REFUSED))
        }
    }
}