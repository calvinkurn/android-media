package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital_checkout.R
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_digital_checkout_bottom_view.view.*
import org.jetbrains.annotations.NotNull

class DigitalCheckoutBottomViewWidget @JvmOverloads constructor(@NotNull context: Context,
                                                                attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_digital_checkout_bottom_view, this, true)
    }

    var isGoToPlusCheckout: Boolean = false
        set(isGoToPlus){
            field = isGoToPlus
            with(view_consent_goto_plus){
                shouldShowWithAction(isGoToPlusCheckout){
                    setDescription(context.getString(
                        R.string.digital_cart_goto_plus_consent,
                        context.getString(R.string.digital_cart_goto_plus_tos),
                        context.getString(R.string.digital_cart_goto_plus_privacy_policy)
                    ))
                    setOnTickCheckbox { isCheckoutButtonEnabled = it }
                    setLinkMovement()
                }
            }
        }

    var promoButtonTitle: String = ""
        set(title) {
            field = title
            digitalPromoBtnView.title = title
        }

    var promoButtonDescription: String = ""
        set(desc) {
            field = desc
            digitalPromoBtnView.desc = desc
        }

    var promoButtonState: ButtonPromoCheckoutView.State = ButtonPromoCheckoutView.State.ACTIVE
        set(state) {
            field = state
            digitalPromoBtnView.state = state
        }

    var promoButtonChevronIcon: Int = 0
        set(value) {
            field = value
            digitalPromoBtnView.chevronIcon = value
        }

    var totalPayment: String = ""
        set(total) {
            field = total
            tvTotalPayment.text = total
        }

    var promoButtonVisibility: Int = View.VISIBLE
        set(visibility) {
            field = visibility
            digitalPromoBtnView.visibility = visibility
        }

    var checkoutButtonText: String = ""
        set(value) {
            field = value
            btnCheckout.text = value
        }

    var isCheckoutButtonEnabled: Boolean = true
        set(isEnabled) {
            field = isEnabled
            btnCheckout.isEnabled = view_consent_goto_plus.isChecked() || isEnabled
        }

    private var onClickConsentListener: ((String) -> Unit)? = null

    fun setDigitalPromoButtonListener(listener: () -> Unit) {
        digitalPromoBtnView.setOnClickListener { listener.invoke() }
    }

    fun setButtonChevronIconListener(listener: () -> Unit) {
        digitalPromoBtnView.setListenerChevronIcon { listener.invoke() }
    }

    fun setCheckoutButtonListener(listener: () -> Unit) {
        btnCheckout.setOnClickListener { listener.invoke() }
    }

    fun setOnClickConsentListener(listener: (String) -> Unit){
        onClickConsentListener = listener
    }

    fun disableVoucherView() {
        digitalPromoBtnView.state = ButtonPromoCheckoutView.State.INACTIVE
        val chevronImageView = digitalPromoBtnView.findViewById<ImageView>(com.tokopedia.promocheckout.common.R.id.iv_promo_checkout_right)
        chevronImageView.setImageDrawable(null)

        val titleTextView = digitalPromoBtnView.findViewById<TextView>(com.tokopedia.promocheckout.common.R.id.tv_promo_checkout_title)
        titleTextView.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        titleTextView.text = resources.getString(R.string.digital_checkout_promo_disabled_title)

        val descTextView = digitalPromoBtnView.findViewById<TextView>(com.tokopedia.promocheckout.common.R.id.tv_promo_checkout_desc)
        descTextView.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        descTextView.text = resources.getString(R.string.digital_checkout_promo_disabled_description)

        digitalPromoBtnView.setOnClickListener { /* do nothing */ }
    }

    private fun setLinkMovement(){

        fun redirectToConsentUrl(url: String) = "tokopedia://webview?url=$url"

        view_consent_goto_plus.setOnClickUrl(
            Pair(context.getString(R.string.digital_cart_goto_plus_tos), {
                onClickConsentListener?.invoke(redirectToConsentUrl(TNC_URL))
            }),
            Pair(context.getString(R.string.digital_cart_goto_plus_privacy_policy), {
                onClickConsentListener?.invoke(redirectToConsentUrl(PRIVACY_POLICY_URL))
            })
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onClickConsentListener = null
    }

    private companion object{
        const val PRIVACY_POLICY_URL = "https://www.tokopedia.com/privacy"
        const val TNC_URL = "https://www.tokopedia.com/help/article/tnc-gotoplus"
    }
}