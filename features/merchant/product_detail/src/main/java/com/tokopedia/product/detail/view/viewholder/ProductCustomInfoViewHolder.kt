package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_dynamic_custom_info.view.*

/**
 * Created by Yehezkiel on 13/08/20
 */
class ProductCustomInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductCustomInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_custom_info
    }

    override fun bind(element: ProductCustomInfoDataModel) {
        if (element.title.isEmpty() && element.icon.isEmpty()) {
            view.custom_desc.setMargin(0, 0, 8.toPx(), 0)
        } else {
            view.custom_desc.setMargin(0, 4.toPx(), 8.toPx(), 0)
        }

        renderSeparator(element.separator)
        renderTitle(element.title, element.icon)
        renderDescription(element.description)
        setupApplink(element.applink, element.title, getComponentTrackData(element))
    }

    private fun renderSeparator(separator: String) = with(view) {
        top_separator.showWithCondition(separator == ProductCustomInfoDataModel.SEPARATOR_BOTH || separator == ProductCustomInfoDataModel.SEPARATOR_TOP)
        bottom_separator.showWithCondition(separator == ProductCustomInfoDataModel.SEPARATOR_BOTH || separator == ProductCustomInfoDataModel.SEPARATOR_BOTTOM)
    }

    private fun setupApplink(applink: String, title: String, componentTrackData: ComponentTrackDataModel) = with(view) {
        if (applink.isNotEmpty()) {
            custom_arrow?.show()
            this.setOnClickListener {
                listener.onBbiInfoClick(applink, title, componentTrackData)
            }
        } else {
            custom_arrow?.hide()
            this.setOnClickListener {}
        }
    }

    private fun renderDescription(description: String) = with(view) {
        custom_desc.shouldShowWithAction(description.isNotEmpty()) {
            custom_desc.text = HtmlLinkHelper(context, description).spannedString
        }
    }

    private fun renderTitle(title: String, icon: String) = with(view) {
        custom_image.shouldShowWithAction(icon.isNotEmpty()) {
            custom_image.loadIcon(icon)
        }
        custom_title.shouldShowWithAction(title.isNotEmpty()) {
            custom_title.text = title
        }
    }

    private fun getComponentTrackData(element: ProductCustomInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}