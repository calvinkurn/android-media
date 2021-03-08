package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireRatingViewHolder(
        itemView: View
) : BaseViewHolder<QuestionnaireUiModel.QuestionnaireRatingUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_questionnaire_rating
    }

    private val tvQuestion: Typography by findView(R.id.tvPmDeactivationRatingQuestion)
    private val ratingBar: AnimatedRatingPickerCreateReviewView by findView(R.id.ratingPmDeactivation)

    override fun bind(element: QuestionnaireUiModel.QuestionnaireRatingUiModel) {
        showItem(element)
    }

    private fun showItem(element: QuestionnaireUiModel.QuestionnaireRatingUiModel) {
        tvQuestion.text = element.question
        ratingBar.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onRatingSelected(rating: Int) {
                element.givenRating = rating
            }
        })
    }
}