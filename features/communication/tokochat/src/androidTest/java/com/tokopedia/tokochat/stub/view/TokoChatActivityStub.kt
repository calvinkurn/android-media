package com.tokopedia.tokochat.stub.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.util.TokoChatValueUtil
import com.tokopedia.tokochat.view.chatroom.TokoChatActivity

class TokoChatActivityStub : TokoChatActivity() {

    override fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = TokoChatFragmentFactoryStub()
    }

    override fun getComponent(): TokoChatComponent {
        return BaseTokoChatTest.tokoChatComponent!!
    }

    override fun getNewFragment(): Fragment {
        return TokoChatFragmentStub.getFragment(
            supportFragmentManager,
            classLoader,
            getFragmentBundle()
        )
    }

    // Override because Instrument tests don't use query parameter or applink
    override fun getFragmentBundle(): Bundle {
        val source = intent?.getStringExtra(ApplinkConst.TokoChat.PARAM_SOURCE) ?: ""
        val gojekOrderId = intent?.getStringExtra(ApplinkConst.TokoChat.ORDER_ID_GOJEK) ?: ""
        val tkpdOrderId = intent?.getStringExtra(ApplinkConst.TokoChat.ORDER_ID_TKPD) ?: ""
        val isFromTokoFoodPostPurchase = intent?.getBooleanExtra(ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE, false) ?: false
        val pushNotifTemplateKey = intent?.getStringExtra(TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY) ?: ""
        return Bundle().apply {
            putString(ApplinkConst.TokoChat.PARAM_SOURCE, source)
            putString(ApplinkConst.TokoChat.ORDER_ID_GOJEK, gojekOrderId)
            putString(ApplinkConst.TokoChat.ORDER_ID_TKPD, tkpdOrderId)
            putBoolean(ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE, isFromTokoFoodPostPurchase)
            putString(TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY, pushNotifTemplateKey)
        }
    }
}
