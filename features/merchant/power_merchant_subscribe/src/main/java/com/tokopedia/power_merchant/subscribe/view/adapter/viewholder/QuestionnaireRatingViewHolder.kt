package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import com.tokopedia.power_merchant.subscribe.databinding.ItemPmQuestionnaireRatingOptionBinding
import android.view.View
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireRatingViewHolder(
    itemView: View,
    private val onAnswerSelected: () -> Unit
) : BaseViewHolder<QuestionnaireUiModel.QuestionnaireRatingUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_questionnaire_rating_option
    }

    private val binding: ItemPmQuestionnaireRatingOptionBinding? by viewBinding()

    override fun bind(element: QuestionnaireUiModel.QuestionnaireRatingUiModel) {
        showItem(element)
    }

    private fun showItem(element: QuestionnaireUiModel.QuestionnaireRatingUiModel) = binding?.run {
        tvPmDeactivationRatingQuestion.text = element.question
        ratingPmDeactivation.setListener(object :
            AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onRatingSelected(rating: Int) {
                element.givenRating = rating
                onAnswerSelected()
            }
        })
    }
}
