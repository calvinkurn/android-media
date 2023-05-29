package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianDailyBudgetUiModel

class AccordianDailyBudgetViewHolder(private val view: View) :
    AbstractViewHolder<AccordianDailyBudgetUiModel>(view) {

    private val text: com.tokopedia.unifyprinciples.Typography =
        view.findViewById(R.id.accordianTypography)

    override fun bind(element: AccordianDailyBudgetUiModel?) {
        text.text = element?.text
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_daily_budget_layout
    }
}
