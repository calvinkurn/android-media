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
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.CardOrderPreferenceBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCard
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCardAdditionalData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentErrorData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentOvoCustomerData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentWalletAdditionalData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentWalletErrorData
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderPreferenceCard(
    val binding: CardOrderPreferenceBinding,
    private val listener: OrderPreferenceCardListener,
    private val orderSummaryAnalytics: OrderSummaryAnalytics
) : RecyclerView.ViewHolder(binding.root) {

    private var shop: OrderShop = OrderShop()
    private var profile: OrderProfile = OrderProfile()
    private var shipment: OrderShipment = OrderShipment()
    private var payment: OrderPayment = OrderPayment()

    fun setPreferenceData(
        shop: OrderShop,
        profile: OrderProfile,
        shipment: OrderShipment,
        payment: OrderPayment
    ) {
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
        binding.tickerPreferenceInfo.visibility =
            if (profile.message.isNotBlank()) View.VISIBLE else View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun showShipping() {
        val shipping = shipment

        binding.apply {
            if (!profile.enable || shipping.isDisabled) {
                renderDisabledShipping()
            } else if (shipping.isLoading || shipping.serviceName == null) {
                shippingOccWidget.renderLoadingShipping()
            } else if (!profile.shipment.isDisableChangeCourier) {
                shippingOccWidget.hideLoaderShipping()
                if (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank()) {
                    renderShippingDuration(shipping)
                    renderBboTicker(shipping)
                    if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
                        renderBboShipping(shipping, shipping.logisticPromoViewModel)
                    } else if (shipping.isHideChangeCourierCard) {
                        renderNormalShippingWithoutChooseCourierCard(shipping)
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
                shippingOccWidget.hideLoaderShipping()
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
        binding.shippingOccWidget.renderDisabledShipping(
            courierSelectionErrorTitle = profile.shipment.courierSelectionError.title,
            courierSelectionErrorDescription = profile.shipment.courierSelectionError.description
        )
    }

    private fun renderShippingDuration(shipping: OrderShipment) {
        binding.shippingOccWidget.renderShippingDuration(
            serviceName = shipping.serviceName,
            shipperName = shipping.shipperName ?: "",
            onChangeDurationListener = {
                onChangeDuration(shipping)
            }
        )
    }

    private fun renderBboTicker(shipping: OrderShipment) {
        binding.shippingOccWidget.renderBboTicker(
            logisticPromo = shipping.shippingRecommendationData?.logisticPromo,
            logisticPromoTickerMessage = shipping.logisticPromoTickerMessage,
            onTickerClickListener = {
                if (profile.enable) {
                    shipping.shippingRecommendationData?.logisticPromo?.apply {
                        listener.onLogisticPromoClick(this)
                    }
                }
            }
        )
    }

    private fun renderBboShipping(
        shipping: OrderShipment,
        logisticPromoUiModel: LogisticPromoUiModel
    ) {
        binding.shippingOccWidget.renderBboShipping(
            logisticPromoUiModel = logisticPromoUiModel,
            onChangeDurationListener = {
                onChangeDuration(shipping)
            })
    }

    @SuppressLint("SetTextI18n")
    private fun renderNormalShippingWithoutChooseCourierCard(shipping: OrderShipment) {
        binding.shippingOccWidget.renderNormalShippingWithoutChooseCourierCard(
            serviceName = shipping.serviceName,
            shippingPrice = shipping.shippingPrice ?: 0,
            serviceEta = shipping.serviceEta,
            onChangeDurationListener = {
                onChangeDuration(shipping)
            }
        )
    }

    private fun onChangeDuration(shipping: OrderShipment) {
        if (profile.enable) {
            val shipperProductId = shipping.getRealShipperProductId().toString()
            listener.chooseDuration(shipperProductId.isEmpty(), shipperProductId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderShippingCourierWithEta(shipping: OrderShipment, eta: String) {
        binding.shippingOccWidget.renderShippingCourierWithEta(
            shipperName = shipping.shipperName,
            shippingPrice = shipping.shippingPrice ?: 0,
            eta = eta,
            onChangeCourierListener = {
                onChangeCourier(shipping)
            }
        )
    }

    private fun onChangeCourier(shipping: OrderShipment) {
        if (profile.enable) {
            val shippingRecommendationData = shipment.shippingRecommendationData
            if (shippingRecommendationData != null) {
                val courierList: List<ShippingCourierUiModel> =
                    shippingRecommendationData.shippingDurationUiModels.find { it.isSelected }?.shippingCourierViewModelList
                        ?: listOf()
                listener.chooseCourier(shipping, ArrayList(courierList))
            }
        }
    }

    private fun renderShippingCourierWithoutEta(shipping: OrderShipment) {
        binding.shippingOccWidget.renderShippingCourierWithoutEta(
            shippingPrice = shipping.shippingPrice ?: 0,
            onChangeCourierListener = {
                onChangeCourier(shipping)
            }
        )
    }

    private fun renderShippingWithErrorMessage(shipping: OrderShipment, errorMessage: String) {
        binding.apply {
            shippingOccWidget.renderShippingWithErrorMessage(
                serviceName = shipping.serviceName,
                serviceEta = shipping.serviceEta,
                errorMessage = errorMessage,
                onShippingErrorMessageClickListener = {
                    onChangeDuration(shipping)
                }
            )
        }
    }


    private fun renderErrorShipping(shipping: OrderShipment) {
        binding.apply {
            shippingOccWidget.renderErrorShipping(
                serviceErrorMessage = shipping.serviceErrorMessage,
                onReloadShipping = {
                    if (profile.enable) {
                        listener.reloadShipping(shop.shopId)
                    }
                }
            )
        }
    }

    private fun renderShippingPinpointError() {
        binding.shippingOccWidget.renderShippingPinpointError(
            onChoosePinpoint = {
                if (profile.enable) {
                    listener.choosePinpoint(profile.address)
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun renderSingleShipping(shipping: OrderShipment, shipperName: String) {
        binding.shippingOccWidget.renderSingleShipping(
            shipperName = shipperName,
            shippingPrice = shipping.shippingPrice,
            shippingEta = shipping.shippingEta,
            logisticPromoViewModel = shipping.logisticPromoViewModel,
            isShowFreeShippingCourier = shipping.isApplyLogisticPromo && shipping.logisticPromoShipping != null && shipping.logisticPromoViewModel != null
        )

        if (shipping.logisticPromoViewModel?.description?.isNotBlank() == true) {
            if (!shipping.hasTriggerViewMessageTracking) {
                orderSummaryAnalytics.eventViewMessageInCourier2JamSampai(shipping.logisticPromoViewModel.description)
                shipping.hasTriggerViewMessageTracking = true
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
                setupPaymentInstallment(payment)
            } else {
                if (payment.errorData != null) {
                    // general & cc error
                    val message = payment.errorData.message
                    val button = payment.errorData.buttonText

                    val span = SpannableString("$message $button")
                    if (button.isNotBlank()) {
                        span.setSpan(
                            StyleSpan(BOLD),
                            message.length + 1,
                            span.length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding.root.context?.let {
                            span.setSpan(
                                ForegroundColorSpan(
                                    ContextCompat.getColor(
                                        it,
                                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                                    )
                                ),
                                message.length + 1,
                                span.length,
                                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        tvPaymentErrorMessage.setOnClickListener {
                            if (profile.enable) {
                                when (payment.errorData.action) {
                                    OrderPaymentErrorData.ACTION_CHANGE_PAYMENT -> listener.choosePayment(
                                        profile,
                                        payment
                                    )
                                    OrderPaymentErrorData.ACTION_CHANGE_CC -> listener.onChangeCreditCardClicked(
                                        payment.creditCard.additionalData
                                    )
                                }
                            }
                        }
                        btnChangePayment.invisible()
                    } else {
                        btnChangePayment.visible()
                    }
                    tvPaymentErrorMessage.text = span
                    tvPaymentErrorMessage.visible()
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
                                        listener.onOvoTopUpClicked(
                                            payment.walletErrorData.callbackUrl,
                                            payment.walletErrorData.isHideDigital,
                                            payment.ovoData.customerData
                                        )
                                    } else {
                                        listener.onWalletTopUpClicked(
                                            payment.walletData.walletType,
                                            payment.walletData.topUp.urlLink,
                                            payment.walletData.callbackUrl,
                                            payment.walletErrorData.isHideDigital,
                                            payment.walletData.topUp.headerTitle
                                        )
                                    }
                                } else if (payment.walletErrorData.type == OrderPaymentWalletErrorData.TYPE_ACTIVATION) {
                                    if (payment.isOvo) {
                                        listener.onOvoActivateClicked(payment.walletErrorData.callbackUrl)
                                    } else {
                                        listener.onWalletActivateClicked(
                                            payment.walletData.activation.headerTitle,
                                            payment.walletData.activation.urlLink,
                                            payment.walletData.callbackUrl
                                        )
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
                        span.setSpan(
                            StyleSpan(BOLD),
                            message.length + 1,
                            span.length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding.root.context?.let {
                            span.setSpan(
                                ForegroundColorSpan(
                                    ContextCompat.getColor(
                                        it,
                                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                                    )
                                ),
                                message.length + 1,
                                span.length,
                                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        tvPaymentErrorMessage.setOnClickListener {
                            if (profile.enable) {
                                if (payment.walletErrorData.type == OrderPaymentWalletErrorData.TYPE_TOP_UP) {
                                    if (payment.isOvo) {
                                        listener.onOvoTopUpClicked(
                                            payment.walletErrorData.callbackUrl,
                                            payment.walletErrorData.isHideDigital,
                                            payment.ovoData.customerData
                                        )
                                    } else {
                                        listener.onWalletTopUpClicked(
                                            payment.walletData.walletType,
                                            payment.walletData.topUp.urlLink,
                                            payment.walletData.callbackUrl,
                                            payment.walletErrorData.isHideDigital,
                                            payment.walletData.topUp.headerTitle
                                        )
                                    }
                                } else if (payment.walletErrorData.type == OrderPaymentWalletErrorData.TYPE_ACTIVATION) {
                                    if (payment.isOvo) {
                                        listener.onOvoActivateClicked(payment.walletErrorData.callbackUrl)
                                    } else {
                                        listener.onWalletActivateClicked(
                                            payment.walletData.activation.headerTitle,
                                            payment.walletData.activation.urlLink,
                                            payment.walletData.callbackUrl
                                        )
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
                tvInstallmentDetailWrap.gone()
                btnChangeInstallment.gone()
                btnChangeInstallmentWrap.gone()
                tvInstallmentErrorMessage.gone()
                tvInstallmentErrorAction.gone()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupPaymentInstallment(payment: OrderPayment) {
        val creditCard = payment.creditCard
        val selectedTerm = creditCard.selectedTerm
        binding.apply {
            if (!creditCard.isDebit && selectedTerm != null) {
                tvInstallmentType.visible()
                tvInstallmentDetail.visible()
                btnChangeInstallment.visible()
                tvInstallmentDetailWrap.gone()
                btnChangeInstallmentWrap.gone()
                if (selectedTerm.term > 0) {
                    tvInstallmentDetail.text = "${selectedTerm.term} Bulan x ${
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            selectedTerm.monthlyAmount,
                            false
                        ).removeDecimalSuffix()
                    }"
                } else {
                    tvInstallmentDetail.text =
                        binding.root.context.getString(R.string.lbl_installment_full_payment)
                }
                setupPaymentInstallmentError(selectedTerm)
                setMultiViewsOnClickListener(
                    tvInstallmentType,
                    tvInstallmentDetail,
                    btnChangeInstallment
                ) {
                    if (profile.enable) {
                        val selectedCreditCard = payment.creditCard
                        if (selectedCreditCard.availableTerms.isNotEmpty()) {
                            listener.onCreditCardInstallmentDetailClicked(selectedCreditCard)
                        }
                    }
                }
            } else if (!creditCard.isDebit && creditCard.isAfpb && creditCard.selectedTerm == null) {
                tvInstallmentType.visible()
                tvInstallmentDetail.visible()
                btnChangeInstallment.visible()
                tvInstallmentDetail.text =
                    binding.root.context.getString(R.string.lbl_installment_afpb_default)
                tvInstallmentDetail.alpha = ENABLE_ALPHA
                tvInstallmentDetailWrap.gone()
                btnChangeInstallmentWrap.gone()
                tvInstallmentErrorMessage.gone()
                tvInstallmentErrorAction.gone()
                setMultiViewsOnClickListener(
                    tvInstallmentType,
                    tvInstallmentDetail,
                    btnChangeInstallment
                ) {
                    if (profile.enable) {
                        val selectedCreditCard = payment.creditCard
                        listener.onCreditCardInstallmentDetailClicked(selectedCreditCard)
                    }
                }
            } else if (payment.walletData.isGoCicil) {
                val goCicilData = payment.walletData.goCicilData
                if (goCicilData.hasInvalidTerm) {
                    tvInstallmentDetailWrap.setText(R.string.occ_gocicil_default_installment)
                    tvInstallmentDetailWrap.visible()
                    tvInstallmentErrorMessage.text = goCicilData.errorMessageInvalidTenure
                    tvInstallmentErrorMessage.visible()
                } else if (goCicilData.selectedTerm == null || goCicilData.availableTerms.isEmpty()) {
                    tvInstallmentDetailWrap.setText(R.string.occ_gocicil_default_installment)
                    tvInstallmentDetailWrap.visible()
                    tvInstallmentErrorMessage.gone()
                } else {
                    tvInstallmentDetailWrap.text = tvInstallmentDetailWrap.context.getString(
                        R.string.occ_lbl_gocicil_installment_detail,
                        goCicilData.selectedTerm.installmentTerm,
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            goCicilData.selectedTerm.installmentAmountPerPeriod,
                            false
                        ).removeDecimalSuffix()
                    )
                    tvInstallmentDetailWrap.visible()
                    tvInstallmentErrorMessage.gone()
                }
                tvInstallmentType.visible()
                tvInstallmentDetail.gone()
                btnChangeInstallment.gone()
                btnChangeInstallmentWrap.visible()
                tvInstallmentErrorAction.gone()
                setMultiViewsOnClickListener(
                    tvInstallmentType,
                    tvInstallmentDetailWrap,
                    btnChangeInstallmentWrap
                ) {
                    if (profile.enable) {
                        listener.onGopayInstallmentDetailClicked()
                    }
                }
            } else {
                tvInstallmentType.gone()
                tvInstallmentDetail.gone()
                tvInstallmentDetailWrap.gone()
                btnChangeInstallment.gone()
                btnChangeInstallmentWrap.gone()
                tvInstallmentErrorMessage.gone()
                tvInstallmentErrorAction.gone()
            }
        }
    }

    private fun setupPaymentInstallmentError(selectedTerm: OrderPaymentInstallmentTerm) {
        binding.apply {
            if (selectedTerm.isError) {
                tvInstallmentDetail.alpha = DISABLE_ALPHA
                tvInstallmentErrorMessage.text =
                    binding.root.context.getString(R.string.lbl_installment_error)
                tvInstallmentErrorAction.text =
                    binding.root.context.getString(R.string.lbl_change_template)
                tvInstallmentErrorAction.setOnClickListener {
                    if (profile.enable) {
                        val creditCard = payment.creditCard
                        if (creditCard.availableTerms.isNotEmpty()) {
                            listener.onCreditCardInstallmentDetailClicked(creditCard)
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
                    setMultiViewsOnClickListener(
                        ivPayment,
                        tvPaymentName,
                        tvPaymentDetail,
                        btnChangePayment
                    ) {
                        if (profile.enable) {
                            listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
                        }
                    }
                }
                payment.creditCard.bankCode.isNotEmpty() -> {
                    showPaymentCC(payment)
                    btnChangePayment.invisible()
                    setMultiViewsOnClickListener(
                        ivPayment,
                        tvPaymentName,
                        tvPaymentDetail,
                        btnChangePayment
                    ) {
                        /* no-op */
                    }
                }
                else -> {
                    hidePaymentCC()
                    btnChangePayment.visible()
                    setMultiViewsOnClickListener(
                        ivPayment,
                        tvPaymentName,
                        tvPaymentDetail,
                        btnChangePayment
                    ) {
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
        span.setSpan(
            StyleSpan(BOLD),
            0,
            addressModel.addressName.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.apply {
            tvAddressName.text = span
            tvAddressDetail.text =
                "${addressModel.addressStreet}, ${addressModel.districtName}, ${addressModel.cityName}, ${addressModel.provinceName} ${addressModel.postalCode}"

            lblMainAddress.visibility = if (addressModel.isMainAddress) View.VISIBLE else View.GONE
            setAddressNameMargin(addressModel.isMainAddress)

            setMultiViewsOnClickListener(tvAddressName, tvAddressDetail, btnChangeAddress) {
                if (profile.enable) {
                    listener.chooseAddress(addressModel.addressId)
                }
            }
        }
    }

    private fun setAddressNameMargin(isMainAddress: Boolean) {
        val displayMetrics = binding.tvAddressName.context?.resources?.displayMetrics ?: return
        binding.tvAddressName.setMargin(
            if (isMainAddress) MAIN_ADDRESS_LEFT_MARGIN.dpToPx(
                displayMetrics
            ) else NOT_MAIN_ADDRESS_LEFT_MARGIN.dpToPx(displayMetrics),
            ADDRESS_TOP_MARGIN.dpToPx(displayMetrics),
            ADDRESS_RIGHT_MARGIN,
            ADDRESS_BOTTOM_MARGIN
        )
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

        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f

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

        fun chooseCourier(shipment: OrderShipment, list: ArrayList<ShippingCourierUiModel>)

        fun chooseDuration(isDurationError: Boolean, currentSpId: String)

        fun choosePinpoint(address: OrderProfileAddress)

        fun choosePayment(profile: OrderProfile, payment: OrderPayment)

        fun onCreditCardInstallmentDetailClicked(creditCard: OrderPaymentCreditCard)

        fun onGopayInstallmentDetailClicked()

        fun onChangeCreditCardClicked(additionalData: OrderPaymentCreditCardAdditionalData)

        fun onOvoActivateClicked(callbackUrl: String)

        fun onWalletActivateClicked(headerTitle: String, activationUrl: String, callbackUrl: String)

        fun onOvoTopUpClicked(
            callbackUrl: String,
            isHideDigital: Int,
            customerData: OrderPaymentOvoCustomerData
        )

        fun onWalletTopUpClicked(
            walletType: Int,
            url: String,
            callbackUrl: String,
            isHideDigital: Int,
            title: String
        )
    }
}
