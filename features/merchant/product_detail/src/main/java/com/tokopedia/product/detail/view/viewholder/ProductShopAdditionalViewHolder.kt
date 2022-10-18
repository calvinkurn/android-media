package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopAdditionalDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicShopAdditionalBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.Label

/**
 * Created by yovi.putra on 17/10/22"
 * Project name: android-tokopedia-core
 **/

class ProductShopAdditionalViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductShopAdditionalDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shop_additional
    }

    private val binding = ItemDynamicShopAdditionalBinding.bind(view)

    private val context get() = binding.root.context

    override fun bind(element: ProductShopAdditionalDataModel) {
        val shouldRenderView = shouldRenderView(element = element)

        renderView(shouldRender = shouldRenderView)

        if (shouldRenderView) {
            impressComponent(element)

            setWidgetContent(element)

            setupAppLink(element)
        }
    }

    /**
     * get view visibility condition
     */
    private fun shouldRenderView(element: ProductShopAdditionalDataModel): Boolean {
        return element.title.isNotEmpty() || element.description.isNotEmpty()
    }

    /**
     * show or not [ProductShopAdditionalViewHolder] view
     */
    private fun renderView(shouldRender: Boolean) = with(binding) {
        if (shouldRender) {
            shopAdditionalContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            shopAdditionalContainer.layoutParams.height = 0
        }
    }

    private fun impressComponent(element: ProductShopAdditionalDataModel) {
        /*if (element.title.isNotEmpty() && element.icon.isNotEmpty()) {
            val componentTrack = getComponentTrackData(element)
            itemView.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(componentTrack)
                listener.showCustomInfoCoachMark(element.name, binding.customImage)
            }
        }*/
    }

    /**
     * show label `see` and set event click if appLink is not empty
     */
    private fun setupAppLink(
        element: ProductShopAdditionalDataModel
    ) = with(binding) {
        shopAdditionalActionLabel.shouldShowWithAction(element.linkText.isNotBlank()) {
            shopAdditionalActionLabel.text = element.linkText

            shopAdditionalActionLabel.setOnClickListener {
                listener.onShopAdditionalSeeMore()
            }
        }
    }

    /**
     * render widget and set content with condition
     */
    private fun setWidgetContent(element: ProductShopAdditionalDataModel) = with(binding) {
        shopAdditionalImage.shouldShowWithAction(element.icon.isNotBlank()) {
            shopAdditionalImage.loadIcon(element.icon)
        }

        shopAdditionalDescription.shouldShowWithAction(element.description.isNotEmpty()) {
            shopAdditionalDescription.text = element.description.parseAsHtmlLink(
                context = context,
                replaceNewLine = false
            )
        }

        shopAdditionalTitle.shouldShowWithAction(element.title.isNotEmpty()) {
            shopAdditionalTitle.text = element.title
        }

        shopAdditionalScrollviewLabel.shouldShowWithAction(element.labels.isNotEmpty()) {
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

    private fun getComponentTrackData(
        element: ProductShopAdditionalDataModel?
    ) = ComponentTrackDataModel(
        componentType = element?.type.orEmpty(),
        componentName = element?.name.orEmpty(),
        adapterPosition = bindingAdapterPosition + Int.ONE
    )
}
