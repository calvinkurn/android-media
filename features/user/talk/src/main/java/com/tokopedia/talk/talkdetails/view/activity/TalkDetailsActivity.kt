package com.tokopedia.talk.talkdetails.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.talk.common.TalkRouter
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.talkdetails.view.fragment.TalkDetailsFragment

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        val fragment = TalkDetailsFragment()
        fragment.arguments = bundle
        return fragment
    }

    companion object {
        const val THREAD_TALK_ID = "THREAD_TALK_ID"
        const val COMMENT_ID = "COMMENT_ID"
        const val SHOP_ID = "SHOP_ID"
        const val SOURCE = "source"

        const val SOURCE_INBOX = "inbox"
        const val SOURCE_SHOP = "shop"
        const val SOURCE_PDP = "pdp"
        const val SOURCE_DEEPLINK = "deeplink"

        const val RESULT_OK_REFRESH_TALK = 200
        const val RESULT_OK_READ = 201
        const val RESULT_OK_DELETE_COMMENT = 202
        const val RESULT_OK_DELETE_TALK = 203


        @JvmStatic
        fun getCallingIntent(threadId: String, shopId: String, context: Context, source: String): Intent {
            val intent = Intent(context, TalkDetailsActivity::class.java)
            intent.putExtra(THREAD_TALK_ID, threadId)
            intent.putExtra(SHOP_ID, shopId)
            intent.putExtra(SOURCE, source)
            return intent
        }
    }

    object DeepLinkIntents {
        const val TALK_ID = "talk_id"
        const val SHOP_ID = "shop_id"

        @JvmStatic
        @DeepLink(ApplinkConst.TALK_DETAIL)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val talkId = extras.getString(TALK_ID, "")
            val shopId = extras.getString(SHOP_ID, "")
            val source = SOURCE_DEEPLINK


            return (context.applicationContext as TalkRouter).getTalkDetailIntent(context,
                    talkId, shopId, source).putExtras(extras)
        }

    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }
}