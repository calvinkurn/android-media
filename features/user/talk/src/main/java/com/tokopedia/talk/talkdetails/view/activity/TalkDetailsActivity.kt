package com.tokopedia.talk.talkdetails.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
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
        const val SHOP_ID = "SHOP_ID"

        @JvmStatic
        fun getCallingIntent(threadId: String, shopId: String, context: Context): Intent {
            val intent = Intent(context, TalkDetailsActivity::class.java)
            intent.putExtra(THREAD_TALK_ID, threadId)
            intent.putExtra(SHOP_ID, shopId)
            return intent
        }
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }
}