package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.QuestionnaireOptionAdapter
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireMultipleViewHolder(
        itemView: View
) : BaseViewHolder<QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_questionnaire_multiple_option
    }

    private val tvTitle: Typography by findView(R.id.tvPmQuestionnaireTitle)
    private val rvOptions: RecyclerView by findView(R.id.rvPmQuestionnaireOptions)
    private val itemDivider: View by findView(R.id.horLinePmQuestionnaireOptions)
    private var optionAdapter: QuestionnaireOptionAdapter? = null

    override fun bind(element: QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel) {
        setupMultipleOptionsList(element)
        tvTitle.text = element.question
        itemDivider.visibility = if (element.showItemDivider) View.VISIBLE else View.GONE
    }

    private fun setupMultipleOptionsList(element: QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel) {
        optionAdapter = QuestionnaireOptionAdapter(element.options)
        rvOptions.layoutManager = object : LinearLayoutManager(itemView.context) {
            override fun canScrollHorizontally(): Boolean = false
        }
        rvOptions.adapter = optionAdapter
    }
}