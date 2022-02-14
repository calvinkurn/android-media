package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicCustomInfoBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 13/08/20
 */
class ProductCustomInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductCustomInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_custom_info
    }

    private val binding = ItemDynamicCustomInfoBinding.bind(view)

    override fun bind(element: ProductCustomInfoDataModel) {
        if (element.title.isEmpty() && element.icon.isEmpty()) {
            binding.customDesc.setMargin(0, 0, 8.toPx(), 0)
        } else {
            binding.customDesc.setMargin(0, 4.toPx(), 8.toPx(), 0)
        }

        renderSeparator(element.separator)
        renderTitle(element.title, element.icon)
        renderDescription(element.description)
        renderLabel(element.getLabelTypeByColor(), element.labelValue)
        setupApplink(element.applink, element.title, getComponentTrackData(element))
    }

    private fun renderLabel(labelColor: Int, labelValue: String) {
        binding.labelCustomInfo.run {
            setLabel(labelValue)
            setLabelType(labelColor)
            showWithCondition(labelValue.isNotEmpty())
        }
    }

    private fun renderSeparator(separator: String) = with(binding) {
        topSeparator.showWithCondition(separator == ProductCustomInfoDataModel.SEPARATOR_BOTH || separator == ProductCustomInfoDataModel.SEPARATOR_TOP)
        bottomSeparator.showWithCondition(separator == ProductCustomInfoDataModel.SEPARATOR_BOTH || separator == ProductCustomInfoDataModel.SEPARATOR_BOTTOM)
    }

    private fun setupApplink(applink: String, title: String, componentTrackData: ComponentTrackDataModel) = with(binding) {
        if (applink.isNotEmpty()) {
            customArrow.show()
            view.setOnClickListener {
                listener.onBbiInfoClick(applink, title, componentTrackData)
            }
        } else {
            customArrow.hide()
            view.setOnClickListener {}
        }
    }

    private fun renderDescription(description: String) = with(binding) {
        customDesc.shouldShowWithAction(description.isNotEmpty()) {
            customDesc.text = HtmlLinkHelper(view.context, description).spannedString
        }
    }

    private fun renderTitle(title: String, icon: String) = with(binding) {
        customImage.shouldShowWithAction(icon.isNotEmpty()) {
            customImage.loadIcon(icon)
        }
        customTitle.shouldShowWithAction(title.isNotEmpty()) {
            customTitle.text = title
        }
    }

    private fun getComponentTrackData(element: ProductCustomInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}