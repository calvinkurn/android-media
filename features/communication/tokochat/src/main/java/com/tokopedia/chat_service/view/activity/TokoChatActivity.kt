package com.tokopedia.chat_service.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.chat_service.view.activity.base.BaseTokoChatActivity
import com.tokopedia.chat_service.view.fragment.TokoChatFragment

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

open class TokoChatActivity : BaseTokoChatActivity() {

    override fun getNewFragment(): Fragment {
        return TokoChatFragment.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle()
        )
    }
}
