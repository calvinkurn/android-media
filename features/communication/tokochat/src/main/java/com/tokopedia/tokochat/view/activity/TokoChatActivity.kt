package com.tokopedia.tokochat.view.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokochat.di.DaggerTokoChatComponent
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.di.TokoChatContextModule
import com.tokopedia.tokochat.view.fragment.TokoChatFragment
import com.tokopedia.tokochat.view.fragment.experiment.TokoChatFragmentExp
import com.tokopedia.tokochat.view.fragment.factory.TokoChatFragmentFactory
import com.tokopedia.tokochat_common.view.activity.BaseTokoChatActivity

/**
 * Base Applink: [com.tokopedia.applink.ApplinkConst.TOKO_CHAT]
 *
 * This page accept 5 optional query parameters:
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.PARAM_SOURCE]
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.ORDER_ID_GOJEK]
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.ORDER_ID_TKPD]
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.SERVICE_TYPE]
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.ORDER_CHAT_TYPE]
 *
 * For Order Chat Type, there are only 3 values
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.ORDER_CHAT_TYPE_DRIVER]
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.ORDER_CHAT_TYPE_SHOP]
 * - [com.tokopedia.applink.ApplinkConst.TokoChat.ORDER_CHAT_TYPE_MERCHANT]
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
class TokoChatActivity : BaseTokoChatActivity<TokoChatComponent>() {

    override fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = TokoChatFragmentFactory()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    private fun initializeTokoChatComponent(): TokoChatComponent {
        return DaggerTokoChatComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .tokoChatContextModule(TokoChatContextModule(this))
            .build().also {
                tokoChatComponent = it
            }
    }

    override fun getComponent(): TokoChatComponent {
        return tokoChatComponent?: initializeTokoChatComponent()
    }

    override fun getNewFragment(): Fragment {
        val isExp = true
        return if (isExp) {
            TokoChatFragmentExp.getFragment(
                supportFragmentManager,
                classLoader,
                bundle ?: Bundle()
            )
        } else {
            TokoChatFragment.getFragment(
                supportFragmentManager,
                classLoader,
                bundle ?: Bundle()
            )
        }
    }
}
