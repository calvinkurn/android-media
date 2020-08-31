package com.tokopedia.talk_old.talkdetails.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk_old.common.di.DaggerTalkComponent
import com.tokopedia.talk_old.common.di.TalkComponent
import com.tokopedia.talk_old.talkdetails.view.fragment.TalkDetailsFragment

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

    override fun onCreate(savedInstanceState: Bundle?) {
        addExtrasIfFromAppLink()
        super.onCreate(savedInstanceState)
    }

    private fun addExtrasIfFromAppLink() {
        val uri = intent.data ?: return
        val threadId = uri.lastPathSegment ?: ""
        val shopId = uri.getQueryParameter(APPLINK_SHOP_ID) ?: ""
        val commentId = uri.getQueryParameter(APPLINK_COMMENT_ID) ?: ""
        val source = uri.getQueryParameter(SOURCE) ?: ""
        if (threadId.isNotEmpty()) {
            intent.putExtra(THREAD_TALK_ID, threadId)
        }
        if (shopId.isNotEmpty()) {
            intent.putExtra(SHOP_ID, shopId)
        }
        if (commentId.isNotEmpty()) {
            intent.putExtra(COMMENT_ID, commentId)
        }
        if (source.isNotEmpty()) {
            intent.putExtra(SOURCE, source)
        }
    }

    companion object {
        const val APPLINK_SHOP_ID = "shop_id"
        const val APPLINK_COMMENT_ID = "comment_id"

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

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }
}