package com.tokopedia.shop.score.penalty.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import javax.inject.Inject

class PenaltyDetailMapper @Inject constructor(@ApplicationContext val context: Context?) {

    fun mapToPenaltyDetail(itemPenaltyUiModel: ItemPenaltyUiModel): ShopPenaltyDetailUiModel {
        val stepperPenaltyDetailList =
            if (itemPenaltyUiModel.isOldPage) {
                mapToStepperPenaltyDetailOld(itemPenaltyUiModel.statusPenalty)
            } else {
                mapToStepperPenaltyDetail(itemPenaltyUiModel.pageType)
            }
        return ShopPenaltyDetailUiModel(
            titleDetail = itemPenaltyUiModel.typePenalty,
            descStatusPenalty = itemPenaltyUiModel.descStatusPenalty,
            startDateDetail = itemPenaltyUiModel.startDate,
            summaryDetail = itemPenaltyUiModel.reasonPenalty,
            deductionPointPenalty = itemPenaltyUiModel.deductionPoint,
            endDateDetail = itemPenaltyUiModel.endDateDetail,
            prefixDateDetail = itemPenaltyUiModel.prefixDatePenalty,
            productName = itemPenaltyUiModel.productName,
            stepperPenaltyDetailList = stepperPenaltyDetailList
        )
    }

    private fun mapToStepperPenaltyDetail(
        @ShopPenaltyPageType pageType: String
    ): List<ShopPenaltyDetailUiModel.StepperPenaltyDetail> {
        return mutableListOf<ShopPenaltyDetailUiModel.StepperPenaltyDetail>().apply {
            when (pageType) {
                ShopPenaltyPageType.NOT_YET_DEDUCTED -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_NN300,
                            titleStepper = R.string.title_point_have_not_been_deducted_new,
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
                ShopPenaltyPageType.ONGOING -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            titleStepper = R.string.title_point_have_not_been_deducted_new,
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
                ShopPenaltyPageType.HISTORY -> {
                    add(
                        ShopPenaltyDetailUiModel.StepperPenaltyDetail(
                            colorDotStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            colorLineStepper = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                            titleStepper = R.string.title_point_have_not_been_deducted_new,
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

    private fun mapToStepperPenaltyDetailOld(
        statusPenalty: String
    ): List<ShopPenaltyDetailUiModel.StepperPenaltyDetail> {
        return mutableListOf<ShopPenaltyDetailUiModel.StepperPenaltyDetail>().apply {
            when (statusPenalty) {
                ShopScoreConstant.POINTS_NOT_YET_DEDUCTED, ShopScoreConstant.NOT_YET_ONGOING -> {
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
                ShopScoreConstant.ON_GOING, ShopScoreConstant.CAPITALIZED_ON_GOING -> {
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
                ShopScoreConstant.PENALTY_DONE, ShopScoreConstant.CAPITALIZED_PENALTY_DONE -> {
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

}
