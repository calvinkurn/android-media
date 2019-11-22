package com.tokopedia.topchat.chatsetting.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent
import com.tokopedia.topchat.chatsetting.di.DaggerChatSettingComponent
import com.tokopedia.topchat.chatsetting.view.fragment.ChatSettingFragment
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity

class ChatSettingActivity : BaseSimpleActivity(), HasComponent<ChatSettingComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }

    override fun getNewFragment(): Fragment? {
        val arg = intent.extras
        return ChatSettingFragment.create(arg)
    }

    override fun getComponent(): ChatSettingComponent {
        return DaggerChatSettingComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        fun getIntent(context: Context?, isTabSeller: Boolean): Intent {
            return Intent(context, ChatSettingActivity::class.java).apply {
                putExtra(TemplateChatActivity.PARAM_IS_SELLER, isTabSeller)
            }
        }
    }
}
