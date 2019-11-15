package com.tokopedia.topchat.chatsetting.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent
import com.tokopedia.topchat.chatsetting.di.DaggerChatSettingComponent
import com.tokopedia.topchat.chatsetting.view.fragment.ChatSettingFragment

class ChatSettingActivity : BaseSimpleActivity(), HasComponent<ChatSettingComponent> {

    override fun getNewFragment(): Fragment? {
        return ChatSettingFragment()
    }

    override fun getComponent(): ChatSettingComponent {
        return DaggerChatSettingComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        fun getIntent(context: Context?): Intent {
            return Intent(context, ChatSettingActivity::class.java)
        }
    }
}
