package com.tokopedia.buyerorderdetail.common.utils

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.buyerorderdetail.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context?) {
    private fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
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
}