package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeAboutOrdersBinding
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailAboutOrderModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailActionButtonModel
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by furqan on 02/11/2021
 */
class RechargeOrderDetailAboutOrderViewHolder(
        private val binding: ItemOrderDetailRechargeAboutOrdersBinding,
        private val listener: ActionListener?
) : AbstractViewHolder<RechargeOrderDetailAboutOrderModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailAboutOrderModel) {
        with(binding) {
            containerRechargeOrderDetailHelp.setOnClickListener {
                RouteManager.route(root.context, element.helpUrl)
                listener?.onClickHelp(element.helpUrl)
            }

            val primaryActionButton = mutableListOf<UnifyButton>()
            val secondaryActionButton = mutableListOf<UnifyButton>()

            for (item in element.actionButtonList.actionButtons) {
                val button = createActionButton(root.context, item)

                if (item.buttonType.equals(PRIMARY_BUTTON_TYPE, true)) {
                    primaryActionButton.add(button)
                } else if (item.buttonType.equals(SECONDARY_BUTTON_TYPE, true)) {
                    primaryActionButton.add(button)
                }
            }

            for (button in primaryActionButton) {
                containerRechargeOrderDetailAboutOrder.addView(button)
            }
            for (button in secondaryActionButton) {
                containerRechargeOrderDetailAboutOrder.addView(button)
            }
        }
    }

    private fun createActionButton(context: Context, actionButton: RechargeOrderDetailActionButtonModel): UnifyButton {
        val button = UnifyButton(context)
        button.buttonSize = UnifyButton.Size.MEDIUM
        button.setMargin(0,
                context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                0,
                0)

        if (actionButton.buttonType.equals(PRIMARY_BUTTON_TYPE, true)) {
            button.buttonType = UnifyButton.Type.MAIN
            button.buttonVariant = UnifyButton.Variant.FILLED
        } else if (actionButton.buttonType.equals(SECONDARY_BUTTON_TYPE, true)) {
            button.buttonType = UnifyButton.Type.MAIN
            button.buttonVariant = UnifyButton.Variant.GHOST
        }

        button.setOnClickListener {
            listener?.onActionButtonClicked(actionButton)
            onActionButtonClicked(context, actionButton.uri)
        }

        return button
    }

    private fun onActionButtonClicked(context: Context, uri: String) {
        if (uri.isNotEmpty() && uri.startsWith(TOKOPEDIA_PREFIX)) {
            var mUri = uri
            val url = Uri.parse(mUri)

            if (mUri.contains(IDEM_POTENCY_KEY)) {
                mUri = mUri.replace(url.getQueryParameter(IDEM_POTENCY_KEY) ?: "", "")
                mUri = mUri.replace("$IDEM_POTENCY_KEY=", "")
            }
            RouteManager.route(context, mUri)
        } else if (uri.isNotEmpty()) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, uri)
        }
    }

    interface ActionListener {
        fun onClickHelp(helpUrl: String)
        fun onActionButtonClicked(actionButton: RechargeOrderDetailActionButtonModel)
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_about_orders

        private const val PRIMARY_BUTTON_TYPE = "primary"
        private const val SECONDARY_BUTTON_TYPE = "secondary"

        private const val TOKOPEDIA_PREFIX = "tokopedia"
        private const val IDEM_POTENCY_KEY = "idem_potency_key"
    }
}