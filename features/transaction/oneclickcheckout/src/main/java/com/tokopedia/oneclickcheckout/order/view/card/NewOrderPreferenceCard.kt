package com.tokopedia.oneclickcheckout.order.view.card

import android.graphics.Typeface.BOLD
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.features.shippingdurationocc.ShippingDurationOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingdurationocc.ShippingDurationOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.AddressListBottomSheet
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.InstallmentDetailBottomSheet
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class NewOrderPreferenceCard(private val view: View, private val listener: OrderPreferenceCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private lateinit var preference: OrderPreference
    private var shipment: OrderShipment? = null
    private var payment: OrderPayment? = null

    private val tvCardHeader by lazy { view.findViewById<Typography>(R.id.tv_new_card_header) }
    private val lblDefaultPreference by lazy { view.findViewById<Label>(R.id.lbl_new_default_preference) }
    private val tvChoosePreference by lazy { view.findViewById<Typography>(R.id.tv_new_choose_preference) }

    private val tvAddressName by lazy { view.findViewById<Typography>(R.id.tv_new_address_name) }
    private val tvAddressReceiver by lazy { view.findViewById<Typography>(R.id.tv_new_address_receiver) }
    private val tvAddressDetail by lazy { view.findViewById<Typography>(R.id.tv_new_address_detail) }
    private val btnChangeAddress by lazy { view.findViewById<IconUnify>(R.id.btn_new_change_address) }

    private val tickerShippingPromo by lazy { view.findViewById<CardUnify>(R.id.ticker_new_shipping_promo) }
    private val tickerShippingPromoDescription by lazy { view.findViewById<Typography>(R.id.ticker_new_shipping_promo_description) }
    private val tickerAction by lazy { view.findViewById<Typography>(R.id.ticker_new_action) }

    private val tvShippingDuration by lazy { view.findViewById<Typography>(R.id.tv_new_shipping_duration) }
    private val btnChangeDuration by lazy { view.findViewById<IconUnify>(R.id.btn_new_change_duration) }
    private val tvShippingCourier by lazy { view.findViewById<Typography>(R.id.tv_new_shipping_courier) }
    private val tvShippingDiscountPrice by lazy { view.findViewById<Typography>(R.id.tv_new_shipping_discount_price) }
    private val btnChangeCourier by lazy { view.findViewById<IconUnify>(R.id.btn_new_change_courier) }
    private val tvShippingErrorMessage by lazy { view.findViewById<Typography>(R.id.tv_new_shipping_error_message) }
    private val btnReloadShipping by lazy { view.findViewById<Typography>(R.id.btn_new_reload_shipping) }
    private val iconReloadShipping by lazy { view.findViewById<IconUnify>(R.id.icon_new_reload_shipping) }

    private val tvPaymentCCName by lazy { view.findViewById<Typography>(R.id.tv_new_payment_cc_name) }
    private val btnChangePaymentCC by lazy { view.findViewById<IconUnify>(R.id.btn_new_change_payment_cc) }
    private val dividerCCPayment by lazy { view.findViewById<View>(R.id.divider_new_cc_payment) }

    private val ivPayment by lazy { view.findViewById<ImageView>(R.id.iv_new_payment) }
    private val tvPaymentName by lazy { view.findViewById<Typography>(R.id.tv_new_payment_name) }
    private val tvPaymentDetail by lazy { view.findViewById<Typography>(R.id.tv_new_payment_detail) }
    private val btnChangePayment by lazy { view.findViewById<IconUnify>(R.id.btn_new_change_payment) }
    private val tvPaymentErrorMessage by lazy { view.findViewById<Typography>(R.id.tv_new_payment_error_message) }
    private val tvPaymentOvoErrorAction by lazy { view.findViewById<Typography>(R.id.tv_new_payment_ovo_error_action) }

    private val tvPaymentInfo by lazy { view.findViewById<Typography>(R.id.tv_new_payment_info) }

    private val tvInstallmentType by lazy { view.findViewById<Typography>(R.id.tv_new_installment_type) }
    private val tvInstallmentDetail by lazy { view.findViewById<Typography>(R.id.tv_new_installment_detail) }
    private val btnChangeInstallment by lazy { view.findViewById<IconUnify>(R.id.btn_new_change_installment) }
    private val tvInstallmentErrorMessage by lazy { view.findViewById<Typography>(R.id.tv_new_installment_error_message) }
    private val tvInstallmentErrorAction by lazy { view.findViewById<Typography>(R.id.tv_new_installment_error_action) }

    fun setPreference(preference: OrderPreference, revampData: OccRevampData) {
        this.preference = preference
        showPreference(revampData)
    }

    fun setShipment(shipment: OrderShipment?) {
        if (::preference.isInitialized) {
            this.shipment = shipment
            showShipping()
        }
    }

    fun setPayment(payment: OrderPayment) {
        if (::preference.isInitialized) {
            this.payment = payment
            showPayment()
        }
    }

    private fun showPreference(revampData: OccRevampData) {
        showHeader(revampData)

        showAddress()

        showShipping()

        showPayment()
    }

    private fun showHeader(revampData: OccRevampData) {
        tvCardHeader?.text = view.context.getString(R.string.lbl_new_occ_profile_name)
        if (preference.preference.status == 2) {
            lblDefaultPreference?.visible()
        } else {
            lblDefaultPreference?.gone()
        }
        tvChoosePreference?.text = revampData.changeTemplateText
        tvChoosePreference?.setOnClickListener {
            if (revampData.totalProfile > 1) {
                listener.onChangePreferenceClicked()
            } else {
                listener.onAddPreferenceClicked(preference)
            }
        }
    }

    private fun showShipping() {
        val shipmentModel = preference.preference.shipment

        val shipping = shipment

        if (shipping == null || shipping.serviceName.isNullOrEmpty()) {
            // loading?
        } else {
            if (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank()) {
                tvShippingDuration?.text = "Pengiriman ${shipping.serviceName}"
                tvShippingDuration?.visible()
                setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                    listener.chooseDuration(false)
                }
                btnChangeDuration?.visible()
                tvShippingCourier?.text = "${shipping.shipperName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice ?: 0, false).removeDecimalSuffix()})"
                tvShippingCourier?.visible()
                btnChangeCourier?.visible()
                tvShippingErrorMessage?.gone()
                btnReloadShipping?.gone()
                iconReloadShipping?.gone()

                renderBboTicker(shipping)

                if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
                    tvShippingCourier?.text = view.context.getString(R.string.lbl_osp_free_shipping)
                    tvShippingDuration?.gone()
                    btnChangeDuration?.gone()
                    if (shipping.logisticPromoViewModel.benefitAmount >= shipping.logisticPromoViewModel.shippingRate) {
                        tvShippingDiscountPrice?.gone()
                    } else {
                        val originalPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.shippingRate, false).removeDecimalSuffix()
                        val finalPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.discountedRate, false).removeDecimalSuffix()
                        val span = SpannableString("($originalPrice $finalPrice)")
                        span.setSpan(StrikethroughSpan(), 1, 1 + originalPrice.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        span.setSpan(RelativeSizeSpan(10 / 12f), 1, 1 + originalPrice.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        span.setSpan(StyleSpan(BOLD), 0, 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        span.setSpan(StyleSpan(BOLD), 1 + originalPrice.length, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        tvShippingDiscountPrice?.text = span
                        tvShippingDiscountPrice?.visible()
                    }
                    setMultiViewsOnClickListener(tvShippingCourier, tvShippingDiscountPrice, btnChangeCourier) {
                        listener.chooseDuration(false)
                    }
                } else {
                    tvShippingDiscountPrice?.gone()
                    setMultiViewsOnClickListener(tvShippingCourier, tvShippingDiscountPrice, btnChangeCourier) {
                        listener.chooseCourier()
                    }
                }
            } else if (shipping.serviceErrorMessage.isNotBlank() && shipping.shippingRecommendationData != null) {
                tvShippingDuration?.text = "Pengiriman ${shipping.serviceName}"
                tvShippingDuration?.visible()
                setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                    /* no-op */
                }
                btnChangeDuration?.gone()
                val button = "Ubah"
                val span = SpannableString("${shipping.serviceErrorMessage} $button")
                span.setSpan(StyleSpan(BOLD), shipping.serviceErrorMessage.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                view.context?.let {
                    span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), shipping.serviceErrorMessage.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                tvShippingErrorMessage?.text = span
                tvShippingErrorMessage?.visible()
                tvShippingErrorMessage?.setOnClickListener {
                    listener.chooseDuration(true)
                }
                tvShippingCourier?.gone()
                btnChangeCourier?.gone()
                btnReloadShipping?.gone()
                iconReloadShipping?.gone()
                tvShippingDiscountPrice?.gone()
                tickerShippingPromo?.gone()
            } else {
                tvShippingDuration?.text = "Pengiriman"
                tvShippingDuration?.visible()
                btnChangeDuration?.gone()
                setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                    /* no-op */
                }
                tvShippingCourier?.gone()
                btnChangeCourier?.gone()
                tvShippingErrorMessage?.text = shipping.serviceErrorMessage
                tvShippingErrorMessage?.visible()
                tvShippingErrorMessage?.setOnClickListener {
                    /* no-op */
                }
                setMultiViewsOnClickListener(iconReloadShipping, btnReloadShipping) {
                    listener.reloadShipping()
                }
                btnReloadShipping?.visible()
                iconReloadShipping?.visible()
                tvShippingDiscountPrice?.gone()
                tickerShippingPromo?.gone()
            }
        }
    }

    private fun renderBboTicker(shipping: OrderShipment) {
        if (shipping.logisticPromoTickerMessage?.isNotEmpty() == true && shipping.shippingRecommendationData?.logisticPromo != null) {
            tickerShippingPromoDescription?.text = "${shipping.logisticPromoTickerMessage}"
            tickerShippingPromo?.visible()
            tickerAction?.setOnClickListener {
                listener.onLogisticPromoClick(shipping.shippingRecommendationData.logisticPromo)
            }
        } else {
            tickerShippingPromo?.gone()
        }
    }

    /*private fun generateServiceDuration(tempServiceDuration: String?): String {
        return if (tempServiceDuration == null || !tempServiceDuration.contains("(") || !tempServiceDuration.contains(")")) {
            view.context.getString(R.string.lbl_no_exact_shipping_duration)
        } else {
            view.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
        }
    }*/

    private fun showPayment() {
        val paymentModel = preference.preference.payment

        ivPayment?.let {
            ImageHandler.loadImageFitCenter(view.context, it, paymentModel.image)
        }
        tvPaymentName?.text = paymentModel.gatewayName
        val description = paymentModel.description
        if (description.isNotBlank()) {
            tvPaymentDetail?.text = description.replace('*', '\u2022')
            tvPaymentDetail?.visible()
        } else {
            tvPaymentDetail?.gone()
        }

        if (paymentModel.tickerMessage.isNotBlank()) {
            tvPaymentInfo?.text = MethodChecker.fromHtml(paymentModel.tickerMessage)
            tvPaymentInfo?.visible()
        } else {
            tvPaymentInfo?.gone()
        }

        val payment = payment
        if (payment != null) {
            if (!payment.isError()) {
                tvPaymentErrorMessage?.gone()
                setPaymentActiveAlpha()

                setupPaymentSelector(payment)

                setupPaymentInstallment(payment.creditCard)
            } else {
                if (payment.customErrorMessage.isNotEmpty()) {
                    // general error
                    val message = payment.customErrorMessage
                    val button = payment.customErrorButton

                    val span = SpannableString("$message $button")
                    if (button.isNotBlank()) {
                        span.setSpan(StyleSpan(BOLD), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        view.context?.let {
                            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        tvPaymentErrorMessage?.setOnClickListener {
                            listener.choosePayment(preference)
                        }
                    }
                    tvPaymentErrorMessage?.text = span
                    tvPaymentErrorMessage?.visible()
                    btnChangePayment?.invisible()
                    setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                        /* no-op */
                    }
                    setPaymentErrorAlpha(false)
                } else if (payment.errorMessage.message.isNotEmpty()) {
                    // cc error
                    val message = payment.errorMessage.message
                    val button = payment.errorMessage.button.text

                    val span = SpannableString("$message $button")
                    if (button.isNotBlank()) {
                        span.setSpan(StyleSpan(BOLD), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        view.context?.let {
                            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        tvPaymentErrorMessage?.setOnClickListener {
                            if (payment.hasCreditCardOption()) {
                                listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
                            } else {
                                listener.onPreferenceEditClicked(preference)
                            }
                        }
                    }
                    tvPaymentErrorMessage?.text = span
                    tvPaymentErrorMessage?.visible()
                    btnChangePayment?.invisible()
                    setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                        /* no-op */
                    }
                    setPaymentErrorAlpha(payment.isCalculationError)
                } else if (payment.ovoErrorData != null) {
                    // ovo error
                    val message = payment.ovoErrorData.message
                    val button = payment.ovoErrorData.buttonTitle

                    val span = SpannableString("$message $button")
                    if (message.isBlank() && button.isNotBlank()) {
                        // only show button
                        tvPaymentOvoErrorAction?.setOnClickListener {
                            if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_TOP_UP) {
                                listener.onOvoTopUpClicked(payment.ovoErrorData.callbackUrl, payment.ovoErrorData.isHideDigital, payment.ovoData.customerData)
                            } else if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_ACTIVATION) {
                                listener.onOvoActivateClicked(payment.ovoErrorData.callbackUrl)
                            }
                        }
                        tvPaymentOvoErrorAction?.text = button
                        tvPaymentOvoErrorAction?.visible()
                        tvPaymentDetail?.gone()
                        tvPaymentErrorMessage?.gone()
                    } else if (message.isNotBlank() && button.isNotBlank()) {
                        // show message and button
                        span.setSpan(StyleSpan(BOLD), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        view.context?.let {
                            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                        tvPaymentErrorMessage?.setOnClickListener {
                            if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_TOP_UP) {
                                listener.onOvoTopUpClicked(payment.ovoErrorData.callbackUrl, payment.ovoErrorData.isHideDigital, payment.ovoData.customerData)
                            } else if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_ACTIVATION) {
                                listener.onOvoActivateClicked(payment.ovoErrorData.callbackUrl)
                            }
                        }
                        tvPaymentErrorMessage?.text = span
                        tvPaymentErrorMessage?.visible()
                        tvPaymentDetail?.gone()
                    } else {
                        // only show message
                        tvPaymentErrorMessage?.setOnClickListener {
                            /* no-op */
                        }
                        tvPaymentErrorMessage?.text = span
                        tvPaymentErrorMessage?.visible()
                        tvPaymentDetail?.gone()
                    }
                    btnChangePayment?.visible()
                    if (payment.ovoErrorData.isBlockingError) {
                        setPaymentErrorAlpha(true)
                    } else {
                        setPaymentActiveAlpha()
                    }
                    setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                        listener.choosePayment(preference)
                    }
                } else {
                    tvPaymentErrorMessage?.gone()
                    setPaymentErrorAlpha(payment.isCalculationError)
                    setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                        listener.choosePayment(preference)
                    }
                }
                tvInstallmentType?.gone()
                tvInstallmentDetail?.gone()
                btnChangeInstallment?.gone()
                tvInstallmentErrorMessage?.gone()
                tvInstallmentErrorAction?.gone()
            }
        }
    }

    private fun setupPaymentInstallment(creditCard: OrderPaymentCreditCard) {
        val selectedTerm = creditCard.selectedTerm
        if (!creditCard.isDebit && selectedTerm != null) {
            tvInstallmentType?.visible()
            tvInstallmentDetail?.visible()
            btnChangeInstallment?.visible()
            if (selectedTerm.term > 0) {
                tvInstallmentDetail?.text = "${selectedTerm.term} Bulan x ${CurrencyFormatUtil.convertPriceValueToIdrFormat(selectedTerm.monthlyAmount, false).removeDecimalSuffix()}"
            } else {
                tvInstallmentDetail?.text = view.context.getString(R.string.lbl_installment_full_payment)
            }
            setupPaymentInstallmentError(selectedTerm)
            btnChangeInstallment?.setOnClickListener {
                listener.onInstallmentDetailClicked()
            }
        } else {
            tvInstallmentType?.gone()
            tvInstallmentDetail?.gone()
            btnChangeInstallment?.gone()
            tvInstallmentErrorMessage?.gone()
            tvInstallmentErrorAction?.gone()
        }
    }

    private fun setupPaymentInstallmentError(selectedTerm: OrderPaymentInstallmentTerm) {
        if (selectedTerm.isError) {
            tvInstallmentDetail?.alpha = 0.5f
            tvInstallmentErrorMessage?.text = view.context.getString(R.string.lbl_installment_error)
            tvInstallmentErrorAction?.text = view.context.getString(R.string.lbl_change_template)
            tvInstallmentErrorAction?.setOnClickListener {
                listener.onInstallmentDetailClicked()
            }
            tvInstallmentErrorMessage?.visible()
            tvInstallmentErrorAction?.visible()
        } else {
            tvInstallmentDetail?.alpha = 1.0f
            tvInstallmentErrorMessage?.gone()
            tvInstallmentErrorAction?.gone()
        }
    }

    private fun setupPaymentSelector(payment: OrderPayment) {
        if (payment.creditCard.numberOfCards.availableCards > 1) {
            showPaymentCC(payment)
            btnChangePayment?.visible()
            setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
            }
        } else if (payment.creditCard.bankCode.isNotEmpty()) {
            showPaymentCC(payment)
            btnChangePayment?.invisible()
            setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                /* no-op */
            }
        } else {
            hidePaymentCC()
            btnChangePayment?.visible()
            setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                listener.choosePayment(preference)
            }
        }
    }

    private fun setPaymentErrorAlpha(isDetailRed: Boolean) {
        ivPayment?.alpha = 0.5f
        tvPaymentName?.alpha = 0.5f
        tvPaymentDetail?.alpha = 0.5f
        if (isDetailRed) {
            tvPaymentDetail?.setTextColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Red_R600))
        } else {
            tvPaymentDetail?.setTextColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
        }
    }

    private fun setPaymentActiveAlpha() {
        ivPayment?.alpha = 1f
        tvPaymentName?.alpha = 1f
        tvPaymentDetail?.alpha = 1f
        tvPaymentDetail?.setTextColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
    }

    private fun showPaymentCC(payment: OrderPayment) {
        tvPaymentCCName?.text = payment.gatewayName
        tvPaymentCCName?.visible()
        btnChangePaymentCC?.visible()
        dividerCCPayment?.visible()
        setMultiViewsOnClickListener(tvPaymentCCName, btnChangePaymentCC) {
            listener.choosePayment(preference)
        }
    }

    private fun hidePaymentCC() {
        tvPaymentCCName?.gone()
        btnChangePaymentCC?.gone()
        dividerCCPayment?.gone()
    }

    private fun setMultiViewsOnClickListener(vararg views: View?, onClickListener: () -> Unit) {
        for (view in views) {
            view?.setOnClickListener {
                onClickListener.invoke()
            }
        }
    }

    private fun showAddress() {
        val addressModel = preference.preference.address
        tvAddressName?.text = addressModel.addressName
        val receiverName = addressModel.receiverName
        val phone = addressModel.phone
        var receiverText = ""
        if (receiverName.isNotBlank()) {
            receiverText = " - $receiverName"
            if (phone.isNotBlank()) {
                receiverText = "$receiverText ($phone)"
            }
        }
        if (receiverText.isNotEmpty()) {
            tvAddressReceiver?.text = receiverText
            tvAddressReceiver?.visible()
        } else {
            tvAddressReceiver?.gone()
        }
        tvAddressDetail?.text = "${addressModel.addressStreet}, ${addressModel.districtName}, ${addressModel.cityName}, ${addressModel.provinceName} ${addressModel.postalCode}"

        setMultiViewsOnClickListener(tvAddressName, tvAddressReceiver, tvAddressDetail, btnChangeAddress) {
            listener.chooseAddress()
        }
    }

    fun showAddressBottomSheet(fragment: OrderSummaryPageFragment, usecase: GetAddressCornerUseCase) {
        AddressListBottomSheet(usecase, object : AddressListBottomSheet.AddressListBottomSheetListener {
            override fun onSelect(addressId: String) {
                listener.onAddressChange(addressId)
            }

            override fun onAddAddress(token: Token?) {
                listener.onAddAddress(token)
            }
        }).show(fragment, preference.preference.address.addressId.toString())
    }

    fun showCourierBottomSheet(fragment: OrderSummaryPageFragment) {
        val shippingRecommendationData = shipment?.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val list: ArrayList<RatesViewModelType> = ArrayList()
            for (shippingDurationViewModel in shippingRecommendationData.shippingDurationViewModels) {
                if (shippingDurationViewModel.isSelected) {
                    if (shippingDurationViewModel.shippingCourierViewModelList.isNotEmpty() && isCourierInstantOrSameday(shippingDurationViewModel.shippingCourierViewModelList[0].productData.shipperId)) {
                        list.add(NotifierModel())
                    }
                    list.addAll(shippingDurationViewModel.shippingCourierViewModelList)
                    break
                }
            }
            ShippingCourierOccBottomSheet().showBottomSheet(fragment, list, object : ShippingCourierOccBottomSheetListener {
                override fun onCourierChosen(shippingCourierViewModel: ShippingCourierUiModel) {
                    listener.onCourierChange(shippingCourierViewModel)
                }

                override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
                    listener.onLogisticPromoClick(data)
                }
            })
        }
    }

    private fun isCourierInstantOrSameday(shipperId: Int): Boolean {
        val ids = CourierConstant.INSTANT_SAMEDAY_COURIER
        for (id in ids) {
            if (shipperId == id) return true
        }
        return false
    }

    fun showDurationBottomSheet(fragment: OrderSummaryPageFragment) {
        val shippingRecommendationData = shipment?.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val list: ArrayList<RatesViewModelType> = ArrayList(shippingRecommendationData.shippingDurationViewModels)
            if (shippingRecommendationData.logisticPromo != null) {
                list.add(0, shippingRecommendationData.logisticPromo)
                if (shippingRecommendationData.logisticPromo.disabled && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[0]) && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[1])) {
                    orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
                }
            }
            ShippingDurationOccBottomSheet().showBottomSheet(fragment, list, object : ShippingDurationOccBottomSheetListener {
                override fun onDurationChosen(serviceData: ServiceData, selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
                    listener.onDurationChange(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
                }

                override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
                    listener.onLogisticPromoClick(data)
                }
            })
        }
    }

    fun showInstallmentDetailBottomSheet(fragment: OrderSummaryPageFragment) {
        val creditCard = payment?.creditCard
        if (creditCard != null && creditCard.availableTerms.isNotEmpty()) {
            InstallmentDetailBottomSheet().show(fragment, creditCard, object : InstallmentDetailBottomSheet.InstallmentDetailBottomSheetListener {
                override fun onSelectInstallment(installment: OrderPaymentInstallmentTerm) {
                    listener.onInstallmentDetailChange(installment)
                }
            })
        }
    }

    companion object {
        private val BBO_DESCRIPTION_MINIMUM_LIMIT = arrayOf("belum", "min")
    }

    interface OrderPreferenceCardListener {

        fun onChangePreferenceClicked()

        fun onAddPreferenceClicked(preference: OrderPreference)

        fun onAddAddress(token: Token?)

        fun onAddressChange(addressId: String)

        fun onCourierChange(shippingCourierViewModel: ShippingCourierUiModel)

        fun onDurationChange(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean)

        fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel)

        fun reloadShipping()

        fun chooseAddress()

        fun chooseCourier()

        fun chooseDuration(isDurationError: Boolean)

        fun choosePayment(preference: OrderPreference)

        fun onPreferenceEditClicked(preference: OrderPreference)

        fun onInstallmentDetailClicked()

        fun onInstallmentDetailChange(selectedInstallmentTerm: OrderPaymentInstallmentTerm)

        fun onChangeCreditCardClicked(additionalData: OrderPaymentCreditCardAdditionalData)

        fun onOvoActivateClicked(callbackUrl: String)

        fun onOvoTopUpClicked(callbackUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData)
    }
}