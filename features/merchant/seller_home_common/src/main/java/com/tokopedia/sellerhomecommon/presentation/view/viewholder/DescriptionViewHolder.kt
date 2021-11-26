package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcDescriptionWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.DescriptionWidgetUiModel

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class DescriptionViewHolder(
    view: View?,
    private val listener: Listener
) : AbstractViewHolder<DescriptionWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_description_widget
    }

    private val binding by lazy { ShcDescriptionWidgetBinding.bind(itemView) }

    override fun bind(element: DescriptionWidgetUiModel) {
        with(binding) {
            tvDescriptionTitle.text = element.title
            tvDescriptionDesc.text = element.subtitle
            setupDetails(element)
        }
    }

    private fun setupDetails(element: DescriptionWidgetUiModel) {
        with(binding) {
            if (element.ctaText.isNotBlank() && element.appLink.isNotBlank()) {
                tvDescriptionCta.text = element.ctaText
                tvDescriptionCta.visible()
                icDescriptionCtaArrow.visible()
                tvDescriptionCta.setOnClickListener {
                    goToDetails(element)
                }
                icDescriptionCtaArrow.setOnClickListener {
                    goToDetails(element)
                }
            } else {
                tvDescriptionCta.gone()
                icDescriptionCtaArrow.gone()
            }

            root.addOnImpressionListener(element.impressHolder) {
                listener.sendDescriptionImpressionEvent(element)
            }
        }
    }

    private fun goToDetails(model: DescriptionWidgetUiModel) {
        if (RouteManager.route(itemView.context, model.appLink)) {
            listener.sendDescriptionCtaClickEvent(model)
        }
    }

    interface Listener : BaseViewHolderListener {
        fun sendDescriptionImpressionEvent(model: DescriptionWidgetUiModel) {}

        fun sendDescriptionCtaClickEvent(model: DescriptionWidgetUiModel) {}
    }
}