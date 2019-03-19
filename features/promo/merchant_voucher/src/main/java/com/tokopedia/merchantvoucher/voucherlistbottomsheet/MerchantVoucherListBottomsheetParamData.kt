package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.os.Bundle

/**
 * Created by Irfan Khoirul on 15/03/19.
 * Store data needed to load MerchantVoucherListBottomSheetFragment.kt
 */

class MerchantVoucherListBottomsheetParamData {

    var productId: Int
    var qty: Int
    var shopId: Int
    var uniqueId: String
    var promoGlobalCode: String
    var skipApply: Int
    var isSuggested: Int
    var cartType: String
    var merchantVoucherList: ArrayList<String>

    companion object {
        val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
        val EXTRA_QTY = "EXTRA_QTY"
        val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        val EXTRA_UNIQUE_ID = "EXTRA_UNIQUE_ID"
        val EXTRA_PROMO_GLOBAL_CODE = "EXTRA_PROMO_GLOBAL_CODE"
        val EXTRA_SKIP_APPLY = "EXTRA_SKIP_APPLY"
        val EXTRA_IS_SUGESTED = "EXTRA_IS_SUGESTED"
        val EXTRA_CART_TYPE = "EXTRA_CART_TYPE"
        val EXTRA_MERCHANT_VOUCHER_LIST = "EXTRA_MERCHANT_VOUCHER_LIST"

        val EXTRA_SHOP_ID_DEFAULT_VALUE = "0"
        val EXTRA_CHECKOUT_TYPE_DEFAULT_VALUE = "default"
    }

    constructor(productId: Int, qty: Int, shopId: Int, uniqueId: String, promoGlobalCode: String, skipApply: Int, isSuggested: Int, cartType: String, merchantVoucherList: ArrayList<String>) {
        this.productId = productId
        this.qty = qty
        this.shopId = shopId
        this.uniqueId = uniqueId
        this.promoGlobalCode = promoGlobalCode
        this.skipApply = skipApply
        this.isSuggested = isSuggested
        this.cartType = cartType
        this.merchantVoucherList = merchantVoucherList
    }

    fun getBundle(): Bundle {
        val bundle = Bundle()
        bundle.putInt(EXTRA_PRODUCT_ID, productId)
        bundle.putInt(EXTRA_QTY, qty)
        bundle.putInt(EXTRA_SHOP_ID, shopId)
        bundle.putString(EXTRA_UNIQUE_ID, uniqueId)
        bundle.putString(EXTRA_PROMO_GLOBAL_CODE, promoGlobalCode)
        bundle.putInt(EXTRA_SKIP_APPLY, skipApply)
        bundle.putInt(EXTRA_IS_SUGESTED, isSuggested)
        bundle.putString(EXTRA_CART_TYPE, cartType)
        bundle.putStringArrayList(EXTRA_MERCHANT_VOUCHER_LIST, merchantVoucherList)

        return bundle
    }

    class BundleBuilder {
        private var productId: Int = 0
        private var qty: Int = 0
        private var shopId: Int = 0
        private var uniqueId: String = ""
        private var promoGlobalCode: String = ""
        private var skipApply: Int = 0
        private var isSuggested: Int = 0
        private var cartType: String = ""
        private var merchantVoucherList: ArrayList<String> = ArrayList()

        fun productId(productId: Int): BundleBuilder {
            this.productId = productId
            return this
        }

        fun qty(qty: Int): BundleBuilder {
            this.qty = qty
            return this
        }

        fun shopId(shopId: Int): BundleBuilder {
            this.shopId = shopId
            return this
        }

        fun uniqueId(uniqueId: String): BundleBuilder {
            this.uniqueId = uniqueId
            return this
        }

        fun promoGlobalCode(promoGlobalCode: String): BundleBuilder {
            this.promoGlobalCode = promoGlobalCode
            return this
        }

        fun skipApply(skipApply: Int): BundleBuilder {
            this.skipApply = skipApply
            return this
        }

        fun isSuggested(isSuggested: Int): BundleBuilder {
            this.isSuggested = isSuggested
            return this
        }

        fun cartType(cartType: String): BundleBuilder {
            this.cartType = cartType
            return this
        }

        fun merchantVoucherList(merchantVoucherList: ArrayList<String>): BundleBuilder {
            this.merchantVoucherList = merchantVoucherList
            return this
        }

        fun build(): MerchantVoucherListBottomsheetParamData {
            return MerchantVoucherListBottomsheetParamData(
                    productId, qty, shopId, uniqueId, promoGlobalCode, skipApply, isSuggested, cartType, merchantVoucherList
            )
        }
    }

}