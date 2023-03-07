package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmQuestionnaireSingleOptionBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.QuestionnaireRadioButtonOptionAdapter
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireRadioButtonViewHolder(
    itemView: View,
    private val onAnswerSelected: () -> Unit
) : BaseViewHolder<QuestionnaireUiModel.QuestionnaireSingleOptionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_questionnaire_single_option
    }

    private val binding: ItemPmQuestionnaireSingleOptionBinding? by viewBinding()

    private var radioButtonOptionAdapter: QuestionnaireRadioButtonOptionAdapter? = null

    override fun bind(element: QuestionnaireUiModel.QuestionnaireSingleOptionUiModel) {
        showItem(element)
    }

    private fun showItem(element: QuestionnaireUiModel.QuestionnaireSingleOptionUiModel) = binding?.run {
        tvPmDeactivationRatingQuestion.text = element.question
        radioButtonOptionAdapter = QuestionnaireRadioButtonOptionAdapter(element.options, onAnswerSelected)

        rvPmQuestionnaireRbOptions.run {
            layoutManager = LinearLayoutManager(context)
            adapter = radioButtonOptionAdapter
        }
    }
}
