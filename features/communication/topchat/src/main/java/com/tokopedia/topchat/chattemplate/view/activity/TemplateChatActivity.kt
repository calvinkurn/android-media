package com.tokopedia.topchat.chattemplate.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment.Companion.createInstance

open class TemplateChatActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.title = getString(R.string.title_template_chat)
        toolbar.setBackgroundColor(
            MethodChecker.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
    }

    override fun getNewFragment(): Fragment? {
        return createInstance(intent.extras)
    }

    override fun getScreenName(): String {
        return ChatTemplateAnalytics.Companions.SCREEN_TEMPLATE_CHAT_SETTING
    }

    override fun getTagFragment(): String {
        return TAG
    }

    companion object {
        private const val TAG = "TEMPLATE_CHAT_FRAGMENT"
        const val PARAM_IS_SELLER = "PARAM_IS_SELLER"
        fun createInstance(context: Context?, isSeller: Boolean?): Intent {
            val intent = Intent(context, TemplateChatActivity::class.java)
            intent.putExtra(PARAM_IS_SELLER, isSeller)
            return intent
        }
    }
}