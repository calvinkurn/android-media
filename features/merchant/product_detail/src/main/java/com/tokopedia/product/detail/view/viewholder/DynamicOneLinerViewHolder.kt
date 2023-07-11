package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicOneLinerDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicOneLinerBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.renderHtmlBold

class DynamicOneLinerViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<DynamicOneLinerDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_one_liner

        private const val STATUS_SHOW = "show"
        private const val STATUS_HIDE = "hide"
        private const val STATUS_PLACEHOLDER = "placeholder"

        private const val CHEVRON_POS_FOLLOW = "follow_text"
        private const val CHEVRON_POS_END = "end"
    }

    val binding = ItemDynamicOneLinerBinding.bind(view)

    override fun bind(element: DynamicOneLinerDataModel) = with(element.data) {
        when (status) {
            STATUS_PLACEHOLDER -> {
                itemView.show()
                binding.dynamicOneLinerContent.hide()
                binding.dynamicOneLinerShimmering.show()
            }

            STATUS_SHOW -> {
                itemView.show()
                binding.dynamicOneLinerContent.show()
                binding.dynamicOneLinerShimmering.hide()
                renderContent(this, getComponentTrackData(element))
                impressComponent(element)
            }

            else -> {
                itemView.setLayoutHeight(0)
            }
        }
    }

    private fun renderContent(
        data: DynamicOneLinerDataModel.Data,
        componentTrackDataModel: ComponentTrackDataModel
    ) = with(binding) {
        val title = data.text
        dynamicOneLinerTitle.showIfWithBlock(title.isNotEmpty()) {
            val context = context
            text = if (context == null) {
                title.parseAsHtml()
            } else {
                title.renderHtmlBold(context)
            }
        }

        val iconUrl = data.icon
        dynamicOneLinerIconLeft.showIfWithBlock(iconUrl.isNotEmpty()) {
            setImageUrl(iconUrl)
        }

        val url = data.applink
        dynamicOneLinerIconRight.showIfWithBlock(url.isNotEmpty()) {
            itemView.setOnClickListener {
                listener.onClickDynamicOneLiner(data.text, componentTrackDataModel)
                listener.goToApplink(url)
            }

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

        dynamicOneLinerSeparatorTop.showWithCondition(data.shouldShowSeparatorTop)
        dynamicOneLinerSeparatorBottom.showWithCondition(data.shouldShowSeparatorBottom)
    }

    private fun impressComponent(element: DynamicOneLinerDataModel) {
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }
}
