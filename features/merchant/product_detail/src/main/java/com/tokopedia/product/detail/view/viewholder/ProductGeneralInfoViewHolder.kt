package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup.LayoutParams
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicGeneralInfoBinding
import com.tokopedia.product.detail.databinding.ItemDynamicInfoContentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.utils.resources.isDarkMode

/**
 * ViewHolder is used to display information whose data source is from p1 and p2
 * component-type: info
 * component-name:
 *     - wholesale
 *     - shipping
 *     - tradein
 *     - protection
 *     - order_prio
 *     - byme
 *     - installment_paylater
 */
class ProductGeneralInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.item_dynamic_general_info
    }

    private val binding = ItemDynamicGeneralInfoBinding.bind(view)
    private val contentBinding by lazyThreadSafetyNone {
        ItemDynamicInfoContentBinding.bind(binding.vsGeneralInfo.inflate())
    }

    override fun bind(element: ProductGeneralInfoDataModel) = with(binding) {
        if (element.shouldRenderContent) {
            root.setLayoutHeight(LayoutParams.WRAP_CONTENT)
            contentBinding.renderContent(element = element)
        } else {
            root.setLayoutHeight(0)
        }
    }

    private fun ItemDynamicInfoContentBinding.renderContent(element: ProductGeneralInfoDataModel) {
        impressComponent(element = element)

        setWidgetContent(element = element)

        setupAppLink(element = element)
    }

    private fun ItemDynamicInfoContentBinding.impressComponent(element: ProductGeneralInfoDataModel) {
        root.addOnImpressionListener(holder = element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    /**
     * show label `see` and set event click if appLink is not empty
     */
    private fun ItemDynamicInfoContentBinding.setupAppLink(element: ProductGeneralInfoDataModel) {
        infoSeeMore.showIfWithBlock(predicate = element.isApplink) {
            setOnClickListener {
                listener.onInfoClicked(
                    appLink = element.applink,
                    name = element.name,
                    componentTrackDataModel = getComponentTrackData(element)
                )
            }
        }
    }

    /**
     * render widget content with condition
     */
    private fun ItemDynamicInfoContentBinding.setWidgetContent(element: ProductGeneralInfoDataModel) {
        renderHeader(element = element)

        renderDescription(element = element)

        // general info not need the label
        infoLabel.gone()

        renderSeparator(element = element)
    }

    private fun ItemDynamicInfoContentBinding.renderHeader(element: ProductGeneralInfoDataModel) {
        val isDarkModel = root.context.isDarkMode()
        val iconUrl = element.getIconUrl(isDarkModel = isDarkModel)

        infoImage.showIfWithBlock(predicate = iconUrl.isNotBlank()) { loadIcon(iconUrl) }
        infoTitle.showIfWithBlock(predicate = element.title.isNotEmpty()) { text = element.title }
        infoHeaderContainer.showWithCondition(
            shouldShow = infoImage.isVisible || infoTitle.isVisible
        )
    }

    private fun ItemDynamicInfoContentBinding.renderDescription(element: ProductGeneralInfoDataModel) {
        infoDescription.showIfWithBlock(predicate = element.subtitle.isNotEmpty()) {
            text = element.subtitle.parseAsHtmlLink(
                context = root.context,
                replaceNewLine = false
            )
        }
    }

    private fun ItemDynamicInfoContentBinding.renderSeparator(
        element: ProductGeneralInfoDataModel
    ) {
        infoTopSeparator.showWithCondition(shouldShow = element.shouldTopSeparatorShowing)
        infoBottomSeparator.showWithCondition(shouldShow = element.shouldBottomSeparatorShowing)
    }
}
