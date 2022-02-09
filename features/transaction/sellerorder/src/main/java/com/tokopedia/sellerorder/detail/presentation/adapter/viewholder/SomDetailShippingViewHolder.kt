package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailShippingItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailShipping
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailShippingViewHolder(
    itemView: View?,
    private val actionListener: SomDetailAdapterFactoryImpl.ActionListener?
) : AbstractViewHolder<SomDetailData>(itemView) {

    private val binding by viewBinding<DetailShippingItemBinding>()

    override fun bind(item: SomDetailData) {
        if (item.dataObject is SomDetailShipping) {
            binding?.run {
                val layoutParamsReceiverName = tvReceiverName.layoutParams as? ConstraintLayout.LayoutParams
                if (item.dataObject.isShippingPrinted) {
                    shippingPrintedLabel.apply {
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
                    tvShippingName.apply {
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                        setOnClickListener {
                            actionListener?.onShowInfoLogisticAll(item.dataObject.logisticInfo.logisticInfoAllList)
                        }
                        text = StringBuilder("${item.dataObject.shippingName} >")
                    }
                } else {
                    tvShippingName.text = item.dataObject.shippingName
                    tvShippingName.setTextColor(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
                }
                val numberPhone = if (item.dataObject.receiverPhone.startsWith(NUMBER_PHONE_SIX_TWO)) {
                    item.dataObject.receiverPhone.replaceFirst(NUMBER_PHONE_SIX_TWO, NUMBER_PHONE_ZERO, true)
                } else {
                    item.dataObject.receiverPhone
                }

                with(item.dataObject) {
                    tvReceiverName.text = receiverName
                    if (numberPhone.isNotBlank()) {
                        tvReceiverNumber.show()
                        tvReceiverNumber.text = numberPhone
                    } else {
                        tvReceiverNumber.hide()
                    }

                    if (receiverStreet.isNotBlank()) {
                        tvReceiverStreet.show()
                        tvReceiverStreet.text = receiverStreet
                    } else {
                        tvReceiverStreet.hide()
                    }

                    if (receiverDistrict.isNotBlank() && !receiverDistrict.startsWith(CONTAINS_COMMA)) {
                        tvReceiverDistrict.show()
                        tvReceiverDistrict.text = receiverDistrict
                    } else {
                        tvReceiverDistrict.hide()
                    }

                    if (receiverProvince.isNotBlank()) {
                        tvReceiverProvince.show()
                        tvReceiverProvince.text = receiverProvince
                    } else {
                        tvReceiverProvince.hide()
                    }

                    shippingAddressCopy.apply {
                        setOnClickListener {

                            val numberPhoneText = if (numberPhone.isNotBlank()) {
                                "\n" + numberPhone
                            } else ""

                            val receiverStreetText = if (receiverStreet.isNotBlank()) {
                                "\n" + receiverStreet
                            } else {
                                ""
                            }

                            val receiverDistrictText = if (receiverDistrict.isNotBlank() && !receiverDistrict.startsWith(CONTAINS_COMMA)) {
                                "\n" + receiverDistrict
                            } else {
                                ""
                            }

                            val receiverProvinceText = if (receiverProvince.isNotBlank()) {
                                "\n" + receiverProvince
                            } else {
                                ""
                            }

                            actionListener?.onCopiedAddress(root.context.getString(R.string.alamat_pengiriman), (receiverName +
                                    numberPhoneText +
                                    receiverStreetText +
                                    receiverDistrictText +
                                    receiverProvinceText))
                        }
                    }
                }

                if (item.dataObject.awb.isNotEmpty()) {
                    rlNoResi.visibility = View.VISIBLE
                    noResiValue.show()
                    noResiValue.text = item.dataObject.awb
                    if (item.dataObject.awbTextColor.isNotEmpty()) {
                        noResiValue.setTextColor(Color.parseColor(item.dataObject.awbTextColor))
                    }
                    noResiCopy.setOnClickListener {
                        actionListener?.onTextCopied(root.context.getString(R.string.awb_label), item.dataObject.awb, root.context.getString(R.string.readable_awb_label))
                    }
                } else {
                    rlNoResi.visibility = View.GONE
                    noResiValue.hide()
                }

                // booking online - driver
                if (item.dataObject.driverName.isEmpty()) cardDriver.visibility = View.GONE
                else {
                    if (item.dataObject.driverName.isNotEmpty()) {
                        tvDriverName.text = item.dataObject.driverName
                    }

                    if (item.dataObject.driverPhoto.isNotEmpty()) {
                        ivDriver.loadImageCircle(item.dataObject.driverPhoto)
                    } else {
                        ivDriver.setImageResource(R.drawable.ic_driver_default)
                    }

                    if (item.dataObject.driverPhone.isNotEmpty()) {
                        tvDriverPhone.text = item.dataObject.driverPhone
                        driverCallBtn.setOnClickListener {
                            actionListener?.onDialPhone(item.dataObject.driverPhone)
                        }
                    } else {
                        tvDriverPhone.visibility = View.GONE
                        driverCallBtn.visibility = View.GONE
                    }

                    if (item.dataObject.driverLicense.isNotEmpty()) {
                        tvDriverLicense.text = item.dataObject.driverLicense
                    } else {
                        tvDriverLicense.visibility = View.GONE
                    }
                }

                // booking online - booking code
                if (item.dataObject.onlineBookingState == 0) {
                    bookingCodeTitle.hide()
                    bookingCodeCopy.hide()
                    bookingCodeValue.hide()
                } else {
                    if (item.dataObject.onlineBookingCode.isEmpty() && item.dataObject.onlineBookingMsg.isEmpty()) {
                        bookingCodeTitle.hide()
                        bookingCodeCopy.hide()
                        bookingCodeValue.hide()
                    } else {
                        bookingCodeTitle.show()
                        bookingCodeCopy.show()
                        bookingCodeValue.show()

                        if (item.dataObject.onlineBookingCode.isEmpty()) {
                            bookingCodeTitle.hide()
                            bookingCodeCopy.hide()
                            bookingCodeValue.hide()
                        } else {
                            bookingCodeCopy.setOnClickListener {
                                actionListener?.onTextCopied(root.context.getString(R.string.booking_code_label), item.dataObject.onlineBookingCode, root.context.getString(R.string.readable_booking_code_label))
                            }
                            bookingCodeValue.apply {
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
                val numberPhoneDropShipper = if (item.dataObject.dropshipperPhone.startsWith(NUMBER_PHONE_SIX_TWO)) {
                    item.dataObject.dropshipperPhone.replaceFirst(NUMBER_PHONE_SIX_TWO, NUMBER_PHONE_ZERO, true)
                } else {
                    item.dataObject.dropshipperPhone
                }
                if (numberPhoneDropShipper.isNotBlank()) {
                    tvDropshipperNumber.text = numberPhoneDropShipper
                    tvDropshipperNumber.show()
                } else {
                    tvDropshipperNumber.gone()
                }
                if (item.dataObject.dropshipperName.isNotBlank()) {
                    tvSomDropshipperName.text = item.dataObject.dropshipperName
                    tvSomDropshipperName.show()
                } else {
                    tvSomDropshipperName.gone()
                }
                tvSomDropshipperLabel.showWithCondition(
                    numberPhoneDropShipper.isNotBlank() || item.dataObject.dropshipperName.isNotBlank()
                )
            }
        }
    }

    companion object {
        const val NUMBER_PHONE_SIX_TWO = "62"
        const val NUMBER_PHONE_ZERO = "0"
        const val CONTAINS_COMMA = ","

        val LAYOUT = R.layout.detail_shipping_item
    }
}