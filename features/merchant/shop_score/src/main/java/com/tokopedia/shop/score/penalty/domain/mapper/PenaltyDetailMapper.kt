package com.tokopedia.shop.score.penalty.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import javax.inject.Inject

class PenaltyDetailMapper @Inject constructor(@ApplicationContext val context: Context?) {

    fun mapToPenaltyDetail(itemPenaltyUiModel: ItemPenaltyUiModel): ShopPenaltyDetailUiModel {
        return ShopPenaltyDetailUiModel(
            titleDetail = itemPenaltyUiModel.typePenalty,
            descStatusPenalty = itemPenaltyUiModel.descStatusPenalty,
            startDateDetail = itemPenaltyUiModel.startDate,
            summaryDetail = itemPenaltyUiModel.reasonPenalty,
            deductionPointPenalty = itemPenaltyUiModel.deductionPoint,
            endDateDetail = itemPenaltyUiModel.endDateDetail,
            prefixDateDetail = itemPenaltyUiModel.prefixDatePenalty,
            productName = itemPenaltyUiModel.productName,
            stepperPenaltyDetailList = mapToStepperPenaltyDetail(
                itemPenaltyUiModel.statusPenalty,
                itemPenaltyUiModel.isOldPage
            )
        )
    }

    private fun mapToStepperPenaltyDetail(
        statusPenalty: String,
        isOldPage: Boolean
    ): List<ShopPenaltyDetailUiModel.StepperPenaltyDetail> {
        return mutableListOf<ShopPenaltyDetailUiModel.StepperPenaltyDetail>().apply {
            when (statusPenalty) {
                ShopScoreConstant.POINTS_NOT_YET_DEDUCTED -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            titleStepper = getNotYetDeductedMessageRes(isOldPage),
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
                            titleStepper = getNotYetDeductedMessageRes(isOldPage),
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
                            titleStepper = getNotYetDeductedMessageRes(isOldPage),
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

    private fun getNotYetDeductedMessageRes(isOldPage: Boolean): Int {
        return if (isOldPage) {
            R.string.title_point_have_not_been_deducted
        } else {
            R.string.title_point_have_not_been_deducted_new
        }
    }

}
