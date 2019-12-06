package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.graphics.Typeface
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailShipping
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import kotlinx.android.synthetic.main.detail_shipping_item.view.*

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailShippingViewHolder(itemView: View, private val actionListener: SomDetailAdapter.ActionListener) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailShipping) {
            itemView.tv_shipping_name.text = item.dataObject.shippingName
            itemView.tv_shipping_price.text = item.dataObject.shippingPrice
            itemView.tv_receiver_name.text = item.dataObject.receiverName
            itemView.tv_receiver_phone.text = item.dataObject.receiverPhone
            itemView.tv_receiver_street.text = item.dataObject.receiverStreet
            itemView.tv_receiver_district.text = item.dataObject.receiverDistrict
            if (item.dataObject.shippingNotes.isNotEmpty()) {
                itemView.tv_receiver_notes.visibility = View.VISIBLE
                itemView.tv_notes_label.visibility = View.VISIBLE
                itemView.tv_receiver_notes.text = item.dataObject.shippingNotes
            } else {
                itemView.tv_receiver_notes.visibility = View.GONE
                itemView.tv_notes_label.visibility = View.GONE
            }

            if (item.dataObject.isFreeShipping || item.dataObject.isRemoveAwb) {
                itemView.label_harus_sesuai.visibility = View.VISIBLE
                itemView.ic_harus_sesuai.visibility = View.VISIBLE
                itemView.label_harus_sesuai.setOnClickListener {
                    actionListener.onShowBottomSheetInfo(
                            itemView.context.getString(R.string.title_bottomsheet_immutable_courier),
                            R.string.desc_bottomsheet_immutable_courier)
                    }
                itemView.ic_harus_sesuai.setOnClickListener {
                    actionListener.onShowBottomSheetInfo(
                            itemView.context.getString(R.string.title_bottomsheet_immutable_courier),
                            R.string.desc_bottomsheet_immutable_courier) }
            } else {
                itemView.label_harus_sesuai.visibility = View.GONE
                itemView.ic_harus_sesuai.visibility = View.GONE
            }

            // booking online - driver
            if (item.dataObject.driverName.isEmpty()) itemView.card_driver.visibility = View.GONE
            else {
                if (item.dataObject.driverName.isNotEmpty()) {
                    itemView.tv_driver_name.text = item.dataObject.driverName
                }

                if (item.dataObject.driverPhoto.isNotEmpty()) {
                    itemView.iv_driver.loadImageCircle(item.dataObject.driverPhoto)
                } else {
                    itemView.iv_driver.setImageResource(R.drawable.ic_driver_default)
                }

                if (item.dataObject.driverPhone.isNotEmpty()) {
                    itemView.tv_driver_phone.text = item.dataObject.driverPhone
                    itemView.driver_call_btn.setOnClickListener {
                        actionListener.onDialPhone(item.dataObject.driverPhone)
                    }
                } else {
                    itemView.tv_driver_phone.visibility = View.GONE
                    itemView.driver_call_btn.visibility = View.GONE
                }

                if (item.dataObject.driverLicense.isNotEmpty()) {
                    itemView.tv_driver_license.text = item.dataObject.driverLicense
                } else {
                    itemView.tv_driver_license.visibility = View.GONE
                }
            }

            // booking online - booking code
            if (item.dataObject.onlineBookingState == 0) {
                itemView.rl_booking_code.visibility = View.GONE
            } else {
                if (item.dataObject.onlineBookingCode.isEmpty() && item.dataObject.onlineBookingMsg.isEmpty()) {
                    itemView.rl_booking_code.visibility = View.GONE
                } else {
                    itemView.rl_booking_code.visibility = View.VISIBLE

                    itemView.rl_wajib_dicantumkan.setOnClickListener {
                        actionListener.onShowBottomSheetInfo(
                                itemView.context.getString(R.string.wajib_tulis_kode_booking_title),
                                R.string.wajib_tulis_kode_booking_desc)
                    }

                    if (item.dataObject.onlineBookingCode.isEmpty()) {
                        itemView.booking_code_see_btn.visibility = View.GONE
                        itemView.booking_code_value.apply {
                            text = itemView.context.getString(R.string.placeholder_kode_booking)
                            setTypeface(this.typeface, Typeface.ITALIC)
                        }
                    } else {
                        itemView.booking_code_value.apply {
                            text = item.dataObject.onlineBookingCode
                            setTypeface(this.typeface, Typeface.BOLD)
                        }
                        itemView.booking_code_see_btn.apply {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                actionListener.onShowBookingCode(
                                        item.dataObject.onlineBookingCode,
                                        item.dataObject.onlineBookingType)
                            }
                        }

                    }
                }
            }
        }
    }
}