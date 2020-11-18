package com.tokopedia.oneclickcheckout.order.view.card

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.features.shippingdurationocc.ShippingDurationOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingdurationocc.ShippingDurationOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticdata.data.constant.CourierConstant
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.InstallmentDetailBottomSheet
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.currency.CurrencyFormatUtil

class NewOrderPreferenceCard(private val view: View, private val listener: OrderPreferenceCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private lateinit var preference: OrderPreference
    private var shipment: OrderShipment? = null
    private var payment: OrderPayment? = null

    private val tvCardHeader by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_card_header) }
    private val lblMainPreference by lazy { view.findViewById<Label>(R.id.lbl_new_main_preference) }
    private val tvChoosePreference by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_choose_preference) }

    private val tvAddressName by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_address_name) }
    private val tvAddressReceiver by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_address_receiver) }
    private val tvAddressDetail by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_address_detail) }
    private val tickerShippingPromo by lazy { view.findViewById<CardUnify>(R.id.ticker_new_shipping_promo) }
    private val tickerShippingPromoDescription by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.ticker_new_shipping_promo_description) }
    private val tickerAction by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.ticker_new_action) }

    private val ivPayment by lazy { view.findViewById<ImageUnify>(R.id.iv_new_payment) }
    private val tvPaymentName by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_payment_name) }
    private val tvPaymentDetail by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_payment_detail) }
    private val tvPaymentErrorMessage by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_payment_error_message) }
    private val tvInstallmentType by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_installment_type) }
    private val tvInstallmentDetail by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_installment_detail) }
    private val tvInstallmentErrorMessage by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_installment_error_message) }
    private val tvInstallmentErrorAction by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_installment_error_action) }

    private val tvNewShippingDuration by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_shipping_duration) }
    private val btnNewChangeDuration by lazy { view.findViewById<IconUnify>(R.id.btn_new_change_duration) }
    private val tvNewShippingCourier by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_shipping_courier) }
    private val btnNewChangeCourier by lazy { view.findViewById<IconUnify>(R.id.btn_new_change_courier) }
    private val tvNewShippingErrorMessage by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.tv_new_shipping_error_message) }
    private val btnNewReloadShipping by lazy { view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.btn_new_reload_shipping) }
    private val iconNewReloadShipping by lazy { view.findViewById<IconUnify>(R.id.icon_new_reload_shipping) }

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
        showHeader()

        showAddress()

        showShipping()

        showPayment()

        tvChoosePreference?.setOnClickListener {
            listener.onChangePreferenceClicked()
        }
    }

    private fun showHeader() {
        if (preference.profileRecommendation.isNullOrEmpty()) {
            val profileIndex = preference.profileIndex
            tvCardHeader?.text = profileIndex
            if (preference.preference.status == 2) {
                lblMainPreference?.visible()
            } else {
                lblMainPreference?.gone()
            }
            tvChoosePreference?.text = view.context.getString(R.string.label_choose_other_preference)
        } else {
            tvCardHeader?.text = preference.profileRecommendation
            lblMainPreference?.gone()
            tvChoosePreference?.text = view.context.getString(R.string.label_create_other_preference)
        }
    }

    private fun showShipping() {
        val shipmentModel = preference.preference.shipment

        val shipping = shipment

        tickerShippingPromo?.gone()

        if (shipping == null || shipping.serviceName.isNullOrEmpty()) {
            // loading?
        } else {
            if (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank()) {
                tvNewShippingDuration?.text = "Pengiriman ${shipping.serviceName}"
                tvNewShippingDuration?.visible()
                btnNewChangeDuration?.setOnClickListener {
                    listener.chooseDuration(false)
                }
                btnNewChangeDuration?.visible()
                tvNewShippingCourier?.text = "${shipping.shipperName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice ?: 0, false).removeDecimalSuffix()})"
                tvNewShippingCourier?.visible()
                btnNewChangeCourier?.setOnClickListener {
                    listener.chooseCourier()
                }
                btnNewChangeCourier?.visible()
                tvNewShippingErrorMessage?.gone()
                btnNewReloadShipping?.gone()
                iconNewReloadShipping?.gone()

                renderBboTicker(shipping)

                if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
                    tvNewShippingCourier?.text = view.context.getString(R.string.lbl_osp_free_shipping)
                    tvNewShippingDuration?.gone()
                    btnNewChangeDuration?.gone()
                    if (shipping.logisticPromoViewModel.benefitAmount >= shipping.logisticPromoViewModel.shippingRate) {
//                        tvBboPrice?.text = view.context.getString(R.string.lbl_osp_free_shipping_only_price)
//                        tvShippingSlashPrice?.gone()
                    } else {

//                        tvBboPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.discountedRate, false).removeDecimalSuffix()
//                        tvShippingSlashPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.shippingRate, false).removeDecimalSuffix()
//                        tvShippingSlashPrice?.paintFlags?.let {
//                            tvShippingSlashPrice?.paintFlags = it or Paint.STRIKE_THRU_TEXT_FLAG
//                        }
//                        tvShippingSlashPrice?.visible()
                    }
                }
            } else {
                tvNewShippingDuration?.text = "Pengiriman"
                tvNewShippingDuration?.visible()
                btnNewChangeDuration?.gone()
                tvNewShippingCourier?.gone()
                btnNewChangeCourier?.gone()
                tvNewShippingErrorMessage?.text = shipping.serviceErrorMessage
                tvNewShippingErrorMessage?.visible()
                btnNewReloadShipping?.setOnClickListener {

                }
                btnNewReloadShipping?.visible()
                iconNewReloadShipping?.visible()
            }
        }
    }

