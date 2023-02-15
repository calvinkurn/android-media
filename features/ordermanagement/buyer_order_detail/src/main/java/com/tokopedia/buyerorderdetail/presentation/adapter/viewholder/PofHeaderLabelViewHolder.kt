package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.PofUtils.getColorHexString
import com.tokopedia.buyerorderdetail.databinding.ItemPartialOrderFulfillmentHeaderBinding
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.iconunify.IconUnify

class PofHeaderLabelViewHolder(view: View?) :
    CustomPayloadViewHolder<ProductListUiModel.ProductPofHeaderLabelUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_partial_order_fulfillment_header
    }

    private val binding = ItemPartialOrderFulfillmentHeaderBinding.bind(itemView)

    override fun bind(element: ProductListUiModel.ProductPofHeaderLabelUiModel) {
        with(binding) {
            setPofHeaderTitle(element)
            setPofHeaderIcon(element.isUnfulfilled)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is ProductListUiModel.ProductPofHeaderLabelUiModel &&
                newItem is ProductListUiModel.ProductPofHeaderLabelUiModel
            ) {
                if (oldItem.title != newItem.title || oldItem.quantity != newItem.quantity) {
                    binding.setPofHeaderTitle(newItem)
                }
                if (oldItem.isUnfulfilled != newItem.isUnfulfilled) {
                    binding.setPofHeaderIcon(newItem.isUnfulfilled)
                }
            }
        }
    }

    private fun ItemPartialOrderFulfillmentHeaderBinding.setPofHeaderTitle(
        item: ProductListUiModel.ProductPofHeaderLabelUiModel
    ) {
        val (headerLabelText, headerTotalItemsLabel) = if (item.isUnfulfilled) {
            Pair(
                root.context.getString(
                    com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_header_label,
                    getColorHexString(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    ),
                    item.title,
                ),
                root.context.getString(
                    com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_header_total_items,
                    getColorHexString(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    ),
                    item.quantity,
                )
            )
        } else {
                Pair(
                    root.context.getString(
                        com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_header_label,
                        getColorHexString(
                            root.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950
                        ),
                        item.title,
                    ),
                    root.context.getString(
                        com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_header_total_items,
                        getColorHexString(
                            root.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN600
                        ),
                        item.quantity,
                    )
                )
        }

        tvPovHeaderLabel.text = MethodChecker.fromHtml(headerLabelText)

        tvPovHeaderTotalItemsLabel.text = MethodChecker.fromHtml(headerTotalItemsLabel)
        tvPovHeaderTotalItemsLabel.setWeight(
            if (item.isUnfulfilled) {
                com.tokopedia.unifyprinciples.Typography.BOLD
            } else {
                com.tokopedia.unifyprinciples.Typography.REGULAR
            }
        )
    }

    private fun ItemPartialOrderFulfillmentHeaderBinding.setPofHeaderIcon(isUnfulfilledProducts: Boolean) {
        icPofHeader.run {
            if (isUnfulfilledProducts) {
                val rn500Color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                setImage(
                    newIconId = IconUnify.CLEAR,
                    newLightEnable = rn500Color
                )
            } else {
                val gn500Color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                setImage(
                    newIconId = IconUnify.CHECK_CIRCLE,
                    newLightEnable = gn500Color
                )
            }
        }
    }
}
