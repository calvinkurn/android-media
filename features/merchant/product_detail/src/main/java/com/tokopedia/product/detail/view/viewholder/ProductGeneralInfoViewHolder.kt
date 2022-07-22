package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicGeneralInfoBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.toPx

class ProductGeneralInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info
    }

    private val binding = ItemDynamicGeneralInfoBinding.bind(view)

    override fun bind(element: ProductGeneralInfoDataModel) {
        val shouldRenderView = shouldRenderView(element = element)

        // render layout view
        renderView(shouldRender = shouldRenderView)

        // render content view
        if (shouldRenderView) {
            // set event
            setEvent(element = element)

            // render icon
            renderIcon(element = element)

            // set text to widget
            setWidgetContent(element = element)

            // set widget visibility
            setWidgetVisibility(element = element)

            // set widget margin
            setWidgetMargin(element = element)
        }
    }

    private fun shouldRenderView(element: ProductGeneralInfoDataModel): Boolean {
        return element.subtitle.isNotEmpty() || element.title.isNotEmpty()
    }

    /**
     * set event to each widget
     */
    private fun setEvent(element: ProductGeneralInfoDataModel) {
        // tracking impression event
        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
        // set event
        view.setOnClickListener {
            if (element.applink.isNotEmpty()) {
                listener.onInfoClicked(element.applink, element.name, getComponentTrackData(element))
            }
        }
    }

    /**
     * set text to each widget
     */
    private fun setWidgetContent(element: ProductGeneralInfoDataModel) {
        binding.pdpInfoTitle.text = MethodChecker.fromHtml(element.title)
        binding.pdpInfoDesc.text = MethodChecker.fromHtml(element.subtitle)
    }

    /**
     * set widget visibility
     */
    private fun setWidgetVisibility(element: ProductGeneralInfoDataModel) = with(binding) {
        pdpInfoTitle.isVisible = element.title.isNotEmpty()
    }

    /**
     * render all icon to each widget
     */
    private fun renderIcon(
        element: ProductGeneralInfoDataModel,
    ) = with(binding) {

        // label `see`
        pdpSee.showWithCondition(element.isApplink)

        // info icon
        pdpIcon.shouldShowWithAction(element.parentIcon.isNotEmpty()) {
            pdpIcon.loadIcon(element.parentIcon)
        }

        // partner logo
        icPdpPartnerLogo.shouldShowWithAction(element.additionalIcon.isNotEmpty()) {
            icPdpPartnerLogo.loadIcon(element.additionalIcon)
        }
    }

    /**
     * show or not [ProductGeneralInfoViewHolder] view
     */
    private fun renderView(shouldRender: Boolean) = with(binding) {
        if (shouldRender) {
            infoSeparator.show()
            generalInfoContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            infoSeparator.gone()
            generalInfoContainer.layoutParams.height = 0
        }
    }

    /**
     * custom margin widget
     */
    private fun setWidgetMargin(element: ProductGeneralInfoDataModel) {
        // description label
        if (element.title.isEmpty() && element.parentIcon.isEmpty()) {
            // avoid broken with `see` label
            binding.pdpInfoDesc.setMargin(
                left = 0,
                top = 0,
                right = 32.toPx(),
                bottom = 0
            )
        } else {
            // avoid broken with `see` label
            binding.pdpInfoDesc.setMargin(
                left = 0,
                top = 12.toPx(),
                right = 0,
                bottom = 0
            )
        }

        // adjust title margin between icon and title when icon is empty,
        if (element.parentIcon.isEmpty()) {
            binding.pdpInfoTitle.setMargin(
                left = 0,
                top = 0,
                right = 0,
                bottom = 0
            )
        } else {
            binding.pdpInfoTitle.setMargin(
                left = 8.toPx(),
                top = 0,
                right = 0,
                bottom = 0
            )
        }
    }

    /**
     * Create component tracking data
     */
    private fun getComponentTrackData(
        element: ProductGeneralInfoDataModel?
    ) = ComponentTrackDataModel(
        componentType = element?.type.orEmpty(),
        componentName = element?.name.orEmpty(),
        adapterPosition = adapterPosition + 1
    )
}