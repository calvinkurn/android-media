package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmQuestionnaireRatingBinding
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireRatingViewHolder(
        itemView: View
) : BaseViewHolder<QuestionnaireUiModel.QuestionnaireRatingUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_questionnaire_rating
    }

    private val binding: ItemPmQuestionnaireRatingBinding? by viewBinding()
    private val ratingStatusList by itemView.context.getRatingStatusList()

    private fun Context.getRatingStatusList(): Lazy<Array<String>> {
        return lazy {
            arrayOf(
                    getString(R.string.pm_rate_1_star), getString(R.string.pm_rate_2_star),
                    getString(R.string.pm_rate_3_star), getString(R.string.pm_rate_4_star),
                    getString(R.string.pm_rate_5_star)
            )
        }
    }

    override fun bind(element: QuestionnaireUiModel.QuestionnaireRatingUiModel) {
        showItem(element)
    }

    private fun showItem(element: QuestionnaireUiModel.QuestionnaireRatingUiModel) = binding?.run {
        tvPmDeactivationRatingQuestion.text = element.question
        ratingPmDeactivation.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onRatingSelected(rating: Int) {
                element.givenRating = rating
                setRatingStatusText(rating)
            }
        })
    }

    private fun ItemPmQuestionnaireRatingBinding.setRatingStatusText(rating: Int) {
        tvPmRatingStatus.text = ratingStatusList.getOrNull(rating.minus(1)).orEmpty()
    }
}