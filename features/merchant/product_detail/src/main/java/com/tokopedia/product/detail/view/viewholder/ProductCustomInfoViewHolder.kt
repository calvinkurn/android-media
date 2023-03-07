package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup.LayoutParams
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicCustomInfoBinding
import com.tokopedia.product.detail.databinding.ItemDynamicInfoContentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

/**
 * Created by Yehezkiel on 13/08/20
 * ViewHolder is used to display information whose data source is from p1
 * component-type: custom_info
 * component-name:
 *     - palugada_hampers
 *     - info_donation
 *     - restricted_categories
 *     - tokonow_usp
 *     - palugada
 *     - obat_keras
 *     - pet_disclaimer
 *     - imei
 */
class ProductCustomInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductCustomInfoDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.item_dynamic_custom_info
    }

    private val binding = ItemDynamicCustomInfoBinding.bind(view)
    private val contentBinding by lazyThreadSafetyNone {
        ItemDynamicInfoContentBinding.bind(binding.vsCustomInfo.inflate())
    }

    override fun bind(element: ProductCustomInfoDataModel) = with(binding) {
        if (element.shouldRenderContent) {
            root.setLayoutHeight(LayoutParams.WRAP_CONTENT)
            contentBinding.renderContent(element = element)
        } else {
            root.setLayoutHeight(0)
        }
    }

    /**
     * show or not [ProductCustomInfoViewHolder] view
     */
    private fun ItemDynamicInfoContentBinding.renderContent(element: ProductCustomInfoDataModel) {
        impressComponent(element = element)

        setWidgetContent(element = element)

        setupAppLink(element = element)
    }


    private fun ItemDynamicInfoContentBinding.impressComponent(element: ProductCustomInfoDataModel) {
        root.addOnImpressionListener(holder = element.impressHolder) {
            val componentTrack = getComponentTrackData(element)
            listener.onImpressComponent(componentTrack)
        }
    }

    /**
     * show label `see` and set event click if appLink is not empty
     */
    private fun ItemDynamicInfoContentBinding.setupAppLink(element: ProductCustomInfoDataModel) {
        infoSeeMore.showIfWithBlock(predicate = element.applink.isNotBlank()) {
            setOnClickListener {
                listener.onBbiInfoClick(
                    url = element.applink,
                    title = element.title,
                    componentTrackDataModel = getComponentTrackData(element = element)
                )
            }
        }
    }

    /**
     * render widget content with condition
     */
    private fun ItemDynamicInfoContentBinding.setWidgetContent(element: ProductCustomInfoDataModel) {
        renderHeader(element = element)

        renderDescription(element = element)

        renderLabel(element = element)

        renderSeparator(element = element)
    }

    private fun ItemDynamicInfoContentBinding.renderHeader(element: ProductCustomInfoDataModel) {
        val isDarkModel = root.context.isDarkMode()
        val iconUrl = element.getIconUrl(isDarkModel = isDarkModel)

        infoImage.showIfWithBlock(predicate = iconUrl.isNotBlank()) { loadIcon(iconUrl) }
        infoTitle.showIfWithBlock(predicate = element.title.isNotEmpty()) { text = element.title }
        infoHeaderContainer.showWithCondition(
            shouldShow = infoImage.isVisible || infoTitle.isVisible
        )
    }

    private fun ItemDynamicInfoContentBinding.renderDescription(element: ProductCustomInfoDataModel) {
        infoDescription.showIfWithBlock(predicate = element.description.isNotEmpty()) {
            text = element.description.parseAsHtmlLink(
                context = root.context,
                replaceNewLine = false
            )
        }
    }

    private fun ItemDynamicInfoContentBinding.renderSeparator(element: ProductCustomInfoDataModel) {
        infoTopSeparator.showWithCondition(shouldShow = element.shouldTopSeparatorShowing)
        infoBottomSeparator.showWithCondition(shouldShow = element.shouldBottomSeparatorShowing)
    }

    private fun ItemDynamicInfoContentBinding.renderLabel(element: ProductCustomInfoDataModel) {
        infoLabel.showIfWithBlock(predicate = element.labelValue.isNotEmpty()) {
            setLabel(element.labelValue)
            setLabelType(element.getLabelTypeByColor())
        }
    }
}
