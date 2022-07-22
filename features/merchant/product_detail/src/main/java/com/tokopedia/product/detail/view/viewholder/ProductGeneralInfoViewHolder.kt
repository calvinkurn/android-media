package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicGeneralInfoBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductGeneralInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info
    }

    private val binding = ItemDynamicGeneralInfoBinding.bind(view)

    override fun bind(element: ProductGeneralInfoDataModel) {
        val viewWillBeRendered = element.subtitle.isNotEmpty()

        // render layout view
        renderView(viewWillBeRendered)

        // render content view
        if (viewWillBeRendered) {
            // set event
            setEvent(element)

            // render icon
            renderIcon(element)

            // set text to widget
            setContentWidget(element)

            // set widget visibility
            setWidgetVisibility(element)
        }
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
            listener.onInfoClicked(element.applink, element.name, getComponentTrackData(element))
        }
    }

    /**
     * set text to each widget
     */
    private fun setContentWidget(element: ProductGeneralInfoDataModel) {
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
    private fun renderView(willRender: Boolean) = with(binding) {
        if (willRender) {
            infoSeparator.show()
            generalInfoContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            infoSeparator.gone()
            generalInfoContainer.layoutParams.height = 0
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