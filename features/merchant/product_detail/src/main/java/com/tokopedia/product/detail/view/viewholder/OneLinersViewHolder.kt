package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.OneLinersContent
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_BOTH
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_BOTTOM
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_TOP
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.STOCK_ASSURANCE
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.viewutil.convertDpToPixel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.stringToUnifyColor

class OneLinersViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<OneLinersDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_one_liners

        private const val BOTTOM_PADDING = 12f
    }

    private val container: View? = view.findViewById(R.id.one_liners_container)
    private val iconStart: ImageUnify? = view.findViewById(R.id.one_liners_icon_start)
    private val title: Typography? = view.findViewById(R.id.one_liners_title)
    private val description: Typography? = view.findViewById(R.id.one_liners_description)
    private val iconRightArrow: IconUnify? = view.findViewById(R.id.one_liners_icon_right)
    private val separatorTop: View? = view.findViewById(R.id.one_liners_top_separator)
    private val separatorBottom: View? = view.findViewById(R.id.one_liners_bottom_separator)

    override fun bind(element: OneLinersDataModel) {
        val content = element.oneLinersContent
        if (content == null || !content.isVisible) {
            itemView.layoutParams.height = 0
            return
        } else itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        renderViewImpression(element = element)

        renderViewEvent(element = element)

        renderSeparators(content = content)

        renderText(content = content)

        renderIconStart(content = content)

        renderIconEnd(content = content)

        if (element.name == ProductDetailConstant.BEST_SELLER) {
            renderBestSellerView(element)
        }
    }

    private fun renderViewImpression(element: OneLinersDataModel) {
        val content = element.oneLinersContent

        view.apply {
            addOnImpressionListener(element.impressHolder) {
                renderCoachMark(content?.eduLink?.appLink?.isNotBlank() == true)

                if (element.name == STOCK_ASSURANCE)
                    listener.onImpressStockAssurance(
                        componentTrackDataModel = getComponentTrackData(element),
                        label = content?.linkText + content?.content
                    )
                else listener.onImpressComponent(getComponentTrackData(element))
            }
        }
    }

    private fun renderViewEvent(element: OneLinersDataModel) {
        val content = element.oneLinersContent ?: return

        if (content.applink.isNotBlank()) {
            view.setOnClickListener { listener.goToApplink(content.applink) }
        }

        if (content.eduLink.appLink.isNotBlank()) {
            iconRightArrow?.setOnClickListener {
                listener.onClickInformationIconAtStockAssurance(
                    componentTrackDataModel = getComponentTrackData(element = element),
                    appLink = content.eduLink.appLink,
                    label = content.linkText + content.linkText
                )
            }
        }
    }

    private fun renderCoachMark(shouldShowCoachmark: Boolean) {
        if (shouldShowCoachmark) {
            iconStart?.let {
                listener.showOneLinersImsCoachMark(it)
            }
        }
    }

    private fun renderBestSellerView(element: OneLinersDataModel) {
        val appLink = element.oneLinersContent?.applink.orEmpty()

        container?.apply {
            val dp12 = convertDpToPixel(BOTTOM_PADDING, context)
            setPadding(paddingLeft, 0, paddingRight, dp12)
        }

        if (appLink.isNotBlank()) {
            view.setOnClickListener {
                listener.onClickBestSeller(
                    getComponentTrackData(element),
                    appLink
                )
            }
        }
        title?.setWeight(Typography.BOLD)
    }

    private fun renderText(content: OneLinersContent) {
        title?.apply {
            text = content.linkText

            runCatching {
                val unifyColor = stringToUnifyColor(context, content.color)
                setTextColor(unifyColor.unifyColor ?: unifyColor.defaultColor)
            }
        }

        description?.text = content.content
    }

    private fun renderIconStart(content: OneLinersContent) {
        val iconUrl = content.icon
        iconStart?.showIfWithBlock(iconUrl.isNotBlank()) {
            setImageUrl(iconUrl)
        }
    }

    private fun renderIconEnd(content: OneLinersContent) {
        // app-link field is existing flag for stock info
        // and edu-link field is new flag for stock should available
        val eduLinkIsNotEmpty = content.eduLink.appLink.isNotBlank()
        val appLinkIsNotEmpty = content.applink.isNotBlank()
        val shouldShow = appLinkIsNotEmpty || eduLinkIsNotEmpty

        iconRightArrow?.showIfWithBlock(shouldShow) {
            if (eduLinkIsNotEmpty) {
                setImage(newIconId = IconUnify.INFORMATION)
            } else if (appLinkIsNotEmpty) {
                setImage(newIconId = IconUnify.CHEVRON_RIGHT)
            }
        }
    }

    private fun renderSeparators(content: OneLinersContent) {
        val separator = content.separator
        separatorTop?.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_TOP)
        separatorBottom?.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_BOTTOM)
    }

    private fun getComponentTrackData(element: OneLinersDataModel?) = ComponentTrackDataModel(
        componentType = element?.type.orEmpty(),
        componentName = element?.name.orEmpty(),
        adapterPosition = bindingAdapterPosition + Int.ONE
    )
}
