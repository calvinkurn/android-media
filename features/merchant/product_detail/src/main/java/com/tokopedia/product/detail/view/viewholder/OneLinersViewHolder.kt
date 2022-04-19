package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_BOTH
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_BOTTOM
import com.tokopedia.product.detail.data.model.datamodel.OneLinersDataModel.Companion.SEPARATOR_TOP
import com.tokopedia.product.detail.data.util.ProductDetailConstant
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
        if (content == null || !content.isVisible){
            itemView.layoutParams.height = 0
            return
        } else itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        val applink = content.applink
        view.apply {
            addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
            if (applink.isNotBlank()) {
                setOnClickListener { listener.goToApplink(applink) }
                iconRightArrow?.visible()
            }
        }

        val separator = content.separator
        separatorTop?.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_TOP)
        separatorBottom?.showWithCondition(separator == SEPARATOR_BOTH || separator == SEPARATOR_BOTTOM)

        title?.apply {
            text = content.linkText

            try {
                val unifyColor = stringToUnifyColor(context, content.color)
                setTextColor(unifyColor.unifyColor ?: unifyColor.defaultColor)

            } catch (ex: RuntimeException) {
                ex.printStackTrace()
            }
        }

        description?.text = content.content

        val iconUrl = content.icon
        iconStart?.apply {
            showWithCondition(iconUrl.isNotBlank())
            setImageUrl(iconUrl)
        }


        if (element.name == ProductDetailConstant.BEST_SELLER) {
            container?.apply {
                val dp12 = convertDpToPixel(BOTTOM_PADDING, context)
                setPadding(paddingLeft, 0, paddingRight, dp12)
            }
            if (applink.isNotBlank()) {
                view.setOnClickListener { listener.onClickBestSeller(getComponentTrackData(element), applink) }
            }
            title?.setWeight(Typography.BOLD)
        }
    }

    private fun getComponentTrackData(element: OneLinersDataModel?) = ComponentTrackDataModel(
        element?.type ?: "",
        element?.name ?: "",
        adapterPosition + 1
    )

}