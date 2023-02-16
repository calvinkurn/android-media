package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.databinding.LayoutDigitalCheckoutBottomViewBinding
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import org.jetbrains.annotations.NotNull
import com.tokopedia.promocheckout.common.R as PromoCommonRes
import com.tokopedia.unifyprinciples.R as UnifyPrinciplesRes

class DigitalCheckoutBottomViewWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = LayoutDigitalCheckoutBottomViewBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    var isGoToPlusCheckout: Boolean = false
        set(isGoToPlus) {
            field = isGoToPlus
            with(binding.viewConsentGotoPlus) {
                shouldShowWithAction(isGoToPlusCheckout) {
                    setDescription(
                        context.getString(
                            R.string.digital_cart_goto_plus_consent,
                            context.getString(R.string.digital_cart_goto_plus_tos),
                            context.getString(R.string.digital_cart_goto_plus_privacy_policy)
                        )
                    )
                    setOnTickCheckbox { isCheckoutButtonEnabled = it }
                    setLinkMovement()
                }
            }
        }

    var promoButtonTitle: String = ""
        set(title) {
            field = title
            binding.digitalPromoBtnView.title = title
        }

    var promoButtonDescription: String = ""
        set(desc) {
            field = desc
            binding.digitalPromoBtnView.desc = desc
        }

    var promoButtonState: ButtonPromoCheckoutView.State = ButtonPromoCheckoutView.State.ACTIVE
        set(state) {
            field = state
            binding.digitalPromoBtnView.state = state
        }

    var promoButtonChevronIcon: Int = 0
        set(value) {
            field = value
            binding.digitalPromoBtnView.chevronIcon = value
        }

    var totalPayment: String = ""
        set(total) {
            field = total
            binding.tvTotalPayment.text = total
        }

    var promoButtonVisibility: Int = View.VISIBLE
        set(visibility) {
            field = visibility
            binding.digitalPromoBtnView.visibility = visibility
        }

    var checkoutButtonText: String = ""
        set(value) {
            field = value
            binding.btnCheckout.text = value
        }

    var isCheckoutButtonEnabled: Boolean = true
        set(isEnabled) {
            field = isEnabled
            binding.btnCheckout.isEnabled = binding.viewConsentGotoPlus.isChecked() || isEnabled
        }

    private var onClickConsentListener: ((String) -> Unit)? = null

    fun setUserConsentWidget(
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner,
        consentCollectionParam: ConsentCollectionParam,
    ) {
        with(binding.viewUserConsentWidget) {
            setOnCheckedChangeListener { isChecked ->
                isCheckoutButtonEnabled = isChecked
            }
            setOnFailedGetCollectionListener {
                isCheckoutButtonEnabled = false
            }
            setOnNeedConsentListener { isNeedConsent ->
                isCheckoutButtonEnabled = !isNeedConsent
            }
            load(lifecycleOwner, viewModelStoreOwner, consentCollectionParam)
            showCrossSellConsent()
        }
    }

    fun setDigitalPromoButtonListener(listener: () -> Unit) {
        binding.digitalPromoBtnView.setOnClickListener { listener.invoke() }
    }

    fun setButtonChevronIconListener(listener: () -> Unit) {
        binding.digitalPromoBtnView.setListenerChevronIcon { listener.invoke() }
    }

    fun setCheckoutButtonListener(listener: () -> Unit) {
        binding.btnCheckout.setOnClickListener { listener.invoke() }
    }

    fun setOnClickConsentListener(listener: (String) -> Unit) {
        onClickConsentListener = listener
    }

    fun disableVoucherView() {
        binding.digitalPromoBtnView.state = ButtonPromoCheckoutView.State.INACTIVE

        val chevronImageView = binding.digitalPromoBtnView.findViewById<ImageView>(PromoCommonRes.id.iv_promo_checkout_right)
        chevronImageView.setImageDrawable(null)

        val titleTextView =
            binding.digitalPromoBtnView.findViewById<TextView>(PromoCommonRes.id.tv_promo_checkout_title)
        titleTextView.setTextColor(
            MethodChecker.getColor(context, UnifyPrinciplesRes.color.Unify_N700_32)
        )
        titleTextView.text = resources.getString(R.string.digital_checkout_promo_disabled_title)

        val descTextView =
            binding.digitalPromoBtnView.findViewById<TextView>(PromoCommonRes.id.tv_promo_checkout_desc)
        descTextView.gone()

        binding.digitalPromoBtnView.setOnClickListener { /* do nothing */ }
    }

    private fun setLinkMovement() {

        fun redirectToConsentUrl(url: String) = "tokopedia://webview?url=$url"

        binding.viewConsentGotoPlus.setOnClickUrl(
            Pair(context.getString(R.string.digital_cart_goto_plus_tos), {
                onClickConsentListener?.invoke(redirectToConsentUrl(TNC_URL))
            }),
            Pair(context.getString(R.string.digital_cart_goto_plus_privacy_policy), {
                onClickConsentListener?.invoke(redirectToConsentUrl(PRIVACY_POLICY_URL))
            })
        )
    }

    fun getCrossSellConsentPayload(): String =
        binding.viewUserConsentWidget.generatePayloadData()

    fun showCrossSellConsent() {
        binding.viewUserConsentWidget.show()
    }

    fun hideCrossSellConsent() {
        binding.viewUserConsentWidget.hide()
    }

    fun isCrossSellConsentVisible(): Boolean {
        return binding.viewUserConsentWidget.isVisible
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onClickConsentListener = null
    }

    private companion object {
        const val PRIVACY_POLICY_URL = "https://www.tokopedia.com/privacy"
        const val TNC_URL = "https://www.tokopedia.com/help/article/tnc-gotoplus"
    }
}
