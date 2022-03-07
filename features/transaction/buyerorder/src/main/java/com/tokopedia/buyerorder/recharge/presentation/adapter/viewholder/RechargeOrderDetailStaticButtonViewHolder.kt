package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeStaticButtonBinding
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailStaticButtonModel
import com.tokopedia.buyerorder.recharge.presentation.model.StaticButtonType
import com.tokopedia.media.loader.loadImage

/**
 * @author by furqan on 02/11/2021
 */
class RechargeOrderDetailStaticButtonViewHolder(
        private val binding: ItemOrderDetailRechargeStaticButtonBinding,
        private val listener: ActionListener?
) : AbstractViewHolder<RechargeOrderDetailStaticButtonModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailStaticButtonModel) {
        with(binding) {
            if (element.iconUrl.isEmpty())
                when (element.buttonType) {
                    StaticButtonType.LANGGANAN -> {
                        ivRechargeOrderDetailStatic.loadImage(
                                getString(R.string.static_assets_ic_langganan_link)
                        )
                    }
                    StaticButtonType.SBM -> {
                        ivRechargeOrderDetailStatic.loadImage(
                                getString(R.string.static_assets_ic_my_bills_link)
                        )
                    }
                }
            else
                ivRechargeOrderDetailStatic.loadImage(element.iconUrl)


            if (element.title.isEmpty())
                tgRechargeOrderDetailStaticTitle.text = getString(element.titleRes)
            else
                tgRechargeOrderDetailStaticTitle.text = element.title

            if (element.subtitle.isEmpty())
                tgRechargeOrderDetailStaticDetail.text = getString(element.subtitleRes)
            else
                tgRechargeOrderDetailStaticDetail.text = element.subtitle

            root.setOnClickListener {
                listener?.onClickStaticButton(element)
                RouteManager.route(root.context, element.actionUrl)
            }
        }
    }

    interface ActionListener {
        fun onClickStaticButton(staticButtonModel: RechargeOrderDetailStaticButtonModel)
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_static_button
    }
}