package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseShippingBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseShippingUiModel

class TokoFoodPurchaseShippingViewHolder(private val viewBinding: ItemPurchaseShippingBinding,
                                         private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseShippingUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_shipping
    }

    override fun bind(element: TokoFoodPurchaseShippingUiModel) {
        if (element.isNeedPinpoint) {
            renderPinpoint(viewBinding, element)
        } else {
            renderShipping(viewBinding, element)
        }
    }

    private fun renderPinpoint(viewBinding: ItemPurchaseShippingBinding, element: TokoFoodPurchaseShippingUiModel) {
        with(viewBinding) {
            containerPinpoint.show()
            containerShipping.gone()

/*
            val noPinpointFullInformation = itemView.context.getString(R.string.text_purchase_message_need_pinpoint)
            val noPinpointAction = itemView.context.getString(R.string.text_purchase_message_need_pinpoint_action)
            val startSpan = noPinpointFullInformation.indexOf(noPinpointAction)
            val endSpan = noPinpointFullInformation.indexOf(noPinpointAction) + noPinpointAction.length
            val noPinpointActionSpannable: Spannable = SpannableString(noPinpointFullInformation).let {
                val color = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                it.setSpan(ForegroundColorSpan(color), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                it.setSpan(TypefaceSpan("sans-serif-medium"), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                it.setSpan(ForegroundColorSpan(color), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                it
            }
            textNoPinpoint.movementMethod = LinkMovementMethod.getInstance()
            textNoPinpoint.text = noPinpointActionSpannable
*/
            val noPinpointFullInformation = itemView.context.getString(R.string.text_purchase_message_need_pinpoint)
            textNoPinpoint.text = MethodChecker.fromHtml(noPinpointFullInformation)
            textNoPinpoint.setOnClickListener {
                listener.onTextSetPinpointClicked()
            }
        }
    }

    private fun renderShipping(viewBinding: ItemPurchaseShippingBinding, element: TokoFoodPurchaseShippingUiModel) {
        with(viewBinding) {
            containerShipping.show()
            containerPinpoint.gone()
            imageShippingLogo.setImageUrl(element.shippingLogoUrl)
            textShippingCourierName.text = element.shippingCourierName
            textShippingEta.text = element.shippingEta
        }
    }


}