package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetCatalogPanelImageBinding
import com.tokopedia.catalogcommon.listener.PanelImageListener
import com.tokopedia.catalogcommon.uimodel.PanelImageUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class PanelImageViewHolder(itemView: View, val panelImageListener: PanelImageListener? = null) :
    AbstractViewHolder<PanelImageUiModel>(itemView) {

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.widget_catalog_panel_image
    }

    private val binding by viewBinding<WidgetCatalogPanelImageBinding>()

    override fun bind(element: PanelImageUiModel) {
        binding?.let {
            if (element.content.size > 1) {
                // TODO: Put the validation here to check whether we should override the text color or not
                // Call this function `overrideWidgetTheme` to override the widget's theme

                it.catalogPanelWidgetImage1?.loadImageRounded(element.content[0].imageUrl, 8.toPx().toFloat())
                it.catalogPanelWidgetImage1?.loadImageRounded(element.content[1].imageUrl, 8.toPx().toFloat())
                it.catalogPanelWidgetTxtHighlight1.text = element.content[0].highlight
                it.catalogPanelWidgetTxtTitle1.text = element.content[0].title
                it.catalogPanelWidgetTxtDescription1.text = element.content[0].description
                it.catalogPanelWidgetTxtHighlight2.text = element.content[1].highlight
                it.catalogPanelWidgetTxtTitle2.text = element.content[1].title
                it.catalogPanelWidgetTxtDescription2.text = element.content[1].description
            }
        }
        panelImageListener?.onPanelImageImpression(element.widgetName)
    }

    private fun overrideWidgetTheme(fontColor: Int) {
        binding?.let {
            it.catalogPanelWidgetTxtHighlight1.setTextColor(fontColor)
            it.catalogPanelWidgetTxtTitle1.setTextColor(fontColor)
            it.catalogPanelWidgetTxtDescription1.setTextColor(fontColor)
            it.catalogPanelWidgetTxtHighlight2.setTextColor(fontColor)
            it.catalogPanelWidgetTxtTitle2.setTextColor(fontColor)
            it.catalogPanelWidgetTxtDescription2.setTextColor(fontColor)
        }
    }
}
