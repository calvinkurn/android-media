package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.utils.CurrencyFormatUtil
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
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderPreference
import kotlinx.android.synthetic.main.card_order_preference.view.*

class OrderPreferenceCard(private val view: View, private val listener: OrderPreferenceCardListener) {

    private lateinit var preference: OrderPreference

    fun setPreference(preference: OrderPreference) {
        this.preference = preference
        showPreference()
    }

    @SuppressLint("SetTextI18n")
    private fun showPreference() {
        showHeader()

        showAddress()

        showShipping()

        showPayment()

        view.tv_choose_preference.setOnClickListener {
            listener.onChangePreferenceClicked()
        }
    }

    private fun showHeader() {
        val profileIndex = preference.profileIndex
        view.tv_card_header.text = profileIndex

        view.iv_edit_preference.setOnClickListener {
            listener.onPreferenceEditClicked(preference)
        }
    }

    private fun showShipping() {
        val shipmentModel = preference.preference.shipment

        val shipping = preference.shipping
        view.tv_shipping_name.text = shipping?.serviceName ?: shipmentModel.serviceName
        view.tv_shipping_duration.text = shipping?.serviceDuration ?: shipmentModel.serviceDuration

        if (shipping == null) {
            view.tv_shipping_duration.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            view.ticker_shipping_promo.gone()
            view.tv_shipping_price.gone()
        } else {
            if (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank()) {
                if (!shipping.isServicePickerEnable) {
                    view.tv_shipping_duration.text = "${shipping.serviceDuration} - ${shipping.shipperName}"
                    view.tv_shipping_duration.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                    view.tv_shipping_duration.setOnClickListener { }
                    view.tv_shipping_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                            ?: 0, false)
                    view.tv_shipping_price.setOnClickListener {
                        listener.chooseCourier()
                    }
                    view.tv_shipping_price.visible()
                    view.tv_shipping_slash_price.gone()
                    view.tv_shipping_courier.gone()
                    view.tv_shipping_courier_lbl.gone()
                    view.tv_shipping_message.gone()
                    view.tv_shipping_change_duration.gone()

                    //BBO
                    if (shipping.logisticPromoTickerMessage?.isNotEmpty() == true && shipping.shippingRecommendationData?.logisticPromo != null) {
                        view.ticker_shipping_promo_description.text = "${shipping.logisticPromoTickerMessage}"
                        view.ticker_shipping_promo.visible()
                        view.ticker_action.setOnClickListener {
                            listener.onLogisticPromoClick(shipping.shippingRecommendationData.logisticPromo)
                        }
                    } else {
                        view.ticker_shipping_promo.gone()
                    }

                    //BBO APPLY
                    if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
                        view.tv_shipping_name.text = "Pengiriman Bebas Ongkir"
                        val tempServiceDuration = shipping.logisticPromoViewModel.title
                        val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                            tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")"))
                        } else {
                            "Durasi tergantung kurir"
                        }
                        view.tv_shipping_duration.text = serviceDur
                        if (shipping.logisticPromoViewModel.benefitAmount >= shipping.logisticPromoShipping.productData.price.price.toDouble()) {
                            view.tv_shipping_price.text = "Rp 0"
                            view.tv_shipping_slash_price.gone()
                        } else {
                            view.tv_shipping_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoShipping.productData.price.price - shipping.logisticPromoViewModel.benefitAmount, false)
                            view.tv_shipping_slash_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoShipping.productData.price.price, false)
                            view.tv_shipping_slash_price.paintFlags = view.tv_shipping_slash_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            view.tv_shipping_slash_price.visible()
                        }
                    }

                } else {
                    view.tv_shipping_duration.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_grey_20dp, 0)
                    view.tv_shipping_duration.setOnClickListener {
                        listener.chooseDuration()
                    }
                    view.tv_shipping_price.gone()
                    view.tv_shipping_slash_price.gone()
                    view.tv_shipping_message.gone()
                    view.tv_shipping_change_duration.gone()
                    view.tv_shipping_courier_lbl.visible()
                    view.tv_shipping_courier.text = "${shipping.shipperName} - ${CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                            ?: 0, false)}"
                    view.tv_shipping_courier.setOnClickListener {
                        listener.chooseCourier()
                    }
                    view.tv_shipping_courier.visible()

                    //BBO
                    if (shipping.logisticPromoTickerMessage?.isNotEmpty() == true && shipping.shippingRecommendationData?.logisticPromo != null) {
                        view.ticker_shipping_promo_description.text = "${shipping.logisticPromoTickerMessage}"
                        view.ticker_shipping_promo.visible()
                        view.ticker_action.setOnClickListener {
                            listener.onLogisticPromoClick(shipping.shippingRecommendationData.logisticPromo)
                        }
                    } else {
                        view.ticker_shipping_promo.gone()
                    }

                    //BBO APPLY
                    if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null) {
                        view.tv_shipping_name.text = "Pengiriman Bebas Ongkir"
                        val tempServiceDuration = shipping.logisticPromoViewModel.title
                        val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                            tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")"))
                        } else {
                            "Durasi tergantung kurir"
                        }
                        view.tv_shipping_duration.text = serviceDur
                        if (shipping.logisticPromoViewModel.benefitAmount >= shipping.logisticPromoViewModel.shippingRate) {
                            view.tv_shipping_courier.text = "Bebas Ongkir - Rp 0"
                            view.tv_shipping_slash_price.gone()
                        } else {
                            view.tv_shipping_courier.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.discountedRate, false)
                            view.tv_shipping_slash_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.logisticPromoViewModel.shippingRate, false)
                            view.tv_shipping_slash_price.paintFlags = view.tv_shipping_slash_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            view.tv_shipping_slash_price.visible()
                        }
                    }
                }
            } else {
                if (!shipping.isServicePickerEnable) {
                    view.tv_shipping_courier_lbl.gone()
                    view.tv_shipping_courier.gone()
                    view.tv_shipping_duration.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                    view.tv_shipping_price.gone()
                    view.tv_shipping_slash_price.gone()
                    view.tv_shipping_message.text = shipping.serviceErrorMessage
                    if (shipping.shippingRecommendationData != null) {
                        view.tv_shipping_change_duration.setOnClickListener {
                            listener.chooseDuration()
                        }
                        view.tv_shipping_change_duration.visible()
                    } else {
                        view.tv_shipping_change_duration.gone()
                    }
                    view.tv_shipping_message.visible()
                }
            }
        }
    }

    private fun showPayment() {
        val paymentModel = preference.preference.payment
        ImageHandler.loadImageFitCenter(view.context, view.iv_payment, paymentModel.image)
        view.tv_payment_name.text = paymentModel.gatewayName
        val description = paymentModel.description
        if (description.isNotBlank()) {
            view.tv_payment_detail.text = description
            view.tv_payment_detail.visible()
        } else {
            view.tv_payment_detail.gone()
        }
    }

    private fun showAddress() {
        val addressModel = preference.preference.address
        view.tv_address_name.text = addressModel.addressName
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
            view.tv_address_receiver.text = receiverText
            view.tv_address_receiver.visible()
        } else {
            view.tv_address_receiver.gone()
        }
        view.tv_address_detail.text = addressModel.addressStreet + ", " +
                addressModel.districtName + ", " +
                addressModel.cityName + ", " +
                addressModel.provinceName + " " +
                addressModel.postalCode
    }

    fun initView() {
//        view.tv_shipping_duration.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, com.tokopedia.design.R.drawable.ic_arrow_drop_down_grey_checkout_module, 0)
    }

    fun showCourierBottomSheet(fragment: OrderSummaryPageFragment) {
//        if (!listener.isLoading()) {
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
//        }
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
            ShippingDurationOccBottomSheet().showBottomSheet(fragment, shippingRecommendationData.shippingDurationViewModels, object : ShippingDurationOccBottomSheetListener {
                override fun onDurationChosen(serviceData: ServiceData, selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
                    listener.onDurationChange(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
                }
            })
        }
    }

    fun setPaymentError(paymentErrorMessage: String?) {
        if (paymentErrorMessage?.isNotEmpty() == true) {
            view.tv_payment_message.text = paymentErrorMessage
            view.tv_payment_message.visible()
            view.tv_payment_message.setOnClickListener {
                listener.onErrorPaymentClicked()
            }
        } else {
            view.tv_payment_message.gone()
        }
    }

    interface OrderPreferenceCardListener {

        fun onChangePreferenceClicked()

        fun onCourierChange(shippingCourierViewModel: ShippingCourierUiModel)

        fun onDurationChange(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean)

        fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel)

//        fun isLoading(): Boolean

        fun chooseCourier()

        fun chooseDuration()

        fun onErrorPaymentClicked()

        fun onPreferenceEditClicked(preference: OrderPreference)
    }
}