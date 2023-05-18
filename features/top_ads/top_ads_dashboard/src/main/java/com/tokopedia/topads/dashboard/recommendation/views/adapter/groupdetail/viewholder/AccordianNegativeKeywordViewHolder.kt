package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianNegativeKeywordUiModel

class AccordianNegativeKeywordViewHolder(private val view: View) :
    AbstractViewHolder<AccordianNegativeKeywordUiModel>(view) {

    private val text: com.tokopedia.unifyprinciples.Typography =
        view.findViewById(R.id.accordianTypography)

    override fun bind(element: AccordianNegativeKeywordUiModel?) {
        text.text = element?.text
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_negative_keyword_layout
    }
}
