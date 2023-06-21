package com.tokopedia.tokochat.view.chatroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.di.TokoChatActivityComponentFactory
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.util.TokoChatValueUtil
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.IS_FROM_BUBBLE_KEY
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.setBackIconUnify
import com.tokopedia.tokochat_common.view.activity.TokoChatBaseActivity

/**
 * Base Applink: [com.tokopedia.applink.ApplinkConst.TOKO_CHAT]
 *
 * This page accept 5 optional query parameters:
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.PARAM_SOURCE]
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.ORDER_ID_GOJEK]
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.ORDER_ID_TKPD]
 *
 * How to construct the applink with query parameters:
 * ```
 * val applinkUri = Uri.parse(ApplinkConstInternalCommunication.TOKO_CHAT).buildUpon().apply {
 *      appendQueryParameter(
 *          ApplinkConst.TokoChat.PARAM_SOURCE, "tokofood")
 *       appendQueryParameter(
 *          ApplinkConst.TokoChat.ORDER_ID_GOJEK, "RB-123-123")
 * }.toString()
 * ```
 *
 * note: Do not hardcode applink.
 * use variables provided in [com.tokopedia.applink.ApplinkConst]
 */
open class TokoChatActivity : TokoChatBaseActivity<TokoChatComponent>() {

    override fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = TokoChatFragmentFactory()
    }

    private fun initializeTokoChatComponent(): TokoChatComponent {
        return TokoChatActivityComponentFactory
            .instance
            .createTokoChatComponent(application).also {
                tokoChatComponent = it
            }
    }

    override fun getComponent(): TokoChatComponent {
        return tokoChatComponent ?: initializeTokoChatComponent()
    }

    override fun getNewFragment(): Fragment {
        return TokoChatFragment.getFragment(
            supportFragmentManager,
            classLoader,
            getFragmentBundle()
        )
    }

    override fun setupTokoChatHeader() {
        super.setupTokoChatHeader()
        getHeaderUnify()?.run {
            isShowBackButton = true
            headerCustomView?.let {
                customView(it)
            }
            setSupportActionBar(this)
            setBackIconUnify()
            contentInsetStartWithNavigation = Int.ZERO
            contentInsetEndWithActions = Int.ZERO
        }
    }

    protected open fun getFragmentBundle(): Bundle {
        val source = intent.data?.getQueryParameter(ApplinkConst.TokoChat.PARAM_SOURCE) ?: ""
        val gojekOrderId = intent.data?.getQueryParameter(ApplinkConst.TokoChat.ORDER_ID_GOJEK) ?: ""
        val tkpdOrderId = intent.data?.getQueryParameter(ApplinkConst.TokoChat.ORDER_ID_TKPD) ?: ""
        val isFromTokoFoodPostPurchase = intent?.getBooleanExtra(ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE, false) ?: false
        val pushNotifTemplateKey = intent?.getStringExtra(TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY) ?: ""
        val isFromBubble = intent?.getBooleanExtra(IS_FROM_BUBBLE_KEY, false) ?: false
        return Bundle().apply {
            putString(ApplinkConst.TokoChat.PARAM_SOURCE, source)
            putString(ApplinkConst.TokoChat.ORDER_ID_GOJEK, gojekOrderId)
            putString(ApplinkConst.TokoChat.ORDER_ID_TKPD, tkpdOrderId)
            putBoolean(ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE, isFromTokoFoodPostPurchase)
            putString(TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY, pushNotifTemplateKey)
            putBoolean(IS_FROM_BUBBLE_KEY, isFromBubble)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            ApplinkConst.TokoChat.PARAM_SOURCE,
            intent.data?.getQueryParameter(ApplinkConst.TokoChat.PARAM_SOURCE) ?: ""
        )
        outState.putString(
            ApplinkConst.TokoChat.ORDER_ID_GOJEK,
            intent.data?.getQueryParameter(ApplinkConst.TokoChat.ORDER_ID_GOJEK) ?: ""
        )
        outState.putString(
            ApplinkConst.TokoChat.ORDER_ID_TKPD,
            intent.data?.getQueryParameter(ApplinkConst.TokoChat.ORDER_ID_TKPD) ?: ""
        )
        outState.putBoolean(
            ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE,
            intent?.getBooleanExtra(
                ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE,
                false
            ) ?: false
        )
        outState.putString(
            TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY,
            intent?.getStringExtra(TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY) ?: ""
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        (fragment as? TokoChatFragment)?.onRestoreInstanceState(savedInstanceState)
    }
}
