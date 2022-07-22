package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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
class ProductCustomInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductCustomInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_custom_info
    }

    private val binding = ItemDynamicCustomInfoBinding.bind(view)

    override fun bind(element: ProductCustomInfoDataModel) {
        impressComponent(element)
        setWidgetMargin(element)
        renderWidget(element)
        setupAppLink(element)
    }

    private fun impressComponent(element: ProductCustomInfoDataModel) {
        if (element.title.isNotEmpty() && element.icon.isNotEmpty()) {
            val componentTrack = getComponentTrackData(element)
            itemView.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(componentTrack)
                listener.showCustomInfoCoachMark(element.name, binding.customImage)
            }
        }
    }

    /**
     * show label `see` and set event click if appLink is not empty
     */
    private fun setupAppLink(
        element: ProductCustomInfoDataModel
    ) = with(binding) {

        if (element.applink.isNotEmpty()) {
            customLabelCheck.show()

            val trackData = getComponentTrackData(element = element)

            view.setOnClickListener {
                listener.onBbiInfoClick(
                    url = element.applink,
                    title = element.title,
                    componentTrackDataModel = trackData
                )
            }
        } else {
            customLabelCheck.hide()
            view.setOnClickListener {}
        }
    }

    /**
     * render widget and set content with condition
     */
    private fun renderWidget(element: ProductCustomInfoDataModel) = with(binding) {
        customDesc.shouldShowWithAction(element.description.isNotEmpty()) {
            customDesc.text = HtmlLinkHelper(view.context, element.description).spannedString
        }

        customImage.shouldShowWithAction(element.icon.isNotEmpty()) {
            customImage.loadIcon(element.icon)
        }

        customTitle.shouldShowWithAction(element.title.isNotEmpty()) {
            customTitle.text = element.title
        }

        binding.labelCustomInfo.shouldShowWithAction(element.labelValue.isNotEmpty()) {
            binding.labelCustomInfo.setLabel(element.labelValue)
            binding.labelCustomInfo.setLabelType(element.labelColor)
        }

        // separator
        renderSeparator(element.separator)
    }

    /**
     * show top and bottom separator with condition
     */
    private fun renderSeparator(separator: String) = with(binding) {
        val shouldTopSeparatorShow = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH
                || separator == ProductCustomInfoDataModel.SEPARATOR_TOP
        val shouldBottomSeparatorShow = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH
                || separator == ProductCustomInfoDataModel.SEPARATOR_BOTTOM

        topSeparator.showWithCondition(shouldShow = shouldTopSeparatorShow)
        bottomSeparator.showWithCondition(shouldShow = shouldBottomSeparatorShow)
    }

    /**
     * custom margin widget
     */
    private fun setWidgetMargin(element: ProductCustomInfoDataModel) {
        // description label
        if (element.title.isEmpty() && element.icon.isEmpty()) {
            // avoid broken with `see` label
            binding.customDesc.setMargin(0, 16.toPx(), 32.toPx(), 0)
        } else {
            // avoid broken with `see` label
            binding.customDesc.setMargin(0, 12.toPx(), 0, 0)
        }

        // adjust title margin between icon and title when icon is empty,
        if (element.icon.isEmpty()) {
            binding.customTitle.setMargin(
                left = 0,
                top = 16.toPx(),
                right = 0,
                bottom = 0
            )
        } else {
            binding.customTitle.setMargin(
                left = 8.toPx(),
                top = 16.toPx(),
                right = 0,
                bottom = 0
            )
        }
    }

    private fun getComponentTrackData(
        element: ProductCustomInfoDataModel?
    ) = ComponentTrackDataModel(
        componentType = element?.type.orEmpty(),
        componentName = element?.name.orEmpty(),
        adapterPosition = adapterPosition + 1
    )

}