package com.tokopedia.buyerorderdetail.common.utils

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.buyerorderdetail.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context?) {
    private fun getString(resId: Int, vararg args: Any): String? {
        return try {
            context?.getString(resId, *args)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getProductListSectionHeader(): String {
        return getString(R.string.header_section_product_list).orEmpty()
    }

    fun getShipmentInfoSectionHeader(): String {
        return getString(R.string.header_section_shipment_info).orEmpty()
    }

    fun getPaymentInfoSectionHeader(): String {
        return getString(R.string.header_section_payment_into).orEmpty()
    }

    fun getErrorMessageNoProduct(): String {
        return getString(R.string.buyer_order_detail_error_message_no_product).orEmpty()
    }

    fun getCopyMessageReceiverAddress(): String {
        return getString(R.string.message_receiver_address_copied).orEmpty()
    }

    fun getCopyMessageAwb(): String {
        return getString(R.string.message_awb_copied).orEmpty()
    }

    fun getReceiverAddressLabel(): String {
        return getString(R.string.label_address).orEmpty()
    }

    fun getAwbLabel(): String {
        return getString(R.string.label_awb_number).orEmpty()
    }

    fun getDropshipLabel(): String {
        return getString(R.string.label_dropshipper).orEmpty()
    }

    fun getCopyLabelReceiverAddress(): String {
        return getString(R.string.copy_label_receiver_address).orEmpty()
    }

    fun getCopyLabelAwb(): String {
        return getString(R.string.copy_label_awb_number).orEmpty()
    }
}