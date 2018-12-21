package com.tokopedia.topchat.revamp.view

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.topchat.revamp.di.DaggerTopChatRoomComponent
import com.tokopedia.topchat.revamp.di.TopChatRoomComponent

class TopChatRoomActivity : BaseChatToolbarActivity(), HasComponent<TopChatRoomComponent> {

    override fun getComponent(): TopChatRoomComponent {
        return DaggerTopChatRoomComponent.builder().
                baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putString(ApplinkConst.Chat.MESSAGE_ID, intent.getStringExtra(ApplinkConst.Chat.MESSAGE_ID))
        return TopChatRoomFragment.createInstance(bundle)
    }
}
