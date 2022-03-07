package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeProductBinding
import com.tokopedia.buyerorder.recharge.presentation.customview.RechargeOrderDetailSimpleView
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailSectionModel

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailProductViewHolder(
        private val binding: ItemOrderDetailRechargeProductBinding,
        private val listener: ActionListener?
) : AbstractViewHolder<RechargeOrderDetailSectionModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailSectionModel) {
        with(binding) {
            if (containerRechargeOrderDetailProductDetail.childCount < element.detailList.size) {
                containerRechargeOrderDetailProductDetail.removeAllViews()
                for (item in element.detailList) {
                    val simpleView = RechargeOrderDetailSimpleView(root.context)
                    simpleView.setData(item)
                    if (item.isCopyable)
                        simpleView.setCopyListener {
                            listener?.onCopyCodeClicked(item.label, item.detail)
                        }
                    containerRechargeOrderDetailProductDetail.addView(simpleView)
                }
            }
        }
    }

    interface ActionListener {
        fun onCopyCodeClicked(label: String, value: String)
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_product
    }
}