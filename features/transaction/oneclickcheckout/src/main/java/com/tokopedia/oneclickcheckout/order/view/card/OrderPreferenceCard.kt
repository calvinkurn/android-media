package com.tokopedia.oneclickcheckout.order.view.card

import android.annotation.SuppressLint
import android.graphics.Typeface.BOLD
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.CardOrderPreferenceBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderPreferenceCard(val binding: CardOrderPreferenceBinding, private val listener: OrderPreferenceCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) : RecyclerView.ViewHolder(binding.root) {

    private var shop: OrderShop = OrderShop()
    private var profile: OrderProfile = OrderProfile()
    private var shipment: OrderShipment = OrderShipment()
    private var payment: OrderPayment = OrderPayment()

    fun setPreferenceData(shop: OrderShop, profile: OrderProfile, shipment: OrderShipment, payment: OrderPayment) {
        this.shop = shop
        this.profile = profile
        this.shipment = shipment
        this.payment = payment
        showPreference()
    }

    private fun showPreference() {
        showPreferenceTicker()

        showAddress()

        showShipping()

        showPayment()

        binding.root.alpha = if (profile.enable) ENABLE_ALPHA else DISABLE_ALPHA
    }

    private fun showPreferenceTicker() {
        binding.tickerPreferenceInfo.tickerTitle = null
        binding.tickerPreferenceInfo.setHtmlDescription(profile.message)
        binding.tickerPreferenceInfo.closeButtonVisibility = View.GONE
        binding.tickerPreferenceInfo.visibility = if (profile.message.isNotBlank()) View.VISIBLE else View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun showShipping() {
        val shipping = shipment

        binding.apply {
            if (!profile.enable || shipping.isDisabled) {
                renderDisabledShipping()
            } else if (shipping.isLoading || shipping.serviceName == null) {
                renderLoadingShipping()
            } else if (!profile.shipment.isDisableChangeCourier) {
                loaderShipping.gone()
                if (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank()) {
                    renderShippingDuration(shipping)
                    renderBboTicker(shipping)
                    if (shipping.isApplyLogisticPromo && shipping.logisticPromoShipping != null && shipping.logisticPromoList.any { it.isApplied }) {
                        renderBboShipping(shipping, shipping.logisticPromoList.first { it.isApplied })
                    } else if (shipping.shippingEta != null) {
                        renderShippingCourierWithEta(shipping, shipping.shippingEta)
                    } else {
                        renderShippingCourierWithoutEta(shipping)
                    }
                } else if (shipping.serviceErrorMessage.isNotBlank() && shipping.shippingRecommendationData != null) {
                    renderShippingWithErrorMessage(shipping, shipping.serviceErrorMessage)
                } else {
                    renderErrorShipping(shipping)
                }
            } else {
                loaderShipping.gone()
                if (shipping.needPinpoint) {
                    renderShippingPinpointError()
                } else if (shipping.shipperName != null && (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank())) {
                    renderSingleShipping(shipping, shipping.shipperName)
                } else {
                    renderErrorShipping(shipping)
                }
            }
        }
    }

    private fun renderDisabledShipping() {
        binding.apply {
            tvShippingDuration.gone()
            tvShippingDurationEta.gone()
            btnChangeDuration.gone()
            tvShippingCourierEta.gone()
            tvShippingCourierNotes.gone()
            btnChangeCourier.gone()
            tvShippingErrorMessage.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
            tickerShippingPromo.gone()
            loaderShipping.gone()
            tvShippingCourier.text = profile.shipment.courierSelectionError.title
            tvShippingPrice.text = profile.shipment.courierSelectionError.description
            tvShippingCourier.visible()
            tvShippingPrice.visible()
            setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice) {
                /* no-op */
            }
        }
    }

    private fun renderLoadingShipping() {
        binding.apply {
            setInvisible(tvShippingDuration)
            setInvisible(tvShippingDurationEta)
            setInvisible(btnChangeDuration)
            setInvisible(tvShippingCourier)
            setInvisible(tvShippingPrice)
            setInvisible(tvShippingCourierEta)
            setInvisible(tvShippingCourierNotes)
            setInvisible(btnChangeCourier)
            setInvisible(tvShippingErrorMessage)
            setInvisible(btnReloadShipping)
            setInvisible(iconReloadShipping)
            setInvisible(tickerShippingPromo)
            loaderShipping.visible()
        }
    }

    private fun setInvisible(view: View) {
        if (view.isVisible) {
            view.invisible()
        }
    }

    private fun renderShippingDuration(shipping: OrderShipment) {
        binding.apply {
            tvShippingDuration.text = binding.root.context.getString(R.string.lbl_shipping_with_name, shipping.serviceName)
            tvShippingDuration.visible()
            tvShippingDurationEta.gone()
            tvShippingCourierNotes.gone()
            setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                if (profile.enable) {
                    val shippingRecommendationData = shipment.shippingRecommendationData
                    if (shippingRecommendationData != null) {
                        val list: ArrayList<RatesViewModelType> = ArrayList(shippingRecommendationData.shippingDurationUiModels)
                        val listLogisticPromo = shippingRecommendationData.listLogisticPromo
                        if (listLogisticPromo.isNotEmpty()) {
                            listLogisticPromo.forEachIndexed { index, logisticPromoUiModel ->  list.add(index, logisticPromoUiModel)}
                            if (listLogisticPromo.any { logisticPromo ->
                                    logisticPromo.disabled && logisticPromo.description.contains(
                                        BBO_DESCRIPTION_MINIMUM_LIMIT[0]
                                    ) && logisticPromo.description.contains(
                                        BBO_DESCRIPTION_MINIMUM_LIMIT[1]
                                    )
                                }) {
                                orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
                            }
                        }
                        listener.chooseDuration(false, shipping.getRealShipperProductId().toString(), list)
                    }
                }
            }
            btnChangeDuration.visible()
            tvShippingCourier.text = shipping.shipperName
            tvShippingCourier.visible()
            btnChangeCourier.visible()
            tvShippingErrorMessage.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
        }
    }

    private fun renderBboTicker(shipping: OrderShipment) {
        binding.apply {
            val logisticPromo = shipping.shippingRecommendationData?.logisticPromo
            if (shipping.logisticPromoTickerMessage?.isNotEmpty() == true && logisticPromo != null) {
                val formattedLogisticPromoTickerMessage = HtmlLinkHelper(tickerShippingPromoTitle.context, shipping.logisticPromoTickerMessage).spannedString
                if (logisticPromo.etaData.errorCode == 0 && !logisticPromo.isBebasOngkirExtra) {
                    if (logisticPromo.etaData.textEta.isEmpty()) {
                        tickerShippingPromoSubtitle.setText(com.tokopedia.logisticcart.R.string.estimasi_tidak_tersedia)
                    } else {
                        tickerShippingPromoSubtitle.text = logisticPromo.etaData.textEta
                    }
                    tickerShippingPromoTitle.text = formattedLogisticPromoTickerMessage
                    tickerShippingPromoTitle.visible()
                    tickerShippingPromoSubtitle.visible()
                    tickerShippingPromoDescription.gone()
                } else {
                    tickerShippingPromoDescription.text = formattedLogisticPromoTickerMessage
                    tickerShippingPromoDescription.visible()
                    tickerShippingPromoTitle.gone()
                    tickerShippingPromoSubtitle.gone()
                }
                tickerShippingPromo.visible()
                tickerAction.setOnClickListener {
                    if (profile.enable) {
                        listener.onLogisticPromoClick(shipping.shippingRecommendationData.logisticPromo!!)
                    }
                }
            } else {
                tickerShippingPromo.gone()
            }
        }
    }

    private fun renderBboShipping(shipping: OrderShipment, logisticPromoViewModel: LogisticPromoUiModel) {
        binding.apply {
            val formattedFreeShippingChosenCourierTitle = HtmlLinkHelper(tvShippingCourier.context, logisticPromoViewModel.freeShippingChosenCourierTitle).spannedString
            tvShippingCourier.text = formattedFreeShippingChosenCourierTitle
            tvShippingDuration.gone()
            btnChangeDuration.gone()
            tvShippingCourierNotes.gone()
            tvShippingPrice.gone()
            if (logisticPromoViewModel.etaData.errorCode == 0 && !logisticPromoViewModel.isBebasOngkirExtra) {
                if (logisticPromoViewModel.etaData.textEta.isEmpty()) {
                    tvShippingCourierEta.setText(com.tokopedia.logisticcart.R.string.estimasi_tidak_tersedia)
                } else {
                    tvShippingCourierEta.text = logisticPromoViewModel.etaData.textEta
                }
                tvShippingCourierEta.visible()
            } else {
                tvShippingCourierEta.gone()
            }
            setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice, tvShippingCourierEta, btnChangeCourier) {
                if (profile.enable) {
                    val shippingRecommendationData = shipment.shippingRecommendationData
                    if (shippingRecommendationData != null) {
                        val list: ArrayList<RatesViewModelType> = ArrayList(shippingRecommendationData.shippingDurationUiModels)
                        val listLogisticPromo = shippingRecommendationData.listLogisticPromo
                        if (listLogisticPromo.isNotEmpty()) {
                            listLogisticPromo.forEachIndexed { index, logisticPromoUiModel ->  list.add(index, logisticPromoUiModel)}
                            if (listLogisticPromo.any { logisticPromo ->
                                    logisticPromo.disabled && logisticPromo.description.contains(
                                        BBO_DESCRIPTION_MINIMUM_LIMIT[0]
                                    ) && logisticPromo.description.contains(
                                        BBO_DESCRIPTION_MINIMUM_LIMIT[1]
                                    )
                                }) {
                                orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
                            }
                        }
                        listener.chooseDuration(false, shipping.getRealShipperProductId().toString(), list)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderShippingCourierWithEta(shipping: OrderShipment, eta: String) {
        binding.apply {
            tvShippingCourier.text = "${shipping.shipperName} (${
                CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                        ?: 0, false).removeDecimalSuffix()
            })"
            if (eta.isNotEmpty()) {
                tvShippingCourierEta.text = eta
            } else {
                tvShippingCourierEta.setText(com.tokopedia.logisticcart.R.string.estimasi_tidak_tersedia)
            }
            tvShippingCourierEta.visible()
            tvShippingCourierNotes.gone()
            tvShippingPrice.gone()
            setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice, tvShippingCourierEta, btnChangeCourier) {
                if (profile.enable) {
                    val shippingRecommendationData = shipment.shippingRecommendationData
                    if (shippingRecommendationData != null) {
                        val list: ArrayList<RatesViewModelType> = ArrayList()
                        for (shippingDurationViewModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationViewModel.isSelected) {
                                if (shippingDurationViewModel.shippingCourierViewModelList.isNotEmpty() && isCourierInstantOrSameday(shippingDurationViewModel.shippingCourierViewModelList[0].productData.shipperId)) {
                                    list.add(NotifierModel())
                                }
                                list.addAll(shippingDurationViewModel.shippingCourierViewModelList)
                                break
                            }
                        }
                        listener.chooseCourier(shipping, list)
                    }
                }
            }
        }
    }

    private fun renderShippingCourierWithoutEta(shipping: OrderShipment) {
        binding.apply {
            tvShippingPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                    ?: 0, false).removeDecimalSuffix()
            tvShippingPrice.visible()
            tvShippingCourierEta.gone()
            tvShippingCourierNotes.gone()
            setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice, tvShippingCourierEta, btnChangeCourier) {
                if (profile.enable) {
                    val shippingRecommendationData = shipment.shippingRecommendationData
                    if (shippingRecommendationData != null) {
                        val list: ArrayList<RatesViewModelType> = ArrayList()
                        for (shippingDurationViewModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationViewModel.isSelected) {
                                if (shippingDurationViewModel.shippingCourierViewModelList.isNotEmpty() && isCourierInstantOrSameday(shippingDurationViewModel.shippingCourierViewModelList[0].productData.shipperId)) {
                                    list.add(NotifierModel())
                                }
                                list.addAll(shippingDurationViewModel.shippingCourierViewModelList)
                                break
                            }
                        }
                        listener.chooseCourier(shipping, list)
                    }
                }
            }
        }
    }

    private fun renderShippingWithErrorMessage(shipping: OrderShipment, errorMessage: String) {
        binding.apply {
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
            val span = SpannableString("$errorMessage $button")
            span.setSpan(StyleSpan(BOLD), errorMessage.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.root.context?.let {
                span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), errorMessage.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tvShippingErrorMessage.text = span
            tvShippingErrorMessage.visible()
            tvShippingErrorMessage.setOnClickListener {
                if (profile.enable) {
                    val shippingRecommendationData = shipment.shippingRecommendationData
                    if (shippingRecommendationData != null) {
                        val list: ArrayList<RatesViewModelType> = ArrayList(shippingRecommendationData.shippingDurationUiModels)
                        val listLogisticPromo = shippingRecommendationData.listLogisticPromo
                        if (listLogisticPromo.isNotEmpty()) {
                            listLogisticPromo.forEachIndexed { index, logisticPromoUiModel ->  list.add(index, logisticPromoUiModel)}
                            if (listLogisticPromo.any { logisticPromo ->
                                    logisticPromo.disabled && logisticPromo.description.contains(
                                        BBO_DESCRIPTION_MINIMUM_LIMIT[0]
                                    ) && logisticPromo.description.contains(
                                        BBO_DESCRIPTION_MINIMUM_LIMIT[1]
                                    )
                                }) {
                                orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
                            }
                        }
                        listener.chooseDuration(true, "", list)
                    }
                }
            }
            tvShippingCourier.gone()
            btnChangeCourier.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
            tvShippingPrice.gone()
            tvShippingCourierEta.gone()
            tvShippingCourierNotes.gone()
            tickerShippingPromo.gone()
        }
    }

    private fun renderErrorShipping(shipping: OrderShipment) {
        binding.apply {
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
                if (profile.enable) {
                    listener.reloadShipping(shop.shopId.toString())
                }
            }
            btnReloadShipping.visible()
            iconReloadShipping.visible()
            tvShippingPrice.gone()
            tvShippingCourierNotes.gone()
            tvShippingCourierEta.gone()
            tickerShippingPromo.gone()
        }
    }

    private fun renderShippingPinpointError() {
        binding.apply {
            tvShippingDuration.gone()
            tvShippingDurationEta.gone()
            btnChangeDuration.gone()
            tvShippingCourierEta.gone()
            tvShippingCourierNotes.gone()
            btnChangeCourier.gone()
            tvShippingErrorMessage.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
            tickerShippingPromo.gone()
            loaderShipping.gone()
            tvShippingCourier.text = root.context.getString(R.string.occ_pinpoint_error_title)
            val errorDescription = root.context.getString(R.string.occ_pinpoint_error_description)
            val buttonText = root.context.getString(R.string.occ_pinpoint_error_action)
            val span = SpannableString("$errorDescription $buttonText")
            span.setSpan(StyleSpan(BOLD), errorDescription.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.root.context?.let {
                span.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), errorDescription.length + 1, span.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tvShippingPrice.text = span
            tvShippingCourier.visible()
            tvShippingPrice.visible()
            setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice) {
                if (profile.enable) {
                    listener.choosePinpoint(profile.address)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderSingleShipping(shipping: OrderShipment, shipperName: String) {
        binding.apply {
            tvShippingDuration.gone()
            tvShippingDurationEta.gone()
            btnChangeDuration.gone()
            tvShippingPrice.gone()
            btnChangeCourier.gone()
            tvShippingErrorMessage.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
            tickerShippingPromo.gone()
            loaderShipping.gone()
            // todo no need to change if logisticpromoviewmodel == listlogisticpromo[0] is always true
            if (shipping.isApplyLogisticPromo && shipping.logisticPromoShipping != null && shipping.logisticPromoViewModel != null) {
                val formattedFreeShippingChosenCourierTitle = HtmlLinkHelper(tvShippingCourier.context, shipping.logisticPromoViewModel.freeShippingChosenCourierTitle).spannedString
                tvShippingCourier.text = formattedFreeShippingChosenCourierTitle
            } else {
                tvShippingCourier.text = "$shipperName (${
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice ?: 0, false).removeDecimalSuffix()
                })"
            }
            if (shipping.shippingEta.isNullOrBlank()) {
                tvShippingCourierEta.setText(com.tokopedia.logisticcart.R.string.estimasi_tidak_tersedia)
            } else {
                tvShippingCourierEta.text = shipping.shippingEta
            }
            if (shipping.logisticPromoViewModel?.description?.isNotBlank() == true) {
                tvShippingCourierNotes.text = MethodChecker.fromHtml(shipping.logisticPromoViewModel.description)
                tvShippingCourierNotes.visible()
                if (!shipping.hasTriggerViewMessageTracking) {
                    orderSummaryAnalytics.eventViewMessageInCourier2JamSampai(shipping.logisticPromoViewModel.description)
                    shipping.hasTriggerViewMessageTracking = true
                }
            } else {
                tvShippingCourierNotes.gone()
            }
            tvShippingCourier.visible()
            tvShippingCourierEta.visible()
            setMultiViewsOnClickListener(tvShippingCourier, tvShippingCourierEta, tvShippingCourierNotes) {
                /* no-op */
            }
        }
    }

    private fun showPayment() {
        val paymentModel = profile.payment

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
                            if (profile.enable) {
                                when (payment.errorData.action) {
                                    OrderPaymentErrorData.ACTION_CHANGE_PAYMENT -> listener.choosePayment(profile, payment)
                                    OrderPaymentErrorData.ACTION_CHANGE_CC -> listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
                                }
                            }
                        }
                    }
                    tvPaymentErrorMessage.text = span
                    tvPaymentErrorMessage.visible()
                    btnChangePayment.invisible()
                    tvPaymentOvoErrorAction.gone()
                    setPaymentErrorAlpha()
                } else if (payment.walletErrorData != null) {
                    // ovo error
                    val message = payment.walletErrorData.message
                    val button = payment.walletErrorData.buttonTitle

                    val span = SpannableString("$message $button")
                    if (message.isBlank() && button.isNotBlank()) {
                        // only show button
                        tvPaymentOvoErrorAction.setOnClickListener {
                            if (profile.enable) {
                                if (payment.walletErrorData.type == OrderPaymentWalletErrorData.TYPE_TOP_UP) {
                                    if (payment.isOvo) {
                                        listener.onOvoTopUpClicked(payment.walletErrorData.callbackUrl, payment.walletErrorData.isHideDigital, payment.ovoData.customerData)
                                    } else {
                                        listener.onWalletTopUpClicked(payment.walletData.walletType, payment.walletData.topUp.urlLink, payment.walletData.callbackUrl,
                                                payment.walletErrorData.isHideDigital, payment.walletData.topUp.headerTitle)
                                    }
                                } else if (payment.walletErrorData.type == OrderPaymentWalletErrorData.TYPE_ACTIVATION) {
                                    if (payment.isOvo) {
                                        listener.onOvoActivateClicked(payment.walletErrorData.callbackUrl)
                                    } else {
                                        listener.onWalletActivateClicked(payment.walletData.activation.headerTitle, payment.walletData.activation.urlLink, payment.walletData.callbackUrl)
                                    }
                                }
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
                            if (profile.enable) {
                                if (payment.walletErrorData.type == OrderPaymentWalletErrorData.TYPE_TOP_UP) {
                                    if (payment.isOvo) {
                                        listener.onOvoTopUpClicked(payment.walletErrorData.callbackUrl, payment.walletErrorData.isHideDigital, payment.ovoData.customerData)
                                    } else {
                                        listener.onWalletTopUpClicked(payment.walletData.walletType, payment.walletData.topUp.urlLink, payment.walletData.callbackUrl,
                                                payment.walletErrorData.isHideDigital, payment.walletData.topUp.headerTitle)
                                    }
                                } else if (payment.walletErrorData.type == OrderPaymentWalletErrorData.TYPE_ACTIVATION) {
                                    if (payment.isOvo) {
                                        listener.onOvoActivateClicked(payment.walletErrorData.callbackUrl)
                                    } else {
                                        listener.onWalletActivateClicked(payment.walletData.activation.headerTitle, payment.walletData.activation.urlLink, payment.walletData.callbackUrl)
                                    }
                                }
                            }
                        }
                        tvPaymentErrorMessage.text = span
                        tvPaymentErrorMessage.visible()
                        tvPaymentOvoErrorAction.gone()
                        if (payment.walletErrorData.type != OrderPaymentWalletErrorData.TYPE_TOP_UP) {
                            tvPaymentDetail.gone()
                        } else if (payment.walletData.walletType == OrderPaymentWalletAdditionalData.WALLET_TYPE_GOPAY) {
                            orderSummaryAnalytics.eventViewTopUpGoPayButton()
                        }
                    } else {
                        // only show message
                        tvPaymentErrorMessage.setOnClickListener {
                            /* no-op */
                        }
                        tvPaymentErrorMessage.text = span
                        tvPaymentErrorMessage.visible()
                        if (tvPaymentDetail.text.toString().isEmpty()) {
                            tvPaymentDetail.gone()
                        }
                        tvPaymentOvoErrorAction.gone()
                    }
                    btnChangePayment.visible()
                    if (payment.walletErrorData.isBlockingError) {
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
                    if (profile.enable) {
                        val selectedCreditCard = payment.creditCard
                        if (selectedCreditCard.availableTerms.isNotEmpty()) {
                            listener.onInstallmentDetailClicked(selectedCreditCard)
                        }
                    }
                }
            } else if (!creditCard.isDebit && creditCard.isAfpb && creditCard.selectedTerm == null) {
                tvInstallmentType.visible()
                tvInstallmentDetail.visible()
                btnChangeInstallment.visible()
                tvInstallmentDetail.text = binding.root.context.getString(R.string.lbl_installment_afpb_default)
                tvInstallmentDetail.alpha = ENABLE_ALPHA
                tvInstallmentErrorMessage.gone()
                tvInstallmentErrorAction.gone()
                setMultiViewsOnClickListener(tvInstallmentType, tvInstallmentDetail, btnChangeInstallment) {
                    if (profile.enable) {
                        val selectedCreditCard = payment.creditCard
                        listener.onInstallmentDetailClicked(selectedCreditCard)
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
                tvInstallmentDetail.alpha = DISABLE_ALPHA
                tvInstallmentErrorMessage.text = binding.root.context.getString(R.string.lbl_installment_error)
                tvInstallmentErrorAction.text = binding.root.context.getString(R.string.lbl_change_template)
                tvInstallmentErrorAction.setOnClickListener {
                    if (profile.enable) {
                        val creditCard = payment.creditCard
                        if (creditCard.availableTerms.isNotEmpty()) {
                            listener.onInstallmentDetailClicked(creditCard)
                        }
                    }
                }
                tvInstallmentErrorMessage.visible()
                tvInstallmentErrorAction.visible()
            } else {
                tvInstallmentDetail.alpha = ENABLE_ALPHA
                tvInstallmentErrorMessage.gone()
                tvInstallmentErrorAction.gone()
            }
        }
    }

    private fun setupPaymentSelector(payment: OrderPayment) {
        binding.apply {
            when {
                payment.creditCard.numberOfCards.availableCards > 1 -> {
                    showPaymentCC(payment)
                    btnChangePayment.visible()
                    setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                        if (profile.enable) {
                            listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
                        }
                    }
                }
                payment.creditCard.bankCode.isNotEmpty() -> {
                    showPaymentCC(payment)
                    btnChangePayment.invisible()
                    setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                        /* no-op */
                    }
                }
                else -> {
                    hidePaymentCC()
                    btnChangePayment.visible()
                    setMultiViewsOnClickListener(ivPayment, tvPaymentName, tvPaymentDetail, btnChangePayment) {
                        if (profile.enable) {
                            listener.choosePayment(profile, payment)
                        }
                    }
                }
            }
        }
    }

    private fun setPaymentErrorAlpha() {
        binding.apply {
            ivPayment.alpha = DISABLE_ALPHA
            tvPaymentName.alpha = DISABLE_ALPHA
            tvPaymentDetail.alpha = DISABLE_ALPHA
        }
    }

    private fun setPaymentActiveAlpha() {
        binding.apply {
            ivPayment.alpha = ENABLE_ALPHA
            tvPaymentName.alpha = ENABLE_ALPHA
            tvPaymentDetail.alpha = ENABLE_ALPHA
        }
    }

    private fun showPaymentCC(payment: OrderPayment) {
        binding.apply {
            tvPaymentCcName.text = payment.gatewayName
            tvPaymentCcName.visible()
            btnChangePaymentCc.visible()
            dividerCcPayment.visible()
            setMultiViewsOnClickListener(tvPaymentCcName, btnChangePaymentCc) {
                if (profile.enable) {
                    listener.choosePayment(profile, payment)
                }
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
        val addressModel = profile.address
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
                if (profile.enable) {
                    listener.chooseAddress(addressModel.addressId.toString())
                }
            }
        }
    }

    private fun setAddressNameMargin(isMainAddress: Boolean) {
        val displayMetrics = binding.tvAddressName.context?.resources?.displayMetrics ?: return
        binding.tvAddressName.setMargin(if (isMainAddress) MAIN_ADDRESS_LEFT_MARGIN.dpToPx(displayMetrics) else NOT_MAIN_ADDRESS_LEFT_MARGIN.dpToPx(displayMetrics), ADDRESS_TOP_MARGIN.dpToPx(displayMetrics), ADDRESS_RIGHT_MARGIN, ADDRESS_BOTTOM_MARGIN)
    }

    private fun isCourierInstantOrSameday(shipperId: Int): Boolean {
        val ids = CourierConstant.INSTANT_SAMEDAY_COURIER
        for (id in ids) {
            if (shipperId == id) return true
        }
        return false
    }

    companion object {
        const val VIEW_TYPE = 4

        private val BBO_DESCRIPTION_MINIMUM_LIMIT = arrayOf("belum", "min")

        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f
        private const val FLOAT_12 = 12F
        private const val PROPOTION_10 = 10

        private const val MAIN_ADDRESS_LEFT_MARGIN = 8
        private const val NOT_MAIN_ADDRESS_LEFT_MARGIN = 16
        private const val ADDRESS_TOP_MARGIN = 12
        private const val ADDRESS_RIGHT_MARGIN = 0
        private const val ADDRESS_BOTTOM_MARGIN = 0
    }

    interface OrderPreferenceCardListener {

        fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel)

        fun reloadShipping(shopId: String)

        fun chooseAddress(currentAddressId: String)

        fun chooseCourier(shipment: OrderShipment, list: ArrayList<RatesViewModelType>)

        fun chooseDuration(isDurationError: Boolean, currentSpId: String, list: ArrayList<RatesViewModelType>)

        fun choosePinpoint(address: OrderProfileAddress)

        fun choosePayment(profile: OrderProfile, payment: OrderPayment)

        fun onInstallmentDetailClicked(creditCard: OrderPaymentCreditCard)

        fun onChangeCreditCardClicked(additionalData: OrderPaymentCreditCardAdditionalData)

        fun onOvoActivateClicked(callbackUrl: String)

        fun onWalletActivateClicked(headerTitle: String, activationUrl: String, callbackUrl: String)

        fun onOvoTopUpClicked(callbackUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData)

        fun onWalletTopUpClicked(walletType: Int, url: String, callbackUrl: String, isHideDigital: Int, title: String)
    }
}