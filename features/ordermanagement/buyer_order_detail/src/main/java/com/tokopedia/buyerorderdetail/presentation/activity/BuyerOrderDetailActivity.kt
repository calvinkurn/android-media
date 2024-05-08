package com.tokopedia.buyerorderdetail.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.byteio.IAdsLog
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.applink.UriUtil
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.performance.BuyerOrderDetailLoadMonitoring
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailCommonIntentParamKey
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailIntentParamKey
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailModule
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import com.tokopedia.tokochat.config.util.TokoChatConnection

open class BuyerOrderDetailActivity : BaseSimpleActivity(), HasComponent<BuyerOrderDetailComponent>, IAdsLog {

    var buyerOrderDetailLoadMonitoring: BuyerOrderDetailLoadMonitoring? = null

    override fun getNewFragment(): Fragment? {
        val extras = intent.extras ?: createIntentExtrasFromAppLink()
        return if (extras == null) {
            finish()
            null
        } else {
            BuyerOrderDetailFragment.newInstance(extras)
        }
    }

    /*
        Posible applink:
        * tokopedia-android-internal://marketplace//buyer-order-detail?payment_id={paymentID}&cart_string={cartString}
        * tokopedia-android-internal://marketplace//buyer-order-detail?order_id={orderID}
     */
    protected fun createIntentExtrasFromAppLink(): Bundle? {
        return intent.data?.let {
            Bundle().apply {
                val params = UriUtil.uriQueryParamsToMap(it)
                val orderId = params[BuyerOrderDetailCommonIntentParamKey.ORDER_ID].orEmpty()
                val paymentId = params[BuyerOrderDetailIntentParamKey.PARAM_PAYMENT_ID].orEmpty()
                val cartString = params[BuyerOrderDetailIntentParamKey.PARAM_CART_STRING].orEmpty()
                putString(BuyerOrderDetailCommonIntentParamKey.ORDER_ID, orderId)
                putString(BuyerOrderDetailIntentParamKey.PARAM_PAYMENT_ID, paymentId)
                putString(BuyerOrderDetailIntentParamKey.PARAM_CART_STRING, cartString)
            }
        }
    }

    override fun getComponent(): BuyerOrderDetailComponent {
        return DaggerBuyerOrderDetailComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .tokoChatConfigComponent(TokoChatConnection.getComponent(this))
                .buyerOrderDetailModule(BuyerOrderDetailModule())
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initLoadMonitoring()
        super.onCreate(savedInstanceState)
        overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_right_in_medium, com.tokopedia.resources.common.R.anim.slide_left_out_medium)
        setupWindowColor()
    }

    private fun setupWindowColor() {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_left_in_medium, com.tokopedia.resources.common.R.anim.slide_right_out_medium)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_left_in_medium, com.tokopedia.resources.common.R.anim.slide_right_out_medium)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_left_in_medium, com.tokopedia.resources.common.R.anim.slide_right_out_medium)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initLoadMonitoring() {
        buyerOrderDetailLoadMonitoring = BuyerOrderDetailLoadMonitoring()
        buyerOrderDetailLoadMonitoring?.initPerformanceMonitoring()
    }

    override fun getPageName(): String {
        return PageName.BUYER_ORDER_MANAGEMENT
    }
}
