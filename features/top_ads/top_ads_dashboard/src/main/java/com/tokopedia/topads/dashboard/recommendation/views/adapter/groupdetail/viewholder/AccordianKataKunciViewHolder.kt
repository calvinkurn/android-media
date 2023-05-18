package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel

class AccordianKataKunciViewHolder(private val itemView: View) :
    AbstractViewHolder<AccordianKataKunciUiModel>(itemView) {

    private val text: com.tokopedia.unifyprinciples.Typography =
        itemView.findViewById(R.id.accordianTypography)

    override fun bind(element: AccordianKataKunciUiModel?) {
        text.text = element?.text
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_kata_kunci_layout
    }
}
