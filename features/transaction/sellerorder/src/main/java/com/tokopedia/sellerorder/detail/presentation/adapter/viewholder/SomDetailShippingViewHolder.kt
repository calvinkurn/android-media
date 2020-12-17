package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailShipping
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.detail_shipping_item.view.*

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailShippingViewHolder(itemView: View, private val actionListener: SomDetailAdapter.ActionListener?) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailShipping) {
            with(itemView) {
                val layoutParamsReceiverName = tv_receiver_name.layoutParams as? ConstraintLayout.LayoutParams
                if (item.dataObject.isShippingPrinted) {
                    shippingPrintedLabel?.apply {
                        show()
                        layoutParamsReceiverName?.topMargin = 6.toPx()
                        unlockFeature = true
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    }
                } else {
                    layoutParamsReceiverName?.topMargin = 0.toPx()
                    shippingPrintedLabel.hide()
                }

                if (item.dataObject.logisticInfo.logisticInfoAllList.isNotEmpty()) {
                    tv_shipping_name.apply {
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                        setOnClickListener {
                            actionListener?.onShowInfoLogisticAll(item.dataObject.logisticInfo.logisticInfoAllList)
                        }
                        text = StringBuilder("${item.dataObject.shippingName} >")
                    }
                } else {
                    tv_shipping_name.text = item.dataObject.shippingName
                    tv_shipping_name.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
                }
                val numberPhone = if (item.dataObject.receiverPhone.startsWith(NUMBER_PHONE_SIX_TWO)) {
                    item.dataObject.receiverPhone.replaceFirst(NUMBER_PHONE_SIX_TWO, NUMBER_PHONE_ZERO, true)
                } else {
                    item.dataObject.receiverPhone
                }
                tv_receiver_name.text = item.dataObject.receiverName
                tv_receiver_number.text = numberPhone
                tv_receiver_street.text = item.dataObject.receiverStreet
                tv_receiver_district.text = item.dataObject.receiverDistrict
                tv_receiver_province.text = item.dataObject.receiverProvince

                shipping_address_copy.apply {
                    setOnClickListener {
                        actionListener?.onCopiedAddress(itemView.context.getString(R.string.alamat_pengiriman), (item.dataObject.receiverName +
                                "\n" + item.dataObject.receiverPhone +
                                "\n" + item.dataObject.receiverStreet +
                                "\n" + item.dataObject.receiverDistrict +
                                "\n" + item.dataObject.receiverProvince))
                    }
                }

                if (item.dataObject.awb.isNotEmpty()) {
                    rl_no_resi?.visibility = View.VISIBLE
                    no_resi_value?.show()
                    no_resi_value?.text = item.dataObject.awb
                    if (item.dataObject.awbTextColor.isNotEmpty()) {
                        itemView.no_resi_value?.setTextColor(Color.parseColor(item.dataObject.awbTextColor))
                    }
                    no_resi_copy?.setOnClickListener {
                        actionListener?.onTextCopied(itemView.context.getString(R.string.awb_label), item.dataObject.awb)
                    }
                } else {
                    rl_no_resi?.visibility = View.GONE
                    no_resi_value.hide()
                }

                // booking online - driver
                if (item.dataObject.driverName.isEmpty()) itemView.card_driver.visibility = View.GONE
                else {
                    if (item.dataObject.driverName.isNotEmpty()) {
                        tv_driver_name.text = item.dataObject.driverName
                    }

                    if (item.dataObject.driverPhoto.isNotEmpty()) {
                        iv_driver.loadImageCircle(item.dataObject.driverPhoto)
                    } else {
                        iv_driver.setImageResource(R.drawable.ic_driver_default)
                    }

                    if (item.dataObject.driverPhone.isNotEmpty()) {
                        tv_driver_phone.text = item.dataObject.driverPhone
                        driver_call_btn.setOnClickListener {
                            actionListener?.onDialPhone(item.dataObject.driverPhone)
                        }
                    } else {
                        tv_driver_phone.visibility = View.GONE
                        driver_call_btn.visibility = View.GONE
                    }

                    if (item.dataObject.driverLicense.isNotEmpty()) {
                        tv_driver_license.text = item.dataObject.driverLicense
                    } else {
                        tv_driver_license.visibility = View.GONE
                    }
                }

                // booking online - booking code
                if (item.dataObject.onlineBookingState == 0) {
                    booking_code_title?.hide()
                    booking_code_value?.hide()
                } else {
                    if (item.dataObject.onlineBookingCode.isEmpty() && item.dataObject.onlineBookingMsg.isEmpty()) {
                        booking_code_title?.hide()
                        booking_code_value?.hide()
                    } else {
                        booking_code_title?.show()
                        booking_code_value?.show()

                        if (item.dataObject.onlineBookingCode.isEmpty()) {
                            booking_code_title?.hide()
                            booking_code_value?.hide()
                        } else {
                            booking_code_value?.show()

                            booking_code_value.apply {
                                text = StringBuilder("${item.dataObject.onlineBookingCode} >")
                                setOnClickListener {
                                    actionListener?.onShowBookingCode(
                                            item.dataObject.onlineBookingCode,
                                            item.dataObject.onlineBookingType)
                                }
                            }
                        }
                    }
                }

                // dropshipper
                if (item.dataObject.dropshipperName.isNotEmpty() && item.dataObject.dropshipperPhone.isNotEmpty()) {
                    rl_drop_shipper.visibility = View.VISIBLE
                    tv_som_dropshipper_name?.show()
                    val numberPhoneDropShipper = if (item.dataObject.dropshipperPhone.startsWith(NUMBER_PHONE_SIX_TWO)) {
                        item.dataObject.dropshipperPhone.replaceFirst(NUMBER_PHONE_SIX_TWO, NUMBER_PHONE_ZERO, true)
                    } else {
                        item.dataObject.dropshipperPhone
                    }
                    tv_som_dropshipper_name.text = item.dataObject.dropshipperName
                    tv_dropshipper_number.text = numberPhoneDropShipper
                } else {
                    rl_drop_shipper.visibility = View.GONE
                    tv_som_dropshipper_name?.hide()
                }
            }
        }

    }

    companion object {
        const val NUMBER_PHONE_SIX_TWO = "62"
        const val NUMBER_PHONE_ZERO = "0"
    }
}