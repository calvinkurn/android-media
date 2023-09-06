package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.ui.ConsentType
import org.jetbrains.annotations.NotNull
import com.tokopedia.promocheckout.common.R as PromoCommonRes
import com.tokopedia.unifyprinciples.R as UnifyPrinciplesRes

class DigitalCheckoutBottomViewWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = LayoutDigitalCheckoutBottomViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

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
            binding.btnCheckout.isEnabled = isEnabled
        }

    private var onClickConsentListener: ((String) -> Unit)? = null

    fun setCrossSellConsentWidget(
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner,
        consentCollectionParam: ConsentCollectionParam,
        isEnableCheckoutButtonInteraction: Boolean
    ) {
        with(binding.viewCrossSellConsentWidget) {
            /* isEnableCheckoutButtonInteraction
            * If we also have Product Consent in the checkout page,
            * we need to disable any interaction with checkout button.
            *  **/
            if (isEnableCheckoutButtonInteraction) {
                setOnCheckedChangeListener { isChecked ->
                    isCheckoutButtonEnabled = isChecked
                }
                setOnFailedGetCollectionListener {
                    isCheckoutButtonEnabled = false
                }
                setOnDetailConsentListener { isShowConsent, consentType ->
                    if (isShowConsent) {
                        if (isCrossSellConsentWidgetVisible()) {
                            isCheckoutButtonEnabled = when (consentType) {
                                is ConsentType.SingleInfo -> true
                                is ConsentType.SingleChecklist -> false
                                is ConsentType.MultipleChecklist -> false
                                else -> true
                            }
                        }
                    } else {
                        if (!isProductConsentWidgetVisible()) {
                            isCheckoutButtonEnabled = true
                        }
                    }
                    removeConsentCollectionObserver()
                }
            }
            load(lifecycleOwner, viewModelStoreOwner, consentCollectionParam)
        }
    }

    fun setProductConsentWidget(
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner,
        consentCollectionParam: ConsentCollectionParam,
        hasDataElements: Boolean
    ) {
        with(binding.viewProductConsentWidget) {
            hideWhenAlreadySubmittedConsent = hasDataElements
            setOnCheckedChangeListener { isChecked ->
                isCheckoutButtonEnabled = isChecked
            }
            setOnFailedGetCollectionListener {
                isCheckoutButtonEnabled = false
            }
            setOnDetailConsentListener { isShowConsent, consentType ->
                if (isShowConsent) {
                    if (isProductConsentWidgetVisible()) {
                        isCheckoutButtonEnabled = when (consentType) {
                            is ConsentType.SingleInfo -> true
                            is ConsentType.SingleChecklist -> false
                            is ConsentType.MultipleChecklist -> false
                            else -> true
                        }
                        removeConsentCollectionObserver()
                    }
                } else {
                    isCheckoutButtonEnabled = true
                }
            }
            load(lifecycleOwner, viewModelStoreOwner, consentCollectionParam)
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

    fun getCrossSellConsentPayload(): String = binding.viewCrossSellConsentWidget.generatePayloadData()

    fun getProductConsentPayload(): String = binding.viewProductConsentWidget.generatePayloadData()

    fun showCrossSellConsent() {
        binding.viewCrossSellConsentWidget.show()
    }

    fun hideCrossSellConsent() {
        binding.viewCrossSellConsentWidget.hide()
    }

    fun showProductConsent() {
        binding.viewProductConsentWidget.show()
    }

    fun isProductConsentWidgetVisible(): Boolean {
        return binding.viewProductConsentWidget.isVisible
    }

    fun isCrossSellConsentWidgetVisible(): Boolean {
        return binding.viewCrossSellConsentWidget.isVisible
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onClickConsentListener = null
    }
}
