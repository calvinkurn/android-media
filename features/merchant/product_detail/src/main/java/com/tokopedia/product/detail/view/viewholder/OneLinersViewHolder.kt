package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup.LayoutParams
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.OneLinersContent
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_BOTH
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_BOTTOM
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_TOP
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.STOCK_ASSURANCE
import com.tokopedia.product.detail.databinding.ItemOneLinersBinding
import com.tokopedia.product.detail.databinding.ItemOneLinersContentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.viewutil.convertDpToPixel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.stringToUnifyColor

class OneLinersViewHolder(
    val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<OneLinersDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_one_liners

        private const val BOTTOM_PADDING = 12f
    }

    private val bindingLayout by lazy {
        ItemOneLinersBinding.bind(view)
    }
    private val bindingContent by lazy {
        ItemOneLinersContentBinding.bind(bindingLayout.vsOneLinersContent.inflate())
    }

    override fun bind(element: OneLinersDataModel) {
        if (element.shouldRenderContent) {
            bindingLayout.root.setLayoutHeight(LayoutParams.WRAP_CONTENT)
            bindingContent.renderContent(element = element)
        } else {
            bindingLayout.root.setLayoutHeight(0)
        }
    }

    private fun ItemOneLinersContentBinding.renderContent(element: OneLinersDataModel) {
        val content = element.oneLinersContent ?: return

        impressComponent(element = element)

        renderViewEvent(element = element)

        renderSeparators(content = content)

        renderText(content = content)

        renderIconStart(content = content)

        renderIconEnd(content = content)

        if (element.name == ProductDetailConstant.BEST_SELLER) {
            renderBestSellerView(element)
        }
    }

    private fun ItemOneLinersContentBinding.impressComponent(element: OneLinersDataModel) {
        val content = element.oneLinersContent

        root.addOnImpressionListener(element.impressHolder) {
            renderCoachMark(content?.eduLink?.appLink?.isNotBlank() == true)

            if (element.name == STOCK_ASSURANCE)
                listener.onImpressStockAssurance(
                    componentTrackDataModel = getComponentTrackData(element),
                    label = content?.linkText + content?.content
                )
            else listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun ItemOneLinersContentBinding.renderViewEvent(element: OneLinersDataModel) {
        val content = element.oneLinersContent ?: return

        if (content.applink.isNotBlank()) {
            root.setOnClickListener { listener.goToApplink(content.applink) }
        }

        if (content.eduLink.appLink.isNotBlank()) {
            oneLinersIconRight.setOnClickListener {
                listener.onClickInformationIconAtStockAssurance(
                    componentTrackDataModel = getComponentTrackData(element = element),
                    appLink = content.eduLink.appLink,
                    label = content.linkText + content.linkText
                )
            }
        }
    }

    private fun ItemOneLinersContentBinding.renderCoachMark(shouldShowCoachmark: Boolean) {
        if (shouldShowCoachmark) {
            listener.showOneLinersImsCoachMark(oneLinersIconStart)
        }
    }

    private fun ItemOneLinersContentBinding.renderBestSellerView(element: OneLinersDataModel) {
        val appLink = element.oneLinersContent?.applink.orEmpty()

        oneLinersContainer.apply {
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
        oneLinersTitle.setWeight(Typography.BOLD)
    }

    private fun ItemOneLinersContentBinding.renderText(content: OneLinersContent) {
        oneLinersTitle.apply {
            text = content.linkText

            runCatching {
                val unifyColor = stringToUnifyColor(context, content.color)
                setTextColor(unifyColor.unifyColor ?: unifyColor.defaultColor)
            }
        }

        oneLinersDescription.text = content.content
    }

    private fun ItemOneLinersContentBinding.renderIconStart(content: OneLinersContent) {
        val iconUrl = content.icon
        oneLinersIconStart.showIfWithBlock(iconUrl.isNotBlank()) {
            setImageUrl(iconUrl)
        }
    }

    private fun ItemOneLinersContentBinding.renderIconEnd(content: OneLinersContent) {
        // app-link field is existing flag for stock info
        // and edu-link field is new flag for stock should available
        val eduLinkIsNotEmpty = content.eduLink.appLink.isNotBlank()
        val appLinkIsNotEmpty = content.applink.isNotBlank()
        val shouldShow = appLinkIsNotEmpty || eduLinkIsNotEmpty

        oneLinersIconRight.showIfWithBlock(shouldShow) {
            if (eduLinkIsNotEmpty) {
                setImage(newIconId = IconUnify.INFORMATION)
            } else if (appLinkIsNotEmpty) {
                setImage(newIconId = IconUnify.CHEVRON_RIGHT)
            }
        }
    }

    private fun ItemOneLinersContentBinding.renderSeparators(content: OneLinersContent) {
        val separator = content.separator
        oneLinersTopSeparator.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_TOP)
        oneLinersBottomSeparator.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_BOTTOM)
    }
}
