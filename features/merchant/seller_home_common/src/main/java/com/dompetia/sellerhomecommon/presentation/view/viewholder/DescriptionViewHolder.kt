package com.dompetia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.dompetia.seller_home_common.R
import com.dompetia.sellerhomecommon.analytics.SellerHomeTracking
import com.dompetia.sellerhomecommon.presentation.model.DescriptionWidgetUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.shc_description_widget.view.*

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class DescriptionViewHolder(view: View?) : AbstractViewHolder<DescriptionWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_description_widget
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
                    goToDetails(element.appLink, element.title)
                }
                icDescriptionCtaArrow.setOnClickListener {
                    goToDetails(element.appLink, element.title)
                }
            } else {
                tvDescriptionCta.gone()
                icDescriptionCtaArrow.gone()
            }

            addOnImpressionListener(element.impressHolder) {
                SellerHomeTracking.sendImpressionDescriptionEvent(element.title)
            }
        }
    }

    private fun goToDetails(appLink: String, descriptionTitle: String) {
        if (RouteManager.route(itemView.context, appLink)) {
            SellerHomeTracking.sendClickDescriptionEvent(descriptionTitle)
        }
    }
}