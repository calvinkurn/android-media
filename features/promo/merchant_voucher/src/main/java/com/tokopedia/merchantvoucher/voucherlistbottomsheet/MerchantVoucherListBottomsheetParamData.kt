package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.os.Bundle

/**
 * Created by Irfan Khoirul on 15/03/19.
 * Store data needed to load MerchantVoucherListBottomSheetFragment.kt
 */

class MerchantVoucherListBottomsheetParamData {

    var shopId: String
    var checkoutType: String

    companion object {
        val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        val EXTRA_CHECKOUT_TYPE = "EXTRA_CHECKOUT_TYPE"

        val EXTRA_SHOP_ID_DEFAULT_VALUE = "0"
        val EXTRA_CHECKOUT_TYPE_DEFAULT_VALUE = "default"
    }

    constructor(shopId: String, checkoutType: String) {
        this.shopId = shopId
        this.checkoutType = checkoutType
    }

    fun getBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(EXTRA_SHOP_ID, shopId)
        bundle.putString(EXTRA_CHECKOUT_TYPE, checkoutType)

        return bundle
    }


    class BundleBuilder {
        private var shopId: String = ""
        private var checkoutType: String = ""

        fun shopId(shopId: String): BundleBuilder {
            this.shopId = shopId
            return this
        }

        fun checkoutType(checkoutType: String): BundleBuilder {
            this.checkoutType = checkoutType
            return this
        }

        fun build(): MerchantVoucherListBottomsheetParamData {
            return MerchantVoucherListBottomsheetParamData(shopId, checkoutType)
        }
    }

}