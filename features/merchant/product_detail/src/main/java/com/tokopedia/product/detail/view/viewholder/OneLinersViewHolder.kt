package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
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
    private val coachMarkIms by lazy(LazyThreadSafetyMode.NONE) { CoachMark2(view.context) }

    override fun bind(element: OneLinersDataModel) {
        val content = element.oneLinersContent
        if (content == null || !content.isVisible) {
            itemView.layoutParams.height = 0
            return
        } else itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        renderViewImpression(element = element)

        renderViewEvent(content = content)

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
                if (element.name == STOCK_ASSURANCE)
                    listener.onImpressStockAssurance(
                        componentTrackDataModel = getComponentTrackData(element),
                        label = content?.linkText + content?.content
                    )
                else listener.onImpressComponent(getComponentTrackData(element))
            }
        }
    }

    private fun renderViewEvent(content: OneLinersContent) {
        if (content.applink.isNotBlank()) {
            view.setOnClickListener { listener.goToApplink(content.applink) }
        } else if (content.eduLink.appLink.isNotBlank()) {
            view.setOnClickListener { listener.goToApplink(content.eduLink.appLink) }
            renderCoachMark()
        }
    }

    private fun renderCoachMark() {
        iconStart?.let {
            if (!coachMarkIms.isShowing) {
                val item = CoachMark2Item(
                    anchorView = it,
                    title = view.context.getString(R.string.pdp_oneliners_ims100_coachmark_title),
                    description = view.context.getString(R.string.pdp_oneliners_ims100_coachmark_description),
                    position = CoachMark2.POSITION_BOTTOM
                )
                coachMarkIms.showCoachMark(arrayListOf(item), null, 0)
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
        iconStart?.apply {
            showWithCondition(iconUrl.isNotBlank())
            setImageUrl(iconUrl)
        }
    }

    private fun renderIconEnd(content: OneLinersContent) {
        // app-link is existing flag for stock info
        // otherwise edu-link is new flag for stock should available always
        val shouldShow = content.applink.isNotBlank() || content.eduLink.isNotEmpty()

        iconRightArrow?.shouldShowWithAction(shouldShow = shouldShow) {
            if (content.applink.isNotBlank()) {
                iconRightArrow.setImage(newIconId = IconUnify.CHEVRON_RIGHT)
            } else if (content.eduLink.isNotEmpty()) {
                iconRightArrow.setImage(newIconId = IconUnify.INFORMATION)
            }
        }
    }

    private fun renderSeparators(content: OneLinersContent) {
        val separator = content.separator
        separatorTop?.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_TOP)
        separatorBottom?.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_BOTTOM)
    }

    private fun getComponentTrackData(element: OneLinersDataModel?) = ComponentTrackDataModel(
        componentType = element?.type ?: "",
        componentName = element?.name ?: "",
        adapterPosition = bindingAdapterPosition + Int.ONE
    )

}
