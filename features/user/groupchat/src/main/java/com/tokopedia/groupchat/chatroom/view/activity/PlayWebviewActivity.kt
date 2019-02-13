package com.tokopedia.groupchat.chatroom.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.groupchat.chatroom.view.fragment.PlayWebviewFragment

/**
 * @author by nisie on 12/02/19.
 */
class PlayWebviewActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return PlayWebviewFragment()
    }

    companion object {

        fun getCallingIntent(context: Context, hasTitlebar: Boolean, url : String): Intent {
            var intent = Intent(context, PlayWebviewActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(ApplinkConst.Play.PARAM_HAS_TITLEBAR, hasTitlebar)
            bundle.putString(ApplinkConst.Play.PARAM_URL, url)
            intent.putExtras(bundle)
            return intent
        }
    }

    object DeepLinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.PLAY_WEBVIEW)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, PlayWebviewActivity::class.java)
                    .setData(uri.build())
                    .putExtras(extras)
        }
    }

}