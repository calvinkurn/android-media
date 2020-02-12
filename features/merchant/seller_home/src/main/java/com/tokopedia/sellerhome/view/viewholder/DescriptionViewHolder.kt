package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.SellerHomeTracking
import com.tokopedia.sellerhome.view.model.DescriptionWidgetUiModel
import kotlinx.android.synthetic.main.sah_description_widget.view.*

class DescriptionViewHolder(view: View?) : AbstractViewHolder<DescriptionWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_description_widget
    }

    override fun bind(element: DescriptionWidgetUiModel) {
        with(itemView) {
            tvDescriptionTitle.text = element.title
            tvDescriptionDesc.text = element.subtitle
            setupDetails(element)
        }
    }

    private fun setupDetails(element: DescriptionWidgetUiModel) {
        with(itemView) {
            if (element.ctaText.isNotBlank() && element.appLink.isNotBlank()) {
                tvDescriptionCta.text = element.ctaText
                tvDescriptionCta.visible()
                icDescriptionCtaArrow.visible()
                tvDescriptionCta.setOnClickListener {
                    goToDetails(element.appLink, element.dataKey, element.title)
                }
                icDescriptionCtaArrow.setOnClickListener {
                    goToDetails(element.appLink, element.dataKey, element.title)
                }
            } else {
                tvDescriptionCta.gone()
                icDescriptionCtaArrow.gone()
            }
        }
    }

    private fun goToDetails(applink: String, dataKey: String, descriptionTitle: String) {
        SellerHomeTracking.sendClickDescriptionEvent(dataKey, descriptionTitle)
        RouteManager.route(itemView.context, applink)
    }
}