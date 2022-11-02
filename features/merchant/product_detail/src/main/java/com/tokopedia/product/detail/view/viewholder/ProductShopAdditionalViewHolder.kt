package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.data.model.datamodel.ProductShopAdditionalDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicShopAdditionalBinding
import com.tokopedia.product.detail.databinding.ItemDynamicShopAdditionalContentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.Label

/**
 * Created by yovi.putra on 17/10/22"
 * Project name: android-tokopedia-core
 **/

class ProductShopAdditionalViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductShopAdditionalDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shop_additional
    }

    private val binding = ItemDynamicShopAdditionalBinding.bind(view)

    private val context get() = binding.root.context

    private val contentBinding by lazy(LazyThreadSafetyMode.NONE) {
        ItemDynamicShopAdditionalContentBinding.bind(
            binding.shopAdditionalShimmerLayoutStub.inflate()
        )
    }

    override fun bind(element: ProductShopAdditionalDataModel) {
        if (!element.isLoading) {
            prepareRenderContent()
            impressComponent(element = element)
            setWidgetContent(element = element)
            setupAppLink(element = element)
        }
    }

    private fun prepareRenderContent() = with(binding) {
        shopAdditionalShimmerLoader.root.gone()
    }

    private fun impressComponent(element: ProductShopAdditionalDataModel) {
        val componentTrack = getComponentTrackData(element)
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.onImpressShopAdditional(
                componentTrackDataModel = componentTrack,
                eventLabel = getEventLabel(element = element)
            )
        }
    }

    /**
     * show label `see` and set event click if appLink is not empty
     */
    private fun setupAppLink(
        element: ProductShopAdditionalDataModel
    ) = with(contentBinding) {
        shopAdditionalActionLabel.showIfWithBlock(predicate = element.linkText.isNotBlank()) {
            text = element.linkText

            setOnClickListener {
                listener.onLearnButtonShopAdditionalClicked(
                    componentTrackDataModel = getComponentTrackData(element = element),
                    eventLabel = getEventLabel(element = element)
                )
            }
        }
    }

    /**
     * render widget and set content with condition
     */
    private fun setWidgetContent(element: ProductShopAdditionalDataModel) = with(contentBinding) {
        shopAdditionalImage.showIfWithBlock(predicate = element.icon.isNotBlank()) {
            loadIcon(element.icon)
        }

        shopAdditionalDescription.showIfWithBlock(predicate = element.description.isNotEmpty()) {
            text = element.description.parseAsHtmlLink(
                context = context,
                replaceNewLine = false
            )
        }

        shopAdditionalTitle.showIfWithBlock(predicate = element.title.isNotEmpty()) {
            text = element.title
        }

        shopAdditionalScrollviewLabel.showIfWithBlock(predicate = element.labels.isNotEmpty()) {
            shopAdditionalContainerLabel.removeAllViews()

            element.labels.forEach {
                val view = renderLabels(label = it)
                shopAdditionalContainerLabel.addView(view)
            }
        }
    }

    private fun renderLabels(label: String) = Label(context = context).apply {
        text = label
        setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
    }

    private fun getEventLabel(element: ProductShopAdditionalDataModel): String {
        val labels = element.labels.joinToString(" ")
        return "title:${element.title};subtitle:${element.description};label:$labels;"
    }
}