//    private fun renderBbo(shipping: OrderShipment, tvBboPrice: Typography?) {
//        if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
//            tvShippingName?.text = view.context.getString(R.string.lbl_osp_free_shipping)
//            tvShippingDuration?.text = generateServiceDuration(shipping.logisticPromoViewModel.title)
//            if (shipping.logisticPromoViewModel.benefitAmount >= shipping.logisticPromoViewModel.shippingRate) {
//                tvBboPrice?.text = view.context.getString(R.string.lbl_osp_free_shipping_only_price)
//                tvShippingSlashPrice?.gone()
//            } else {
//                tvBboPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.discountedRate, false).removeDecimalSuffix()
//                tvShippingSlashPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.shippingRate, false).removeDecimalSuffix()
//                tvShippingSlashPrice?.paintFlags?.let {
//                    tvShippingSlashPrice?.paintFlags = it or Paint.STRIKE_THRU_TEXT_FLAG
//                }
//                tvShippingSlashPrice?.visible()
//            }
//        }
//    }

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

    private fun generateServiceDuration(tempServiceDuration: String?): String {
        return if (tempServiceDuration == null || !tempServiceDuration.contains("(") || !tempServiceDuration.contains(")")) {
            view.context.getString(R.string.lbl_no_exact_shipping_duration)
        } else {
            view.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
        }
    }

    private fun showPayment() {
        val paymentModel = preference.preference.payment

        ivPayment?.setImageUrl(paymentModel.image)
        tvPaymentName?.text = paymentModel.gatewayName
        val description = paymentModel.description
        if (description.isNotBlank()) {
            tvPaymentDetail?.text = description.replace('*', '\u2022')
            tvPaymentDetail?.visible()
        } else {
            tvPaymentDetail?.gone()
        }
//        if (paymentModel.tickerMessage.isNotBlank()) {
//            tvPaymentInfo?.text = MethodChecker.fromHtml(paymentModel.tickerMessage)
//            tvPaymentInfo?.visible()
//        } else {
//            tvPaymentInfo?.gone()
//        }

        val payment = payment
        if (payment != null) {
            if (!payment.isError()) {
                tvPaymentErrorMessage?.gone()
//                tvPaymentErrorAction?.gone()
//                tvPaymentOvoErrorAction?.gone()
                setPaymentActiveAlpha()

                setupPaymentSelector(payment)

                setupPaymentInstallment(payment.creditCard.selectedTerm)
                (ivPayment?.layoutParams as? ConstraintLayout.LayoutParams)?.bottomToBottom = R.id.tv_payment_detail
            } else {
                if (payment.errorMessage.message.isNotEmpty()) {
                    tvPaymentErrorMessage?.text = payment.errorMessage.message
                    tvPaymentErrorMessage?.visible()
                    val actionText = payment.errorMessage.button.text
//                    if (actionText.isNotEmpty()) {
//                        tvPaymentErrorAction?.text = actionText
//                        tvPaymentErrorAction?.setOnClickListener {
//                            if (payment.hasCreditCardOption()) {
//                                listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
//                            } else {
//                                listener.onPreferenceEditClicked(preference)
//                            }
//                        }
//                        tvPaymentErrorAction?.visible()
//                    } else {
//                        tvPaymentErrorAction?.gone()
//                    }
//                    tvPaymentOvoErrorAction?.gone()
                    tvPaymentDetail?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    tvPaymentDetail?.setOnClickListener {
                        //do nothing
                    }
                    setPaymentErrorAlpha(payment.isCalculationError)
                    (ivPayment?.layoutParams as ConstraintLayout.LayoutParams).bottomToBottom = R.id.tv_payment_detail
                } else if (payment.ovoErrorData != null) {
                    if (payment.ovoErrorData.message.isNotBlank()) {
                        tvPaymentErrorMessage?.text = payment.ovoErrorData.message
                        tvPaymentErrorMessage?.visible()
                        (ivPayment?.layoutParams as? ConstraintLayout.LayoutParams)?.bottomToBottom = R.id.tv_payment_error_message
                    } else {
                        tvPaymentErrorMessage?.gone()
                        (ivPayment?.layoutParams as? ConstraintLayout.LayoutParams)?.bottomToBottom = R.id.tv_payment_ovo_error_action
                    }
//                    if (payment.ovoErrorData.buttonTitle.isNotBlank()) {
//                        tvPaymentOvoErrorAction?.text = payment.ovoErrorData.buttonTitle
//                        tvPaymentOvoErrorAction?.setOnClickListener {
//                            if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_TOP_UP) {
//                                listener.onOvoTopUpClicked(payment.ovoErrorData.callbackUrl, payment.ovoErrorData.isHideDigital, payment.ovoData.customerData)
//                            } else if (payment.ovoErrorData.type == OrderPaymentOvoErrorData.TYPE_ACTIVATION) {
//                                listener.onOvoActivateClicked(payment.ovoErrorData.callbackUrl)
//                            }
//                        }
//                        tvPaymentOvoErrorAction?.visible()
//                    } else {
//                        tvPaymentOvoErrorAction?.gone()
//                    }
                    tvPaymentDetail?.gone()
//                    tvPaymentErrorAction?.gone()
                    if (payment.ovoErrorData.isBlockingError) {
                        setPaymentErrorAlpha(true)
                    } else {
                        setPaymentActiveAlpha()
                    }
                } else {
                    tvPaymentErrorMessage?.gone()
//                    tvPaymentErrorAction?.gone()
//                    tvPaymentOvoErrorAction?.gone()
                    setPaymentErrorAlpha(payment.isCalculationError)
                    (ivPayment?.layoutParams as? ConstraintLayout.LayoutParams)?.bottomToBottom = R.id.tv_payment_detail
                }
                tvInstallmentType?.gone()
                tvInstallmentDetail?.gone()
                tvInstallmentErrorMessage?.gone()
                tvInstallmentErrorAction?.gone()
            }
        }
    }

    private fun setupPaymentInstallment(selectedTerm: OrderPaymentInstallmentTerm?) {
        if (selectedTerm != null) {
            tvInstallmentType?.visible()
            tvInstallmentDetail?.visible()
            if (selectedTerm.term > 0) {
                tvInstallmentDetail?.text = "${selectedTerm.term} Bulan x ${CurrencyFormatUtil.convertPriceValueToIdrFormat(selectedTerm.monthlyAmount, false).removeDecimalSuffix()}"
            } else {
                tvInstallmentDetail?.text = view.context.getString(R.string.lbl_installment_full_payment)
            }
            setupPaymentInstallmentError(selectedTerm)
            tvInstallmentDetail?.setOnClickListener {
                listener.onInstallmentDetailClicked()
            }
        } else {
            tvInstallmentType?.gone()
            tvInstallmentDetail?.gone()
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
            tvPaymentDetail?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_grey_20dp, 0)
            tvPaymentDetail?.setOnClickListener {
                listener.onChangeCreditCardClicked(payment.creditCard.additionalData)
            }
        } else {
            tvPaymentDetail?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tvPaymentDetail?.setOnClickListener {
                //do nothing
            }
        }
    }

    private fun setPaymentErrorAlpha(isDetailRed: Boolean) {
        ivPayment?.alpha = 0.5f
        tvPaymentName?.alpha = 0.5f
        tvPaymentDetail?.alpha = 0.5f
        if (isDetailRed) {
//            tvPaymentDetail?.setTextColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Red_R600))
        } else {
//            tvPaymentDetail?.setTextColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
        }
    }

    private fun setPaymentActiveAlpha() {
        ivPayment?.alpha = 1f
        tvPaymentName?.alpha = 1f
        tvPaymentDetail?.alpha = 1f
//        tvPaymentDetail?.setTextColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
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
            if (shippingRecommendationData.logisticPromo != null) {
                list.add(shippingRecommendationData.logisticPromo)
                if (shippingRecommendationData.logisticPromo.disabled && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[0]) && shippingRecommendationData.logisticPromo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[1])) {
                    orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
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
                list.add(shippingRecommendationData.logisticPromo)
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

        fun onCourierChange(shippingCourierViewModel: ShippingCourierUiModel)

        fun onDurationChange(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean)

        fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel)

        fun chooseCourier()

        fun chooseDuration(isDurationError: Boolean)

        fun onPreferenceEditClicked(preference: OrderPreference)

        fun onInstallmentDetailClicked()

        fun onInstallmentDetailChange(selectedInstallmentTerm: OrderPaymentInstallmentTerm)

        fun onChangeCreditCardClicked(additionalData: OrderPaymentCreditCardAdditionalData)

        fun onOvoActivateClicked(callbackUrl: String)

        fun onOvoTopUpClicked(callbackUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData)
    }
}