package com.tokopedia.normalcheckout.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
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
        private var tradeInParams: TradeInParams? = null
        private var normalCheckoutFragment : NormalCheckoutFragment? = null

        /**
         * shopID: mandatory
         * productID or (shopDomain and productName) is mandatory
         *
         */
        @JvmStatic
        fun getIntent(context: Context, shopId: String, productId: String,
                      notes: String? = "", quantity: Int? = 0,
                      selectedVariantId: String? = null,
                      @ProductAction action: Int = ATC_AND_BUY,
                      placeholderProductImage: String? = "",
                      trackerAttribution: String? = "",
                      trackerListName: String? = "",
                      shopType: String? = "",
                      shopName: String? = "",
                      isOneClickShipment:Boolean): Intent {
            return Intent(context, NormalCheckoutActivity::class.java).apply {
                putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, shopId)
                putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, productId)
                putExtra(ApplinkConst.Transaction.EXTRA_NOTES, notes)
                putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
                putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
                putExtra(ApplinkConst.Transaction.EXTRA_ACTION, action)
                putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_IMAGE, placeholderProductImage)
                putExtra(ApplinkConst.Transaction.TRACKER_ATTRIBUTION, trackerAttribution)
                putExtra(ApplinkConst.Transaction.TRACKER_LIST_NAME, trackerListName)
                putExtra(ApplinkConst.Transaction.EXTRA_SHOP_TYPE, shopType)
                putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, shopName)
                putExtra(ApplinkConst.Transaction.EXTRA_OCS, isOneClickShipment)
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
        tradeInParams = intent.getParcelableExtra(ApplinkConst.Transaction.EXTRA_TRADE_IN_PARAMS)
        bundle?.run {
            normalCheckoutFragment = NormalCheckoutFragment.createInstance(
                    getString(ApplinkConst.Transaction.EXTRA_SHOP_ID),
                    getString(ApplinkConst.Transaction.EXTRA_PRODUCT_ID),
                    getString(ApplinkConst.Transaction.EXTRA_NOTES),
                    getInt(ApplinkConst.Transaction.EXTRA_QUANTITY),
                    getString(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID),
                    getInt(ApplinkConst.Transaction.EXTRA_ACTION),
                    getString(ApplinkConst.Transaction.EXTRA_PRODUCT_IMAGE),
                    getString(ApplinkConst.Transaction.TRACKER_ATTRIBUTION),
                    getString(ApplinkConst.Transaction.TRACKER_LIST_NAME),
                    getString(ApplinkConst.Transaction.EXTRA_SHOP_TYPE),
                    getString(ApplinkConst.Transaction.EXTRA_SHOP_NAME),
                    getBoolean(ApplinkConst.Transaction.EXTRA_OCS),
                    getBoolean(ApplinkConst.Transaction.EXTRA_NEED_REFRESH),
                    tradeInParams
            )
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
