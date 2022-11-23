package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicCustomInfoBinding
import com.tokopedia.product.detail.databinding.ItemDynamicGeneralInfoBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.utils.resources.isDarkMode

class ProductGeneralInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.item_dynamic_general_info
    }

    private val binding = ItemDynamicGeneralInfoBinding.bind(view)
    private val contentBinding by lazy { binding.generalInfoLayout }

    override fun bind(element: ProductGeneralInfoDataModel) = with(contentBinding) {
        if (element.shouldRenderContent) {
            renderContent(element = element)
        } else {
            hideContent()
        }
    }

    private fun ItemDynamicCustomInfoBinding.hideContent() {
        infoTopSeparator.hide()
        infoBottomSeparator.hide()
        binding.setContentHeight(height = 0)
    }

    private fun ItemDynamicGeneralInfoBinding.setContentHeight(height: Int) {
        if (root.layoutParams.height == height) return

        root.layoutParams.height = height
    }

    private fun ItemDynamicCustomInfoBinding.renderContent(element: ProductGeneralInfoDataModel) {
        binding.setContentHeight(height = ViewGroup.LayoutParams.WRAP_CONTENT)

        impressComponent(element = element)

        setWidgetContent(element = element)

        setupAppLink(element = element)
    }

    private fun impressComponent(element: ProductGeneralInfoDataModel) {
        view.addOnImpressionListener(holder = element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    /**
     * show label `see` and set event click if appLink is not empty
     */
    private fun ItemDynamicCustomInfoBinding.setupAppLink(element: ProductGeneralInfoDataModel) {
        infoSeeMore.showIfWithBlock(predicate = element.applink.isNotBlank()) {
            listener.onInfoClicked(
                appLink = element.applink,
                name = element.name,
                componentTrackDataModel = getComponentTrackData(element)
            )
        }
    }

    /**
     * render widget content with condition
     */
    private fun ItemDynamicCustomInfoBinding.setWidgetContent(element: ProductGeneralInfoDataModel) {
        val isDarkModel = binding.root.context.isDarkMode()
        val iconUrl = element.getIconUrl(isDarkModel = isDarkModel)

        infoImage.showIfWithBlock(predicate = iconUrl.isNotBlank()) { loadIcon(iconUrl) }

        infoTitle.showIfWithBlock(predicate = element.title.isNotEmpty()) { text = element.title }

        infoHeaderContainer.showWithCondition(
            shouldShow = infoImage.isVisible || infoTitle.isVisible
        )

        infoDescription.showIfWithBlock(predicate = element.subtitle.isNotEmpty()) {
            text = element.subtitle.parseAsHtmlLink(
                context = root.context,
                replaceNewLine = false
            )
        }

        infoLabel.gone()

        infoTopSeparator.showWithCondition(shouldShow = element.shouldTopSeparatorShowing)
        infoBottomSeparator.showWithCondition(shouldShow = element.shouldBottomSeparatorShowing)
    }
}
