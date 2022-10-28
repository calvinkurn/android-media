package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeActionButtonSectionBinding
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailActionButtonListModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailActionButtonModel
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by furqan on 02/11/2021
 */
class RechargeOrderDetailActionButtonSectionViewHolder(
    private val binding: ItemOrderDetailRechargeActionButtonSectionBinding,
    private val listener: ActionListener?
) : AbstractViewHolder<RechargeOrderDetailActionButtonListModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailActionButtonListModel) {
        with(binding) {
            if (containerRechargeOrderDetailActionButton.childCount < element.actionButtons.size) {
                containerRechargeOrderDetailActionButton.removeAllViews()
                val primaryActionButton = mutableListOf<UnifyButton>()

                for (item in element.actionButtons) {
                    val button = createActionButton(root.context, item)
                    primaryActionButton.add(button)
                }

                for (button in primaryActionButton) {
                    containerRechargeOrderDetailActionButton.addView(button)
                    button.setMargin(
                        0,
                        root.context.resources.getDimensionPixelSize(
                            com.tokopedia.unifyprinciples.R.dimen.unify_space_8
                        ),
                        0,
                        0
                    )
                    button.requestLayout()
                }
            }
        }
    }

    private fun createActionButton(
        context: Context,
        actionButton: RechargeOrderDetailActionButtonModel
    ): UnifyButton {
        val button = UnifyButton(context)
        button.text = actionButton.label
        button.buttonSize = UnifyButton.Size.MEDIUM

        if (actionButton.buttonType.equals(PRIMARY_BUTTON_TYPE, true)) {
            button.buttonType = UnifyButton.Type.MAIN
            button.buttonVariant = UnifyButton.Variant.FILLED
        } else if (actionButton.buttonType.equals(SECONDARY_BUTTON_TYPE, true)) {
            button.buttonType = UnifyButton.Type.MAIN
            button.buttonVariant = UnifyButton.Variant.GHOST
        } else if (actionButton.buttonType.equals(DISABLED_BUTTON_TYPE, true)) {
            button.buttonType = UnifyButton.Type.MAIN
            button.buttonVariant = UnifyButton.Variant.FILLED
            button.isEnabled = false
        }

        button.setOnClickListener {
            listener?.onActionButtonClicked(actionButton)
            if (!actionButton.mappingUri.equals(MAPPING_URI_VOID, true)) {
                onActionButtonClicked(context, actionButton.uri)
            } else {
                listener?.onVoidButtonClicked()
            }
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
        fun onActionButtonClicked(actionButton: RechargeOrderDetailActionButtonModel)
        fun onVoidButtonClicked()
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_action_button_section

        const val MAPPING_URI_VOID = "void"

        private const val PRIMARY_BUTTON_TYPE = "primary"
        private const val SECONDARY_BUTTON_TYPE = "secondary"
        private const val DISABLED_BUTTON_TYPE = "disabled"

        private const val TOKOPEDIA_PREFIX = "tokopedia"
        private const val IDEM_POTENCY_KEY = "idem_potency_key"
    }
}
