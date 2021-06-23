package com.tokopedia.oneclickcheckout.order.view.card

import android.annotation.SuppressLint
import android.graphics.Typeface.BOLD
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
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
import com.tokopedia.oneclickcheckout.address.AddressListBottomSheet
import com.tokopedia.oneclickcheckout.databinding.CardOrderPreferenceNewBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.payment.creditcard.installment.InstallmentDetailBottomSheet
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class NewOrderPreferenceCard(private val binding: CardOrderPreferenceNewBinding, private val listener: OrderPreferenceCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics): RecyclerView.ViewHolder(binding.root) {

    private lateinit var preference: OrderPreference
    private var shipment: OrderShipment? = null
    private var payment: OrderPayment? = null

    fun setPreference(preference: OrderPreference) {
        this.preference = preference
        showPreference()
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

    private fun showPreference() {
        showAddress()

        showShipping()

        showPayment()
    }

    @SuppressLint("SetTextI18n")
    private fun showShipping() {
        val shipping = shipment

        binding.apply {
            if (shipping?.serviceName == null) {
                tvShippingDuration.gone()
                tvShippingDurationEta.gone()
                btnChangeDuration.gone()
                tvShippingCourier.gone()
                tvShippingPrice.gone()
                tvShippingCourierEta.gone()
                btnChangeCourier.gone()
                tvShippingErrorMessage.gone()
                btnReloadShipping.gone()
                iconReloadShipping.gone()
                tickerShippingPromo.gone()
                loaderShipping.visible()
            } else {
                loaderShipping.gone()
                if (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank()) {
                    tvShippingDuration.text = binding.root.context.getString(R.string.lbl_shipping_with_name, shipping.serviceName)
                    tvShippingDuration.visible()
                    tvShippingDurationEta.gone()
                    setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                        val shippingRecommendationData = shipment?.shippingRecommendationData
                        if (shippingRecommendationData != null) {
                            val list: ArrayList<RatesViewModelType> = ArrayList(shippingRecommendationData.shippingDurationViewModels)
                            if (shippingRecommendationData.logisticPromo != null) {
                                list.add(0, shippingRecommendationData.logisticPromo)
                                if (shippingRecommendationData.logisticPromo.disabled && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[0]) && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[1])) {
                                    orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
                                }
                            }
                            listener.chooseDuration(false, shipping.getRealShipperProductId().toString(), list)
                        }
                    }
                    btnChangeDuration.visible()
                    tvShippingCourier.text = shipping.shipperName
                    tvShippingCourier.visible()
                    btnChangeCourier.visible()
                    tvShippingErrorMessage.gone()
                    btnReloadShipping.gone()
                    iconReloadShipping.gone()

                    renderBboTicker(shipping)

                    if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
                        tvShippingCourier.text = binding.root.context.getString(R.string.lbl_shipping_with_name, shipping.logisticPromoViewModel.title)
                        tvShippingDuration.gone()
                        btnChangeDuration.gone()
                        if (shipping.logisticPromoViewModel.benefitAmount >= shipping.logisticPromoViewModel.shippingRate) {
                            tvShippingPrice.gone()
                        } else {
                            val originalPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.shippingRate, false).removeDecimalSuffix()
                            val finalPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.discountedRate, false).removeDecimalSuffix()
                            val span = SpannableString("($originalPrice $finalPrice)")
                            span.setSpan(StrikethroughSpan(), 1, 1 + originalPrice.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            span.setSpan(RelativeSizeSpan(10 / 12f), 1, 1 + originalPrice.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            span.setSpan(StyleSpan(BOLD), 0, 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            span.setSpan(StyleSpan(BOLD), 1 + originalPrice.length, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            binding.root.context?.let {
                                val color = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
                                span.setSpan(ForegroundColorSpan(color), 0, 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                                span.setSpan(ForegroundColorSpan(color), 1 + originalPrice.length, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                            tvShippingPrice.text = span
                            tvShippingPrice.visible()
                        }
                        if (shipping.logisticPromoViewModel.etaData.errorCode == 0) {
                            if (shipping.logisticPromoViewModel.etaData.textEta.isEmpty()) {
                                tvShippingCourierEta.setText(com.tokopedia.logisticcart.R.string.estimasi_tidak_tersedia)
                            } else {
                                tvShippingCourierEta.text = shipping.logisticPromoViewModel.etaData.textEta
                            }
                            tvShippingCourierEta.visible()
                        } else {
                            tvShippingCourierEta.gone()
                        }
                        setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice, tvShippingCourierEta, btnChangeCourier) {
                            val shippingRecommendationData = shipment?.shippingRecommendationData
                            if (shippingRecommendationData != null) {
                                val list: ArrayList<RatesViewModelType> = ArrayList(shippingRecommendationData.shippingDurationViewModels)
                                if (shippingRecommendationData.logisticPromo != null) {
                                    list.add(0, shippingRecommendationData.logisticPromo)
                                    if (shippingRecommendationData.logisticPromo.disabled && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[0]) && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[1])) {
                                        orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
                                    }
                                }
                                listener.chooseDuration(false, shipping.getRealShipperProductId().toString(), list)
                            }
                        }
                    } else if (shipping.shippingEta != null) {
                        tvShippingCourier.text = "${shipping.shipperName} (${
                            CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                                    ?: 0, false).removeDecimalSuffix()
                        })"
                        if (shipping.shippingEta.isNotEmpty()) {
                            tvShippingCourierEta.text = shipping.shippingEta
                        } else {
                            tvShippingCourierEta.setText(com.tokopedia.logisticcart.R.string.estimasi_tidak_tersedia)
                        }
                        tvShippingCourierEta.visible()
                        tvShippingPrice.gone()
                        setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice, tvShippingCourierEta, btnChangeCourier) {
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
                                listener.chooseCourier(list)
                            }
                        }
                    } else {
                        tvShippingPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                                ?: 0, false).removeDecimalSuffix()
                        tvShippingPrice.visible()
                        tvShippingCourierEta.gone()
                        setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice, tvShippingCourierEta, btnChangeCourier) {
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
                                listener.chooseCourier(list)
                            }
                        }
                    }
                } else if (shipping.serviceErrorMessage.isNotBlank() && shipping.shippingRecommendationData != null) {
                    tvShippingDuration.text = binding.root.context.getString(R.string.lbl_shipping_with_name, shipping.serviceName)
                    tvShippingDuration.visible()
                    if (shipping.serviceEta != null) {
                        if (shipping.serviceEta.isNotEmpty()) {
                            tvShippingDurationEta.text = shipping.serviceEta
                        } else {
                            tvShippingDurationEta.setText(com.tokopedia.logisticcart.R.string.estimasi_tidak_tersedia)
                        }
                        tvShippingDurationEta.visible()
                    } else {
                        tvShippingDurationEta.gone()
                    }
                    setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                        /* no-op */
                    }
                    btnChangeDuration.gone()
                    val button = binding.root.context.getString(R.string.lbl_change_template)
                    val span = SpannableString("${shipping.serviceErrorMessage} $button")
                    span.setSpan(StyleSpan(BOLD), shipping.serviceErrorMessage.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.root.context?.let {
                        span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), shipping.serviceErrorMessage.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    tvShippingErrorMessage.text = span
                    tvShippingErrorMessage.visible()
                    tvShippingErrorMessage.setOnClickListener {
                        val shippingRecommendationData = shipment?.shippingRecommendationData
                        if (shippingRecommendationData != null) {
                            val list: ArrayList<RatesViewModelType> = ArrayList(shippingRecommendationData.shippingDurationViewModels)
                            if (shippingRecommendationData.logisticPromo != null) {
                                list.add(0, shippingRecommendationData.logisticPromo)
                                if (shippingRecommendationData.logisticPromo.disabled && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[0]) && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[1])) {
                                    orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
                                }
                            }
                            listener.chooseDuration(true, "", list)
                        }
                    }
                    tvShippingCourier.gone()
                    btnChangeCourier.gone()
                    btnReloadShipping.gone()
                    iconReloadShipping.gone()
                    tvShippingPrice.gone()
                    tvShippingCourierEta.gone()
                    tickerShippingPromo.gone()
                } else {
                    tvShippingDuration.text = binding.root.context.getString(R.string.lbl_shipping)
                    tvShippingDuration.visible()
                    tvShippingDurationEta.gone()
                    btnChangeDuration.gone()
                    setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                        /* no-op */
                    }
                    tvShippingCourier.gone()
                    btnChangeCourier.gone()
                    tvShippingErrorMessage.text = shipping.serviceErrorMessage
                    tvShippingErrorMessage.visible()
                    tvShippingErrorMessage.setOnClickListener {
                        /* no-op */
                    }
                    setMultiViewsOnClickListener(iconReloadShipping, btnReloadShipping) {
                        listener.reloadShipping()
                    }
                    btnReloadShipping.visible()
                    iconReloadShipping.visible()
                    tvShippingPrice.gone()
                    tvShippingCourierEta.gone()
                    tickerShippingPromo.gone()
                }
            }
        }
    }

    private fun renderBboTicker(shipping: OrderShipment) {
        binding.apply {
            val logisticPromo = shipping.shippingRecommendationData?.logisticPromo
            if (shipping.logisticPromoTickerMessage?.isNotEmpty() == true && logisticPromo != null) {
                if (logisticPromo.etaData.errorCode == 0) {
                    if (logisticPromo.etaData.textEta.isEmpty()) {
                        tickerShippingPromoSubtitle.setText(com.tokopedia.logisticcart.R.string.estimasi_tidak_tersedia)
                    } else {
                        tickerShippingPromoSubtitle.text = logisticPromo.etaData.textEta
                    }
                    tickerShippingPromoTitle.text = "${shipping.logisticPromoTickerMessage}"
                    tickerShippingPromoTitle.visible()
                    tickerShippingPromoSubtitle.visible()
                    tickerShippingPromoDescription.gone()
                } else {
                    tickerShippingPromoDescription.text = "${shipping.logisticPromoTickerMessage}"
                    tickerShippingPromoDescription.visible()
                    tickerShippingPromoTitle.gone()
                    tickerShippingPromoSubtitle.gone()
                }
                tickerShippingPromo.visible()
                tickerAction.setOnClickListener {
                    listener.onLogisticPromoClick(shipping.shippingRecommendationData.logisticPromo)
                }
            } else {
                tickerShippingPromo.gone()
            }
        }
    }

    private fun showPayment() {
        val paymentModel = preference.preference.payment

        binding.apply {
            ivPayment.let {
                ImageHandler.loadImageFitCenter(binding.root.context, it, paymentModel.image)
            }
            tvPaymentName.text = paymentModel.gatewayName
            val description = paymentModel.description
            if (description.isNotBlank()) {
                tvPaymentDetail.text = description.replace('*', '\u2022')
                tvPaymentDetail.visible()
            } else {
                tvPaymentDetail.gone()
            }

            if (paymentModel.tickerMessage.isNotBlank()) {
                tvPaymentInfo.text = MethodChecker.fromHtml(paymentModel.tickerMessage)
                tvPaymentInfo.visible()
            } else {
                tvPaymentInfo.gone()
            }

            val payment = payment
            if (payment != null) {
                setupPaymentSelector(payment)
                if (!payment.isError()) {
                    tvPaymentErrorMessage.gone()
                    tvPaymentOvoErrorAction.gone()
                    setPaymentActiveAlpha()
                    setupPaymentInstallment(payment.creditCard)
                } else {
                    if (payment.errorData != null) {
                        // general & cc error
                        val message = payment.errorData.message
                        val button = payment.errorData.buttonText

                        val span = SpannableString("$message $button")
                        if (button.isNotBlank()) {
                            span.setSpan(StyleSpan(BOLD), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            binding.root.context?.let {
                                span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                            tvPaymentErrorMessage.setOnClickListener {
                                when (payment.errorData.action) {
                                    OrderPaymentErrorData.ACTION_CHANGE_PAYMENT -> listener.choosePayment(preference)
                                    OrderPaymentErrorData.ACTION_CHANGE_CC -> listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
                                }
                            }
                        }
                        tvPaymentErrorMessage.text = span
                        tvPaymentErrorMessage.visible()
                        btnChangePayment.invisible()
                        tvPaymentOvoErrorAction.gone()
                        setPaymentErrorAlpha()
                    } else if (payment.ovoErrorData != null) {
                        // ovo error
                        val message = payment.ovoErrorData.message
                        val button = payment.ovoErrorData.buttonTitle

                        val span = SpannableString("$message $button")
                        if (message.isBlank() && button.isNotBlank()) {
                            // only show button
                            tvPaymentOvoErrorAction.setOnClickListener {
                                if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_TOP_UP) {
                                    listener.onOvoTopUpClicked(payment.ovoErrorData.callbackUrl, payment.ovoErrorData.isHideDigital, payment.ovoData.customerData)
                                } else if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_ACTIVATION) {
                                    listener.onOvoActivateClicked(payment.ovoErrorData.callbackUrl)
                                }
                            }
                            tvPaymentOvoErrorAction.text = button
                            tvPaymentOvoErrorAction.visible()
                            tvPaymentDetail.gone()
                            tvPaymentErrorMessage.gone()
                        } else if (message.isNotBlank() && button.isNotBlank()) {
                            // show message and button
                            span.setSpan(StyleSpan(BOLD), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            binding.root.context?.let {
                                span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), message.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                            tvPaymentErrorMessage.setOnClickListener {
                                if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_TOP_UP) {
                                    listener.onOvoTopUpClicked(payment.ovoErrorData.callbackUrl, payment.ovoErrorData.isHideDigital, payment.ovoData.customerData)
                                } else if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_ACTIVATION) {
                                    listener.onOvoActivateClicked(payment.ovoErrorData.callbackUrl)
                                }
                            }
                            tvPaymentErrorMessage.text = span
                            tvPaymentErrorMessage.visible()
                            tvPaymentOvoErrorAction.gone()
                            if (payment.ovoErrorData.type != OrderPaymentOvoErrorData.TYPE_TOP_UP) {
                                tvPaymentDetail.gone()
                            }
                        } else {
                            // only show message
                            tvPaymentErrorMessage.setOnClickListener {
                                /* no-op */
                            }
                            tvPaymentErrorMessage.text = span
                            tvPaymentErrorMessage.visible()
                            tvPaymentDetail.gone()
                            tvPaymentOvoErrorAction.gone()
                        }
                        btnChangePayment.visible()
                        if (payment.ovoErrorData.isBlockingError) {
                            setPaymentErrorAlpha()
                        } else {
                            setPaymentActiveAlpha()
                        }
                    } else {
                        // fallback
                        tvPaymentErrorMessage.gone()
                        tvPaymentOvoErrorAction.gone()
                        setPaymentErrorAlpha()
                    }
                    tvInstallmentType.gone()
                    tvInstallmentDetail.gone()
                    btnChangeInstallment.gone()
                    tvInstallmentErrorMessage.gone()
                    tvInstallmentErrorAction.gone()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupPaymentInstallment(creditCard: OrderPaymentCreditCard) {
        val selectedTerm = creditCard.selectedTerm
        binding.apply {
            if (!creditCard.isDebit && selectedTerm != null) {
                tvInstallmentType.visible()
                tvInstallmentDetail.visible()
                btnChangeInstallment.visible()
                if (selectedTerm.term > 0) {
                    tvInstallmentDetail.text = "${selectedTerm.term} Bulan x ${CurrencyFormatUtil.convertPriceValueToIdrFormat(selectedTerm.monthlyAmount, false).removeDecimalSuffix()}"
                } else {
                    tvInstallmentDetail.text = binding.root.context.getString(R.string.lbl_installment_full_payment)
                }
                setupPaymentInstallmentError(selectedTerm)
                setMultiViewsOnClickListener(tvInstallmentType, tvInstallmentDetail, btnChangeInstallment) {
                    val creditCard = payment?.creditCard
                    if (creditCard != null && creditCard.availableTerms.isNotEmpty()) {
                        listener.onInstallmentDetailClicked(creditCard)
                    }
                }
            } else {
                tvInstallmentType.gone()
                tvInstallmentDetail.gone()
                btnChangeInstallment.gone()
                tvInstallmentErrorMessage.gone()
                tvInstallmentErrorAction.gone()
            }
        }
    }

    private fun setupPaymentInstallmentError(selectedTerm: OrderPaymentInstallmentTerm) {
        binding.apply {
            if (selectedTerm.isError) {
                tvInstallmentDetail.alpha = 0.5f
                tvInstallmentErrorMessage.text = binding.root.context.getString(R.string.lbl_installment_error)
                tvInstallmentErrorAction.text = binding.root.context.getString(R.string.lbl_change_template)
                tvInstallmentErrorAction.setOnClickListener {
                    val creditCard = payment?.creditCard
                    if (creditCard != null && creditCard.availableTerms.isNotEmpty()) {
                        listener.onInstallmentDetailClicked(creditCard)
                    }
                }
                tvInstallmentErrorMessage.visible()
                tvInstallmentErrorAction.visible()
            } else {
                tvInstallmentDetail.alpha = 1.0f
                tvInstallmentErrorMessage.gone()
                tvInstallmentErrorAction.gone()
            }
        }
    }

    private fun setupPaymentSelector(payment: OrderPayment) {
        binding.apply {
            if (payment.creditCard.numberOfCards.availableCards > 1) {
                showPaymentCC(payment)
                btnChangePayment.visible()
                setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                    listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
                }
            } else if (payment.creditCard.bankCode.isNotEmpty()) {
                showPaymentCC(payment)
                btnChangePayment.invisible()
                setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                    /* no-op */
                }
            } else {
                hidePaymentCC()
                btnChangePayment.visible()
                setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                    listener.choosePayment(preference)
                }
            }
        }
    }

    private fun setPaymentErrorAlpha() {
        binding.apply {
            ivPayment.alpha = 0.5f
            tvPaymentName.alpha = 0.5f
            tvPaymentDetail.alpha = 0.5f
        }
    }

    private fun setPaymentActiveAlpha() {
        binding.apply {
            ivPayment.alpha = 1f
            tvPaymentName.alpha = 1f
            tvPaymentDetail.alpha = 1f
        }
    }

    private fun showPaymentCC(payment: OrderPayment) {
        binding.apply {
            tvPaymentCcName.text = payment.gatewayName
            tvPaymentCcName.visible()
            btnChangePaymentCc.visible()
            dividerCcPayment.visible()
            setMultiViewsOnClickListener(tvPaymentCcName, btnChangePaymentCc) {
                listener.choosePayment(preference)
            }
        }
    }

    private fun hidePaymentCC() {
        binding.apply {
            tvPaymentCcName.gone()
            btnChangePaymentCc.gone()
            dividerCcPayment.gone()
        }
    }

    private fun setMultiViewsOnClickListener(vararg views: View?, onClickListener: () -> Unit) {
        for (view in views) {
            view?.setOnClickListener {
                onClickListener.invoke()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAddress() {
        val addressModel = preference.preference.address
        val receiverName = addressModel.receiverName
        val phone = addressModel.phone
        var receiverText = ""
        if (receiverName.isNotBlank()) {
            receiverText = " - $receiverName"
            if (phone.isNotBlank()) {
                receiverText = "$receiverText ($phone)"
            }
        }
        val span = SpannableString(addressModel.addressName + receiverText)
        span.setSpan(StyleSpan(BOLD), 0, addressModel.addressName.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.apply {
            tvAddressName.text = span
            tvAddressDetail.text = "${addressModel.addressStreet}, ${addressModel.districtName}, ${addressModel.cityName}, ${addressModel.provinceName} ${addressModel.postalCode}"

            lblMainAddress.visibility = if (addressModel.isMainAddress) View.VISIBLE else View.GONE
            setAddressNameMargin(addressModel.isMainAddress)

            setMultiViewsOnClickListener(tvAddressName, tvAddressDetail, btnChangeAddress) {
                listener.chooseAddress(addressModel.addressId.toString())
            }
        }
    }

    private fun setAddressNameMargin(isMainAddress: Boolean) {
        val displayMetrics = binding.tvAddressName.context?.resources?.displayMetrics ?: return
        binding.tvAddressName.setMargin(if (isMainAddress) MAIN_ADDRESS_LEFT_MARGIN.dpToPx(displayMetrics) else NOT_MAIN_ADDRESS_LEFT_MARGIN.dpToPx(displayMetrics), ADDRESS_TOP_MARGIN.dpToPx(displayMetrics), ADDRESS_RIGHT_MARGIN, ADDRESS_BOTTOM_MARGIN)
    }

    fun showAddressBottomSheet(fragment: OrderSummaryPageFragment, usecase: GetAddressCornerUseCase, addressState: Int) {
        AddressListBottomSheet(usecase, object : AddressListBottomSheet.AddressListBottomSheetListener {
            override fun onSelect(addressModel: RecipientAddressModel) {
                listener.onAddressChange(addressModel)
            }

            override fun onAddAddress(token: Token?) {
                listener.onAddAddress(token)
            }
        }).show(fragment, preference.preference.address.addressId.toString(), addressState)
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
        const val VIEW_TYPE = 4

        private val BBO_DESCRIPTION_MINIMUM_LIMIT = arrayOf("belum", "min")

        private const val MAIN_ADDRESS_LEFT_MARGIN = 8
        private const val NOT_MAIN_ADDRESS_LEFT_MARGIN = 16
        private const val ADDRESS_TOP_MARGIN = 12
        private const val ADDRESS_RIGHT_MARGIN = 0
        private const val ADDRESS_BOTTOM_MARGIN = 0
    }

    interface OrderPreferenceCardListener {

        fun onAddAddress(token: Token?)

        fun onAddressChange(addressModel: RecipientAddressModel)

        fun onCourierChange(shippingCourierViewModel: ShippingCourierUiModel)

        fun onDurationChange(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean)

        fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel)

        fun reloadShipping()

        fun chooseAddress(currentAddressId: String)

        fun chooseCourier(list: ArrayList<RatesViewModelType>)

        fun chooseDuration(isDurationError: Boolean, currentSpId: String, list: ArrayList<RatesViewModelType>)

        fun choosePayment(preference: OrderPreference)

        fun onInstallmentDetailClicked(creditCard: OrderPaymentCreditCard)

        fun onInstallmentDetailChange(selectedInstallmentTerm: OrderPaymentInstallmentTerm)

        fun onChangeCreditCardClicked(additionalData: OrderPaymentCreditCardAdditionalData)

        fun onOvoActivateClicked(callbackUrl: String)

        fun onOvoTopUpClicked(callbackUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData)
    }
}