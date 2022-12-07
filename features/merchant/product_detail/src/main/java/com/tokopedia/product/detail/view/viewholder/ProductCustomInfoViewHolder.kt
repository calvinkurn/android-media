package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicCustomInfoBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

/**
 * Created by Yehezkiel on 13/08/20
 */
class ProductCustomInfoViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductCustomInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_custom_info

        // added space between description with parent layout when icon and title hide
        private const val DESCRIPTION_MARGIN_TOP_WHEN_ICON_TITLE_EMPTY = 18

        // added space between description with title when icon and title show, same as xml value
        private const val DESCRIPTION_MARGIN_TOP_DEFAULT = 12

        // added space between description with parent layout when icon and title hide
        // so as not to crash with `see` label
        private const val DESCRIPTION_MARGIN_RIGHT_WHEN_ICON_TITLE_EMPTY = 32

        // default margin top value same as xml value between title with parent layout
        private const val TITLE_MARGIN_TOP_DEFAULT = 16

        // added space between title to icon when icon is show
        private const val TITLE_MARGIN_LEFT_WHEN_ICON_SHOW = 8

        // default margin top value same as xml value between custom info with description label
        private const val CUSTOM_INFO_MARGIN_TOP_DEFAULT = 8
        // added space between custom info with parent layout when title, icon, and desc is hide
        private const val CUSTOM_INFO_MARGIN_TOP_WHEN_TITLE_ICON_DESC_IS_HIDE = 16
    }

    private val binding = ItemDynamicCustomInfoBinding.bind(view)

    override fun bind(element: ProductCustomInfoDataModel) {
        val shouldRenderView = shouldRenderView(element = element)

        renderView(shouldRender = shouldRenderView)

        if (shouldRenderView) {
            impressComponent(element)

            setWidgetMargin(element)

            setWidgetContent(element)

            setupAppLink(element)
        }
    }

    /**
     * get view visibility condition
     */
    private fun shouldRenderView(element: ProductCustomInfoDataModel): Boolean {
        return element.title.isNotEmpty()
                || element.description.isNotEmpty()
                || element.labelValue.isNotEmpty()
    }

    /**
     * show or not [ProductCustomInfoViewHolder] view
     */
    private fun renderView(shouldRender: Boolean) = with(binding) {
        if (shouldRender) {
            topSeparator.show()
            bottomSeparator.show()
            customLayoutContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            topSeparator.hide()
            bottomSeparator.hide()
            customLayoutContainer.layoutParams.height = 0
        }
    }

    private fun impressComponent(element: ProductCustomInfoDataModel) {
        if (element.title.isNotEmpty() && element.icon.isNotEmpty()) {
            val componentTrack = getComponentTrackData(element)
            itemView.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(componentTrack)
            }
        }
    }

    /**
     * show label `see` and set event click if appLink is not empty
     */
    private fun setupAppLink(
        element: ProductCustomInfoDataModel
    ) = with(binding) {

        if (element.applink.isNotEmpty()) {
            customLabelSee.show()

            val trackData = getComponentTrackData(element = element)

            view.setOnClickListener {
                listener.onBbiInfoClick(
                    url = element.applink,
                    title = element.title,
                    componentTrackDataModel = trackData
                )
            }
        } else {
            customLabelSee.hide()
            view.setOnClickListener {}
        }
    }

    /**
     * render widget and set content with condition
     */
    private fun setWidgetContent(element: ProductCustomInfoDataModel) = with(binding) {
        val isDarkModel = binding.root.context.isDarkMode()
        val iconUrl = element.getIconUrl(isDarkModel = isDarkModel)

        customImage.shouldShowWithAction(iconUrl.isNotBlank()) {
            customImage.loadIcon(iconUrl)
        }

        customDesc.shouldShowWithAction(element.description.isNotEmpty()) {
            customDesc.text = HtmlLinkHelper(view.context, element.description).spannedString
        }

        customTitle.shouldShowWithAction(element.title.isNotEmpty()) {
            customTitle.text = element.title
        }

        labelCustomInfo.shouldShowWithAction(element.labelValue.isNotEmpty()) {
            labelCustomInfo.setLabel(element.labelValue)
            labelCustomInfo.setLabelType(element.getLabelTypeByColor())
        }

        // separator
        renderSeparator(element.separator)
    }

    /**
     * show top and bottom separator with condition
     */
    private fun renderSeparator(separator: String) = with(binding) {
        val shouldTopSeparatorShow = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH
                || separator == ProductCustomInfoDataModel.SEPARATOR_TOP
        val shouldBottomSeparatorShow = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH
                || separator == ProductCustomInfoDataModel.SEPARATOR_BOTTOM

        topSeparator.showWithCondition(shouldShow = shouldTopSeparatorShow)
        bottomSeparator.showWithCondition(shouldShow = shouldBottomSeparatorShow)
    }

    /**
     * custom margin widget
     */
    private fun setWidgetMargin(element: ProductCustomInfoDataModel) = with(binding) {
        // description label
        if (element.title.isEmpty() && element.icon.isEmpty()) {
            // avoid broken with `see` label
            if (element.applink.isNotBlank()) {
                customDesc.setMargin(
                    left = Int.ZERO,
                    top = DESCRIPTION_MARGIN_TOP_WHEN_ICON_TITLE_EMPTY.toPx(),
                    right = DESCRIPTION_MARGIN_RIGHT_WHEN_ICON_TITLE_EMPTY.toPx(),
                    bottom = Int.ZERO
                )
            } else {
                customDesc.setMargin(
                    left = Int.ZERO,
                    top = DESCRIPTION_MARGIN_TOP_WHEN_ICON_TITLE_EMPTY.toPx(),
                    right = Int.ZERO,
                    bottom = Int.ZERO
                )
            }
        } else {
            // avoid broken with `see` label
            customDesc.setMargin(
                left = Int.ZERO,
                top = DESCRIPTION_MARGIN_TOP_DEFAULT.toPx(),
                right = Int.ZERO,
                bottom = Int.ZERO
            )
        }

        // adjust title margin start between icon and title when icon is empty,
        if (element.icon.isEmpty()) {
            customTitle.setMargin(
                left = Int.ZERO,
                top = TITLE_MARGIN_TOP_DEFAULT.toPx(),
                right = Int.ZERO,
                bottom = Int.ZERO
            )
        } else {
            customTitle.setMargin(
                left = TITLE_MARGIN_LEFT_WHEN_ICON_SHOW.toPx(),
                top = TITLE_MARGIN_TOP_DEFAULT.toPx(),
                right = Int.ZERO,
                bottom = Int.ZERO
            )
        }

        // adjust label margin top when title, icon and description is empty
        if (element.title.isEmpty() && element.icon.isEmpty() && element.description.isEmpty()) {
            labelCustomInfo.setMargin(
                left = Int.ZERO,
                top = CUSTOM_INFO_MARGIN_TOP_WHEN_TITLE_ICON_DESC_IS_HIDE.toPx(),
                right = Int.ZERO,
                bottom = Int.ZERO
            )
        } else {
            labelCustomInfo.setMargin(
                left = Int.ZERO,
                top = CUSTOM_INFO_MARGIN_TOP_DEFAULT.toPx(),
                right = Int.ZERO,
                bottom = Int.ZERO
            )
        }
    }

    private fun getComponentTrackData(
        element: ProductCustomInfoDataModel?
    ) = ComponentTrackDataModel(
        componentType = element?.type.orEmpty(),
        componentName = element?.name.orEmpty(),
        adapterPosition = adapterPosition + 1
    )

}