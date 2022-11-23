package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicCustomInfoBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

/**
 * Created by Yehezkiel on 13/08/20
 */
class ProductCustomInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductCustomInfoDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.item_dynamic_custom_info
    }

    private val binding = ItemDynamicCustomInfoBinding.bind(view)

    override fun bind(element: ProductCustomInfoDataModel) = with(binding) {
        if (element.shouldRenderContent) {
            renderContent(element = element)
        } else {
            hideContent()
        }
    }

    private fun ItemDynamicCustomInfoBinding.hideContent() {
        infoTopSeparator.hide()
        infoBottomSeparator.hide()
        setContentHeight(height = 0)
    }

    private fun ItemDynamicCustomInfoBinding.setContentHeight(height: Int) {
        if (root.layoutParams.height == height) return

        root.layoutParams.height = height
    }

    /**
     * show or not [ProductCustomInfoViewHolder] view
     */
    private fun ItemDynamicCustomInfoBinding.renderContent(element: ProductCustomInfoDataModel) {
        setContentHeight(height = ViewGroup.LayoutParams.WRAP_CONTENT)

        impressComponent(element)

        setWidgetContent(element)

        setupAppLink(element)
    }


    private fun impressComponent(element: ProductCustomInfoDataModel) {
        itemView.addOnImpressionListener(holder = element.impressHolder) {
            val componentTrack = getComponentTrackData(element)

            listener.onImpressComponent(componentTrack)
            listener.showCustomInfoCoachMark(element.name, binding.infoImage)
        }
    }

    /**
     * show label `see` and set event click if appLink is not empty
     */
    private fun ItemDynamicCustomInfoBinding.setupAppLink(element: ProductCustomInfoDataModel) {
        infoSeeMore.showIfWithBlock(predicate = element.applink.isNotBlank()) {
            root.setOnClickListener {
                val trackData = getComponentTrackData(element = element)

                listener.onBbiInfoClick(
                    url = element.applink,
                    title = element.title,
                    componentTrackDataModel = trackData
                )
            }
        }
    }

    /**
     * render widget content with condition
     */
    private fun ItemDynamicCustomInfoBinding.setWidgetContent(element: ProductCustomInfoDataModel) {
        val isDarkModel = binding.root.context.isDarkMode()
        val iconUrl = element.getIconUrl(isDarkModel = isDarkModel)

        infoImage.showIfWithBlock(predicate = iconUrl.isNotBlank()) { loadIcon(iconUrl) }

        infoTitle.showIfWithBlock(predicate = element.title.isNotEmpty()) { text = element.title }

        infoHeaderContainer.showWithCondition(
            shouldShow = infoImage.isVisible || infoTitle.isVisible
        )

        infoDescription.showIfWithBlock(predicate = element.description.isNotEmpty()) {
            text = element.description.parseAsHtmlLink(
                context = root.context,
                replaceNewLine = false
            )
        }

        infoLabel.showIfWithBlock(predicate = element.labelValue.isNotEmpty()) {
            setLabel(element.labelValue)
            setLabelType(element.getLabelTypeByColor())
        }

        infoTopSeparator.showWithCondition(shouldShow = element.shouldTopSeparatorShowing)
        infoBottomSeparator.showWithCondition(shouldShow = element.shouldBottomSeparatorShowing)
    }
}
