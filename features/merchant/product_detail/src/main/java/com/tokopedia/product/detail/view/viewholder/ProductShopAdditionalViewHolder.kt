package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.visible
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
        if (element.isLoading) {
            onLoadingState()
        } else {
            onDataLoaded(element = element)
        }
    }

    private fun onLoadingState() = with(binding) {
        shopAdditionalShimmerLoader.root.visible()
        contentBinding.root.gone()
    }

    private fun onDataLoaded(element: ProductShopAdditionalDataModel) = with(binding) {
        prepareContentView()
        impressComponent(element = element)
        setWidgetContent(element = element)
        setAppLink(element = element)
    }

    private fun prepareContentView() {
        binding.shopAdditionalShimmerLoader.root.gone()
        contentBinding.root.visible()
    }

    private fun impressComponent(element: ProductShopAdditionalDataModel) {
        val componentTrack = getComponentTrackData(element)
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(componentTrackDataModel = componentTrack)
        }
    }

    private fun setAppLink(
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

    private fun setWidgetContent(element: ProductShopAdditionalDataModel) = with(element) {
        renderImage(iconUrl = icon)
        renderTitle(title = title)
        renderDescription(description = description)
        renderLabels(labels = labels)
    }

    private fun renderImage(iconUrl: String) = with(contentBinding) {
        shopAdditionalImage.showIfWithBlock(predicate = iconUrl.isNotBlank()) {
            loadIcon(iconUrl)
        }
    }

    private fun renderTitle(title: String) = with(contentBinding) {
        shopAdditionalTitle.showIfWithBlock(predicate = title.isNotEmpty()) {
            text = title
        }
    }

    private fun renderDescription(description: String) = with(contentBinding) {
        shopAdditionalDescription.showIfWithBlock(predicate = description.isNotEmpty()) {
            text = description.parseAsHtmlLink(
                context = context,
                replaceNewLine = false
            )
        }
    }

    private fun renderLabels(labels: List<String>) = with(contentBinding) {
        shopAdditionalScrollviewLabel.showIfWithBlock(predicate = labels.isNotEmpty()) {
            shopAdditionalContainerLabel.removeAllViews()

            labels.forEach {
                val view = renderLabel(label = it)
                shopAdditionalContainerLabel.addView(view)
            }
        }
    }

    private fun renderLabel(label: String) = Label(context = context).apply {
        text = label
        setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
    }

    private fun getEventLabel(element: ProductShopAdditionalDataModel): String {
        val labels = element.labels.joinToString("+")
        return "title:${element.title};subtitle:${element.description};label:$labels;"
    }
}
