package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderPreference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import kotlinx.android.synthetic.main.card_order_preference.view.*

class OrderPreferenceCard(private val view: View, private val fragment: OrderSummaryPageFragment, private val listener: OrderPreferenceCardListener) {

    private lateinit var preference: OrderPreference

    fun setPreference(preference: OrderPreference) {
        this.preference = preference
        showPreference()
    }

    @SuppressLint("SetTextI18n")
    private fun showPreference() {
        showAddress()

        showShipping()

        showPayment()

        view.tv_choose_preference.setOnClickListener {
            listener.onChangePreferenceClicked()
        }
    }

    private fun showShipping() {
        val shipmentModel = preference.preference.shipment
        view.tv_shipping_name.text = shipmentModel.serviceName
        view.tv_shipping_duration.text = shipmentModel.serviceDuration

        val shipping = preference.shipping

        if (shipping == null) {
            view.tv_shipping_duration.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            view.ticker_shipping_promo.gone()
            view.tv_shipping_price.gone()
        } else {
            if (shipping.serviceErrorMessage == null || shipping.serviceErrorMessage.isBlank()) {
                if (!shipping.isServicePickerEnable) {
                    view.tv_shipping_duration.text = "${shipmentModel.serviceDuration} - ${shipping.shipperName}"
                    view.tv_shipping_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.shippingPrice
                            ?: 0, false)
                    view.tv_shipping_price.setOnClickListener {
                        chooseCourier()
                    }
                    view.tv_shipping_price.visible()
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
            receiverText = "- $receiverName"
            if (phone.isNotBlank()) {
                receiverText = "$receiverText($phone)"
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

    private fun chooseCourier() {
        if (!fragment.isLoading()) {
            val shippingRecommendationData = preference.shipping?.shippingRecommendationData
            if (shippingRecommendationData != null) {
                val list: ArrayList<RatesViewModelType> = ArrayList()
                for (shippingDurationViewModel in shippingRecommendationData.shippingDurationViewModels) {
                    if (shippingDurationViewModel.isSelected) {
                        list.addAll(shippingDurationViewModel.shippingCourierViewModelList)
                        break
                    }
                }
                ShippingCourierOccBottomSheet().showBottomSheet(fragment, list, object : ShippingCourierOccBottomSheetListener {
                    override fun onCourierChosen(shippingCourierViewModel: ShippingCourierUiModel) {
                        fragment.onChooseCourier(shippingCourierViewModel)
                    }

                    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {

                    }
                })
            }
        }
    }

    interface OrderPreferenceCardListener {

        fun onChangePreferenceClicked()

//        fun onCourierChange()
    }
}