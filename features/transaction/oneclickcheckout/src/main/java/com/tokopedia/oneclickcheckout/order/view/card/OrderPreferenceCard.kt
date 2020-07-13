package com.tokopedia.oneclickcheckout.order.view.card

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
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
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.model.OrderPreference
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderPreferenceCard(private val view: View, private val listener: OrderPreferenceCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private lateinit var preference: OrderPreference

    private val tvCardHeader by lazy { view.findViewById<Typography>(R.id.tv_card_header) }
    private val lblMainPreference by lazy { view.findViewById<Label>(R.id.lbl_main_preference) }
    private val ivEditPreference by lazy { view.findViewById<ImageView>(R.id.iv_edit_preference) }
    private val tvChoosePreference by lazy { view.findViewById<Typography>(R.id.tv_choose_preference) }
    private val tvShippingName by lazy { view.findViewById<Typography>(R.id.tv_shipping_name) }
    private val tvShippingDuration by lazy { view.findViewById<Typography>(R.id.tv_shipping_duration) }
    private val tickerShippingPromo by lazy { view.findViewById<CardUnify>(R.id.ticker_shipping_promo) }
    private val tvShippingPrice by lazy { view.findViewById<Typography>(R.id.tv_shipping_price) }
    private val tvShippingSlashPrice by lazy { view.findViewById<Typography>(R.id.tv_shipping_slash_price) }
    private val tvShippingCourier by lazy { view.findViewById<Typography>(R.id.tv_shipping_courier) }
    private val tvShippingCourierLbl by lazy { view.findViewById<Typography>(R.id.tv_shipping_courier_lbl) }
    private val tvShippingMessage by lazy { view.findViewById<Typography>(R.id.tv_shipping_message) }
    private val tvShippingChangeDuration by lazy { view.findViewById<Typography>(R.id.tv_shipping_change_duration) }
    private val tickerShippingPromoDescription by lazy { view.findViewById<Typography>(R.id.ticker_shipping_promo_description) }
    private val tickerAction by lazy { view.findViewById<Typography>(R.id.ticker_action) }
    private val ivPayment by lazy { view.findViewById<ImageView>(R.id.iv_payment) }
    private val tvPaymentName by lazy { view.findViewById<Typography>(R.id.tv_payment_name) }
    private val tvPaymentDetail by lazy { view.findViewById<Typography>(R.id.tv_payment_detail) }
    private val tvAddressName by lazy { view.findViewById<Typography>(R.id.tv_address_name) }
    private val tvAddressReceiver by lazy { view.findViewById<Typography>(R.id.tv_address_receiver) }
    private val tvAddressDetail by lazy { view.findViewById<Typography>(R.id.tv_address_detail) }

    fun setPreference(preference: OrderPreference) {
        this.preference = preference
        showPreference()
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

        ivEditPreference?.setOnClickListener {
            listener.onPreferenceEditClicked(preference)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showShipping() {
        val shipmentModel = preference.preference.shipment

        val shipping = preference.shipping
        tvShippingName?.text = view.context.getString(R.string.lbl_shipping_with_name, shipmentModel.serviceName.capitalize())

        if (shipping == null) {
            val tempServiceDuration = shipmentModel.serviceDuration
            val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                view.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
            } else {
                OrderSummaryPageViewModel.NO_EXACT_DURATION_MESSAGE
            }
            tvShippingDuration?.text = serviceDur
            tvShippingDuration?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tickerShippingPromo?.gone()
            tvShippingPrice?.gone()
        } else {
            if (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank()) {
                if (!shipping.isServicePickerEnable) {
                    tvShippingDuration?.text = "${shipping.serviceDuration} - ${shipping.shipperName}"
                    tvShippingDuration?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    tvShippingDuration?.setOnClickListener { }
                    tvShippingPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                            ?: 0, false).removeDecimalSuffix()
                    tvShippingPrice?.setOnClickListener {
                        listener.chooseCourier()
                    }
                    tvShippingPrice?.visible()
                    tvShippingSlashPrice?.gone()
                    tvShippingCourier?.gone()
                    tvShippingCourierLbl?.gone()
                    tvShippingMessage?.gone()
                    tvShippingChangeDuration?.gone()

                    //BBO
                    if (shipping.logisticPromoTickerMessage?.isNotEmpty() == true && shipping.shippingRecommendationData?.logisticPromo != null) {
                        tickerShippingPromoDescription?.text = "${shipping.logisticPromoTickerMessage}"
                        tickerShippingPromo?.visible()
                        tickerAction?.setOnClickListener {
                            listener.onLogisticPromoClick(shipping.shippingRecommendationData.logisticPromo)
                        }
                    } else {
                        tickerShippingPromo?.gone()
                    }

                    //BBO APPLY
                    if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
                        tvShippingName?.text = view.context.getString(R.string.lbl_osp_free_shipping)
                        val tempServiceDuration = shipping.logisticPromoViewModel.title
                        val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                            view.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
                        } else {
                            OrderSummaryPageViewModel.NO_EXACT_DURATION_MESSAGE
                        }
                        tvShippingDuration?.text = serviceDur
                        if (shipping.logisticPromoViewModel.benefitAmount >= shipping.logisticPromoViewModel.shippingRate) {
                            tvShippingPrice?.text = view.context.getString(R.string.lbl_osp_free_shipping_only_price)
                            tvShippingSlashPrice?.gone()
                        } else {
                            tvShippingPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.discountedRate, false).removeDecimalSuffix()
                            tvShippingSlashPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.shippingRate, false).removeDecimalSuffix()
                            tvShippingSlashPrice?.paintFlags?.let {
                                tvShippingSlashPrice?.paintFlags = it or Paint.STRIKE_THRU_TEXT_FLAG
                            }
                            tvShippingSlashPrice?.visible()
                        }
                    }

                } else {
                    tvShippingName?.text = view.context.getString(R.string.lbl_shipping)
                    tvShippingDuration?.text = shipping.serviceName
                    tvShippingDuration?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_grey_20dp, 0)
                    tvShippingDuration?.setOnClickListener {
                        listener.chooseDuration(false)
                    }
                    tvShippingPrice?.gone()
                    tvShippingSlashPrice?.gone()
                    tvShippingMessage?.gone()
                    tvShippingChangeDuration?.gone()
                    tvShippingCourierLbl?.visible()
                    tvShippingCourier?.text = "${shipping.shipperName} - ${CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                            ?: 0, false).removeDecimalSuffix()}"
                    tvShippingCourier?.setOnClickListener {
                        listener.chooseCourier()
                    }
                    tvShippingCourier?.visible()

                    //BBO
                    if (shipping.logisticPromoTickerMessage?.isNotEmpty() == true && shipping.shippingRecommendationData?.logisticPromo != null) {
                        tickerShippingPromoDescription?.text = "${shipping.logisticPromoTickerMessage}"
                        tickerShippingPromo?.visible()
                        tickerAction?.setOnClickListener {
                            listener.onLogisticPromoClick(shipping.shippingRecommendationData.logisticPromo)
                        }
                    } else {
                        tickerShippingPromo?.gone()
                    }

                    //BBO APPLY
                    if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
                        tvShippingName?.text = view.context.getString(R.string.lbl_osp_free_shipping)
                        val tempServiceDuration = shipping.logisticPromoViewModel.title
                        val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                            view.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
                        } else {
                            OrderSummaryPageViewModel.NO_EXACT_DURATION_MESSAGE
                        }
                        tvShippingDuration?.text = serviceDur
                        if (shipping.logisticPromoViewModel.benefitAmount >= shipping.logisticPromoViewModel.shippingRate) {
                            tvShippingCourier?.text = view.context.getString(R.string.lbl_osp_free_shipping_with_price)
                            tvShippingSlashPrice?.gone()
                        } else {
                            tvShippingCourier?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.discountedRate, false).removeDecimalSuffix()
                            tvShippingSlashPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.shippingRate, false).removeDecimalSuffix()
                            tvShippingSlashPrice?.paintFlags?.let {
                                tvShippingSlashPrice?.paintFlags = it or Paint.STRIKE_THRU_TEXT_FLAG
                            }
                            tvShippingSlashPrice?.visible()
                        }
                    }
                }
            } else {
                if (!shipping.isServicePickerEnable) {
                    tvShippingDuration?.text = shipping.serviceDuration
                    tvShippingCourierLbl?.gone()
                    tvShippingCourier?.gone()
                    tvShippingDuration?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    tvShippingPrice?.gone()
                    tvShippingSlashPrice?.gone()
                    tvShippingMessage?.text = shipping.serviceErrorMessage
                    if (shipping.shippingRecommendationData != null) {
                        tvShippingChangeDuration?.setOnClickListener {
                            listener.chooseDuration(true)
                        }
                        tvShippingChangeDuration?.visible()
                    } else {
                        tvShippingChangeDuration?.gone()
                    }
                    tvShippingMessage?.visible()
                }
            }
        }
    }

    private fun showPayment() {
        val paymentModel = preference.preference.payment
        ivPayment?.let {
            ImageHandler.loadImageFitCenter(view.context, it, paymentModel.image)
        }
        tvPaymentName?.text = paymentModel.gatewayName
        val description = paymentModel.description
        if (description.isNotBlank()) {
            tvPaymentDetail?.text = description
            tvPaymentDetail?.visible()
        } else {
            tvPaymentDetail?.gone()
        }
    }

    fun setPaymentError(isError: Boolean) {
        if (isError) {
            ivPayment?.alpha = 0.5f
            tvPaymentName?.alpha = 0.5f
            tvPaymentDetail?.alpha = 0.5f
            val color = tvPaymentDetail?.context?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Red_R600)
            if (color != null) {
                tvPaymentDetail?.setTextColor(color)
            }
        } else {
            ivPayment?.alpha = 1f
            tvPaymentName?.alpha = 1f
            tvPaymentDetail?.alpha = 1f
            val color = tvPaymentDetail?.context?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_68)
            if (color != null) {
                tvPaymentDetail?.setTextColor(color)
            }
        }
    }

    @SuppressLint("SetTextI18n")
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
        val shippingRecommendationData = preference.shipping?.shippingRecommendationData
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
        val shippingRecommendationData = preference.shipping?.shippingRecommendationData
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
    }
}