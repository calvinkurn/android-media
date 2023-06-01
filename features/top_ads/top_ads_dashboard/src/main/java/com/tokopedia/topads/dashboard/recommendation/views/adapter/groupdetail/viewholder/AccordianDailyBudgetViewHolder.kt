package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianDailyBudgetUiModel

class AccordianDailyBudgetViewHolder(private val view: View) :
    AbstractViewHolder<AccordianDailyBudgetUiModel>(view) {

    private val currentBudget : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.currentBudget)
    private val recommendedBudget : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.recommendedBudget)
    private val dailyBudget : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.dailyBudgetInput)

    override fun bind(element: AccordianDailyBudgetUiModel?) {
        currentBudget.text = String.format("Rp%d", 1324)
        recommendedBudget.text = String.format("Rp%d", 1234)
        dailyBudget.editText.setText("1234")
    }

    companion object {
        val LAYOUT = R.layout.topads_insights_accordian_anggaran_harian_item_layout
    }
}
