package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
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
import com.tokopedia.utils.resources.isDarkMode

class ProductGeneralInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info

        // add space between info description right and `see` label when title and icon is empty
        private const val DESCRIPTION_MARGIN_RIGHT_WHEN_TITLE_ICON_IS_HIDE = 38

        // add space between info description top and parent when title and icon is empty
        private const val DESCRIPTION_MARGIN_TOP_WHEN_TITLE_ICON_IS_HIDE = 18

        // add space between info description top and parent when title and icon is empty
        private const val DESCRIPTION_MARGIN_TOP_WHEN_TITLE_ICON_IS_SHOW = 12

        // add space between info description top and parent when title and icon is empty
        private const val TITLE_MARGIN_LEFT_WHEN_ICON_IS_SHOW = 8
    }

    private val binding = ItemDynamicGeneralInfoBinding.bind(view)

    override fun bind(element: ProductGeneralInfoDataModel) {
        val shouldRenderView = shouldRenderView(element = element)

        renderView(shouldRender = shouldRenderView)

        if (shouldRenderView) {
            setEvent(element = element)
            renderContent(element = element)
            setWidgetContent(element = element)
            setWidgetMargin(element = element)
        }
    }

    private fun shouldRenderView(element: ProductGeneralInfoDataModel): Boolean {
        return !element.isPlaceholder && (element.subtitle.isNotEmpty() || element.title.isNotEmpty())
    }

    /**
     * set event to each widget
     */
    private fun setEvent(element: ProductGeneralInfoDataModel) {
        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }

        view.setOnClickListener {
            if (!element.isApplink) return@setOnClickListener
            listener.onInfoClicked(element.applink, element.name, getComponentTrackData(element))
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
     * render content each widget
     */
    private fun renderContent(
        element: ProductGeneralInfoDataModel
    ) = with(binding) {
        pdpSee.showWithCondition(element.isApplink)

        val icon = element.getIconUrl(isDarkModel = binding.root.context.isDarkMode())
        pdpIcon.shouldShowWithAction(icon.isNotEmpty()) {
            pdpIcon.loadIcon(icon)
        }

        pdpInfoTitle.showWithCondition(element.title.isNotEmpty())

        pdpInfoDesc.showWithCondition(element.subtitle.isNotEmpty())
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
        // when title and parent icon is not available
        if (element.title.isEmpty() && element.parentIcon.isEmpty()) {
            // when appLink is available & partner logo is available
            if (element.applink.isNotBlank() && element.additionalIcon.isNotBlank()) {
                // set description margin top to parent for align with appLink label
                binding.pdpInfoDesc.setMargin(
                    left = Int.ZERO,
                    top = DESCRIPTION_MARGIN_TOP_WHEN_TITLE_ICON_IS_HIDE.toPx(),
                    right = Int.ZERO,
                    bottom = Int.ZERO
                )
            } else if (element.applink.isNotBlank()) { // when appLink is available & partner logo is NOT available
                // set description margin right to parent with 0dp value
                binding.pdpInfoDesc.setMargin(
                    left = Int.ZERO,
                    top = DESCRIPTION_MARGIN_TOP_WHEN_TITLE_ICON_IS_HIDE.toPx(),
                    right = DESCRIPTION_MARGIN_RIGHT_WHEN_TITLE_ICON_IS_HIDE.toPx(),
                    bottom = Int.ZERO
                )
            } else { // when appLink is not available & partner logo is NOT available
                binding.pdpInfoDesc.setMargin(
                    left = Int.ZERO,
                    top = DESCRIPTION_MARGIN_TOP_WHEN_TITLE_ICON_IS_HIDE.toPx(),
                    right = Int.ZERO,
                    bottom = Int.ZERO
                )
            }
        } else {
            // set description margin by default
            binding.pdpInfoDesc.setMargin(
                left = Int.ZERO,
                top = DESCRIPTION_MARGIN_TOP_WHEN_TITLE_ICON_IS_SHOW.toPx(),
                right = Int.ZERO,
                bottom = Int.ZERO
            )
        }

        // adjust title margin between icon and title when icon is empty,
        if (element.parentIcon.isEmpty()) {
            binding.pdpInfoTitle.setMargin(
                left = Int.ZERO,
                top = Int.ZERO,
                right = Int.ZERO,
                bottom = Int.ZERO
            )
        } else {
            // set margin default
            binding.pdpInfoTitle.setMargin(
                left = TITLE_MARGIN_LEFT_WHEN_ICON_IS_SHOW.toPx(),
                top = Int.ZERO,
                right = Int.ZERO,
                bottom = Int.ZERO
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
