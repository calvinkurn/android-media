package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicOneLinerDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.ItemDynamicOneLinerBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class DynamicOneLinerViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<DynamicOneLinerDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_one_liner
        private const val STATUS_HIDE = "hide"
        private const val STATUS_SHOW = "show"
        private const val CHEVRON_POS_FOLLOW = "follow_text"
        private const val CHEVRON_POS_END = "end"
    }

    val binding = ItemDynamicOneLinerBinding.bind(view)

    override fun bind(element: DynamicOneLinerDataModel) = with(element.data) {
        when (status) {
            STATUS_SHOW -> {
                itemView.setLayoutHeight(LayoutParams.WRAP_CONTENT)
                renderContent(this, element.name, getComponentTrackData(element))
                impressComponent(element)
            }

            STATUS_HIDE -> {
                itemView.setLayoutHeight(0)
            }
        }
    }

    private fun renderContent(
        data: DynamicOneLinerDataModel.Data,
        name: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) = with(binding) {
        configPadding(binding, data)

        val title = data.text
        dynamicOneLinerTitle.showIfWithBlock(title.isNotEmpty()) {
            text = title.parseAsHtmlLink(context = context, replaceNewLine = false)
        }

        val iconUrl = data.icon
        dynamicOneLinerIconLeft.showIfWithBlock(iconUrl.isNotEmpty()) {
            setImageUrl(iconUrl)
        }

        val url = data.applink
        dynamicOneLinerIconRight.showIfWithBlock(
            url.isNotEmpty() ||
                name == ProductDetailConstant.PRODUCT_DYNAMIC_ONELINER_PROMO
        ) {
            val chainStyle = when (data.chevronPos) {
                CHEVRON_POS_FOLLOW -> ConstraintSet.CHAIN_PACKED
                CHEVRON_POS_END -> ConstraintSet.CHAIN_SPREAD_INSIDE
                else -> null
            }

            chainStyle?.let {
                val constraintSet = ConstraintSet()
                constraintSet.clone(dynamicOneLinerContent)

                constraintSet.setHorizontalChainStyle(id, chainStyle)
                constraintSet.applyTo(dynamicOneLinerContent)
            }
        }

        setupClick(
            url = url,
            name = name,
            text = data.text,
            componentTrackDataModel = componentTrackDataModel
        )
        dynamicOneLinerSeparatorTop.showWithCondition(data.shouldShowSeparatorTop)
        dynamicOneLinerSeparatorBottom.showWithCondition(data.shouldShowSeparatorBottom)
    }

    private fun setupClick(
        url: String,
        name: String,
        text: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (url.isNotEmpty() || name == ProductDetailConstant.PRODUCT_DYNAMIC_ONELINER_PROMO) {
            itemView.setOnClickListener {
                listener.onClickDynamicOneLiner(text, url, componentTrackDataModel)
            }
        } else {
            itemView.setOnClickListener(null)
        }
    }

    private fun configPadding(
        binding: ItemDynamicOneLinerBinding,
        data: DynamicOneLinerDataModel.Data
    ) = with(binding.dynamicOneLinerContent) {
        setPadding(paddingLeft, data.paddingTopPx, paddingRight, data.paddingBottomPx)
    }

    private fun impressComponent(element: DynamicOneLinerDataModel) {
        itemView.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }
}
