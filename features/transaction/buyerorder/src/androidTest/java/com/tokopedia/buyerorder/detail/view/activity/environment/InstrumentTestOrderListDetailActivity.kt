package com.tokopedia.buyerorder.detail.view.activity.environment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.buyerorder.detail.di.DaggerOrderDetailsComponent
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.buyerorder.detail.view.fragment.OmsDetailFragment as OldOms
import com.tokopedia.buyerorder.detail.revamp.fragment.OmsDetailFragment as NewOms

/**
 * created by @bayazidnasir on 7/9/2022
 */

class InstrumentTestOrderListDetailActivity: BaseSimpleActivity(), HasComponent<OrderDetailsComponent> {

    private var orderId = ""
    private var orderCategory = ""
    private var fromPayment = ""
    private var upstream = ""

    private val remoteConfig: RemoteConfig by lazy {
        FirebaseRemoteConfigImpl(this)
    }

    private val userSession: UserSessionInterface by lazy {
        UserSession(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let {
            fromPayment = it.getQueryParameter("from_payment") ?: ""
            intent.extras?.let { _ ->
                orderId = intent.getStringExtra("order_id") ?: ""
            }
        }

        if (userSession.isLoggedIn){
            intent.data?.let {
                upstream = it.getQueryParameter("upstream") ?: ""
            }
        }

    }

    override fun getNewFragment(): Fragment {
        return if (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_RECHARGE_BUYER_ORDER_DETAIL)) {
            NewOms.getInstance(orderId, orderCategory, fromPayment, upstream)
        } else {
            OldOms.getInstance(orderId, orderCategory, fromPayment, upstream)
        }
    }

    override fun getComponent(): OrderDetailsComponent {
        return DaggerOrderDetailsComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}