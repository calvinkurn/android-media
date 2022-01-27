package com.tokopedia.topchat.chattemplate.view.activity

import android.content.Context
import com.tokopedia.topchat.chattemplate.view.fragment.EditTemplateChatFragment.Companion.createInstance
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import android.os.Bundle
import com.tokopedia.topchat.common.InboxMessageConstant
import com.tokopedia.topchat.chattemplate.analytics.ChatTemplateAnalytics
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifyprinciples.R

open class EditTemplateChatActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null && intent.extras != null) {
            val message = intent.extras!!.getString(InboxMessageConstant.PARAM_MESSAGE)
            setToolbarTitle(message == null)
        }
        toolbar.setBackgroundColor(MethodChecker.getColor(this, R.color.Unify_Background))
    }

    override fun getNewFragment(): Fragment {
        return createInstance(intent.extras)
    }

    private fun setToolbarTitle(isAdd: Boolean) {
        if (isAdd) {
            toolbar.title = getString(com.tokopedia.topchat.R.string.add_template_chat_title)
        } else {
            toolbar.title = getString(com.tokopedia.topchat.R.string.edit_template_chat_title)
        }
    }

    override fun getScreenName(): String {
        return ChatTemplateAnalytics.Companions.SCREEN_TEMPLATE_CHAT_SET
    }

    companion object {
        private const val TAG = "EDIT_TEMPLATE_CHAT_FRAGMENT"
        fun createInstance(context: Context?): Intent {
            return Intent(context, EditTemplateChatActivity::class.java)
        }
    }
}