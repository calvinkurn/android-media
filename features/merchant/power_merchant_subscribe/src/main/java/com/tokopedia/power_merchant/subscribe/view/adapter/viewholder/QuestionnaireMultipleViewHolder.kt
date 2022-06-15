package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmQuestionnaireMultipleOptionBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.QuestionnaireOptionAdapter
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireMultipleViewHolder(
        itemView: View,
        private val onAnswerSelected: () -> Unit
) : BaseViewHolder<QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_questionnaire_multiple_option
    }

    private val binding: ItemPmQuestionnaireMultipleOptionBinding? by viewBinding()

    private var optionAdapter: QuestionnaireOptionAdapter? = null

    override fun bind(element: QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel) {
        binding?.run {
            setupMultipleOptionsList(element)
            tvPmQuestionnaireTitle.text = element.question
            horLinePmQuestionnaireOptions.visibility = if (element.showItemDivider) View.VISIBLE else View.GONE
        }
    }

    private fun ItemPmQuestionnaireMultipleOptionBinding.setupMultipleOptionsList(element: QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel) {
        optionAdapter = QuestionnaireOptionAdapter(element.options, onAnswerSelected)
        rvPmQuestionnaireOptions.layoutManager = object : LinearLayoutManager(itemView.context) {
            override fun canScrollHorizontally(): Boolean = false
        }
        rvPmQuestionnaireOptions.adapter = optionAdapter
    }
}