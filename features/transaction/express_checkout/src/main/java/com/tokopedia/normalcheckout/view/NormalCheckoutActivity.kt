package com.tokopedia.normalcheckout.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.expresscheckout.R
import com.tokopedia.normalcheckout.constant.ATC_AND_BUY
import com.tokopedia.normalcheckout.constant.ProductAction
import com.tokopedia.tradein_common.IAccessRequestListener
import com.tokopedia.tradein.model.TradeInParams

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

open class NormalCheckoutActivity : BaseSimpleActivity(), IAccessRequestListener {
    companion object {
        const val EXTRA_SHOP_ID = "shop_id"
        const val EXTRA_CATEGORY_ID = "category_id"
        const val EXTRA_CATEGORY_NAME = "category_name"
        const val EXTRA_PRODUCT_ID = "product_id"
        const val EXTRA_PRODUCT_TITLE = "product_title"
        const val EXTRA_PRODUCT_PRICE = "product_price"
        const val EXTRA_PRODUCT_CONDITION = "product_condition"
        const val EXTRA_NOTES = "notes"
        const val EXTRA_QUANTITY = "quantity"
        const val EXTRA_SELECTED_VARIANT_ID = "selected_variant_id"
        const val EXTRA_PRODUCT_IMAGE = "product_image"
        const val EXTRA_ACTION = "action"
        const val EXTRA_SHOP_TYPE = "shop_type"
        const val EXTRA_SHOP_NAME = "shop_name"
        const val EXTRA_OCS = "ocs"
        const val EXTRA_TRADE_IN_PARAMS = "trade_in_params"
        private const val TRACKER_ATTRIBUTION = "tracker_attribution"
        private const val TRACKER_LIST_NAME = "tracker_list_name"
        private var tradeInParams: TradeInParams? = null
        private var normalCheckoutFragment : NormalCheckoutFragment? = null

        /**
         * shopID: mandatory
         * productID or (shopDomain and productName) is mandatory
         *
         */
        @JvmStatic
        fun getIntent(context: Context, shopId: String, categoryId: String, categoryName: String, productId: String,
                      productTitle: String?, productPrice: Float?, condition: String, notes: String? = "", quantity: Int? = 0,
                      selectedVariantId: String? = null,
                      @ProductAction action: Int = ATC_AND_BUY,
                      placeholderProductImage: String? = "",
                      trackerAttribution: String? = "",
                      trackerListName: String? = "",
                      shopType: String? = "",
                      shopName: String? = "",
                      isOneClickShipment: Boolean): Intent {
            return Intent(context, NormalCheckoutActivity::class.java).apply {
                putExtra(EXTRA_SHOP_ID, shopId)
                putExtra(EXTRA_CATEGORY_ID, categoryId)
                putExtra(EXTRA_CATEGORY_NAME, categoryName)
                putExtra(EXTRA_PRODUCT_ID, productId)
                putExtra(EXTRA_PRODUCT_TITLE, productTitle)
                putExtra(EXTRA_PRODUCT_PRICE, productPrice)
                putExtra(EXTRA_PRODUCT_CONDITION, condition)
                putExtra(EXTRA_NOTES, notes)
                putExtra(EXTRA_QUANTITY, quantity)
                putExtra(EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
                putExtra(EXTRA_ACTION, action)
                putExtra(EXTRA_PRODUCT_IMAGE, placeholderProductImage)
                putExtra(TRACKER_ATTRIBUTION, trackerAttribution)
                putExtra(TRACKER_LIST_NAME, trackerListName)
                putExtra(EXTRA_SHOP_TYPE, shopType)
                putExtra(EXTRA_SHOP_NAME, shopName)
                putExtra(EXTRA_OCS, isOneClickShipment)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        tradeInParams = intent.getParcelableExtra(EXTRA_TRADE_IN_PARAMS)
        bundle?.run {
            normalCheckoutFragment = NormalCheckoutFragment.createInstance(
                    getString(EXTRA_SHOP_ID),
                    getString(EXTRA_CATEGORY_ID),
                    getString(EXTRA_CATEGORY_NAME),
                    getString(EXTRA_PRODUCT_ID),
                    getString(EXTRA_PRODUCT_TITLE),
                    getFloat(EXTRA_PRODUCT_PRICE),
                    getString(EXTRA_PRODUCT_CONDITION),
                    getString(EXTRA_NOTES),
                    getInt(EXTRA_QUANTITY),
                    getString(EXTRA_SELECTED_VARIANT_ID),
                    getInt(EXTRA_ACTION),
                    getString(EXTRA_PRODUCT_IMAGE),
                    getString(TRACKER_ATTRIBUTION),
                    getString(TRACKER_LIST_NAME),
                    getString(EXTRA_SHOP_TYPE),
                    getString(EXTRA_SHOP_NAME),
                    getBoolean(EXTRA_OCS),
                    tradeInParams)
            return normalCheckoutFragment!!
        }
        return Fragment()
    }

    override fun onBackPressed() {
        fragment?.run {
            (this as NormalCheckoutFragment).selectVariantAndFinish()
        }
        overridePendingTransition(0, R.anim.push_down)
    }

    override fun clickAccept() {
        normalCheckoutFragment?.run {
            this.goToTradeInHome()
        }
    }

    override fun clickDeny() {

    }

}
