package com.tokopedia.top_ads_on_boarding.view.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.top_ads_on_boarding.R
import com.tokopedia.top_ads_on_boarding.model.OnboardingFaqItemUiModel
import com.tokopedia.unifyprinciples.Typography

class OnboardingFaqItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val accordionUnify: AccordionUnify = itemView.findViewById(R.id.accordionUnify)

    fun bind(element: OnboardingFaqItemUiModel, onAccordianItemClicked: (Int) -> Unit ) {
        accordionUnify.apply {
            accordionData.clear()
            removeAllViews()
            val accordionUnifyData = getAccordianData(element)
            addGroup(accordionUnifyData)
            onItemClick = {_, boolean -> onAccordianItemClicked.invoke(element.id)}
        }
    }

    private fun getAccordianData(element: OnboardingFaqItemUiModel): AccordionDataUnify {
        return AccordionDataUnify(
            title = element.title,
            isExpanded = element.isExpanded,
            expandableView = getView(element.desc),
        )
    }

    private fun getView(desc: String): View {
        val layout = View.inflate(view.context, R.layout.topads_onboarding_faq_accordian_item_layout, null)
        layout.findViewById<Typography>(R.id.entry).text = desc
        return layout
    }
}
