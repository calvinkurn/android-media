package com.tokopedia.seller.action

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.action.common.analytics.SellerActionAnalytics
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.const.SellerActionFeatureName
import com.tokopedia.seller.action.common.di.DaggerSellerActionComponent
import javax.inject.Inject

class SellerActionActivity: Activity() {

    @Inject
    lateinit var analytics: SellerActionAnalytics

    companion object {
        fun createOrderDetailIntent(context: Context, orderId: String): Intent {
            return RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_ACTION).apply {
                putExtra(SellerActionConst.Params.FEATURE_NAME, SellerActionFeatureName.ORDER_DETAIL)
                putExtra(SellerActionConst.Params.ORDER_ID, orderId)
            }
        }

        fun createActionIntent(context: Context, featureName: String): Intent {
            return RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_ACTION).apply {
                putExtra(SellerActionConst.Params.FEATURE_NAME, featureName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependency()
        super.onCreate(savedInstanceState)
        intent?.getStringExtra(SellerActionConst.Params.FEATURE_NAME)?.let { feature ->
            redirectToSellerapp(feature)
        }
    }

    private fun injectDependency() {
        DaggerSellerActionComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun redirectToSellerapp(featureName: String) {
        when(featureName) {
            SellerActionFeatureName.ALL_ORDER -> {
                analytics.clickOrderAppButton()
                RouteManager.route(this, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL)
            }
            SellerActionFeatureName.ORDER_DETAIL -> {
                intent?.getStringExtra(SellerActionConst.Params.ORDER_ID)?.let { orderId ->
                    analytics.clickOrderLine()
                    RouteManager.route(this, ApplinkConstInternalOrder.ORDER_DETAIL, orderId)
                }
            }
            else -> {
                RouteManager.route(this, ApplinkConstInternalSellerapp.SELLER_HOME)
            }
        }
        finish()
    }
}