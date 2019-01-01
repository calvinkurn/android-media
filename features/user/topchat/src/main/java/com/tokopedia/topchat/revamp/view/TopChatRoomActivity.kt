package com.tokopedia.topchat.revamp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.topchat.common.InboxMessageConstant.PARAM_MESSAGE_ID

class TopChatRoomActivity : BaseChatToolbarActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putString(ApplinkConst.Chat.MESSAGE_ID, intent.getStringExtra(ApplinkConst.Chat.MESSAGE_ID))
        return TopChatRoomFragment.createInstance(bundle)
    }



    companion object {

        /**
         * To create intent with header already initialized.
         */
        @JvmStatic
        fun getCallingIntent(context: Context, messageId: String, name: String,
                             label: String, senderId: String, role: String, mode: Int,
                             keyword: String, image: String): Intent {
            val intent = Intent(context, TopChatRoomActivity::class.java)
            intent.putExtra(PARAM_MESSAGE_ID, messageId)
            val model = ChatRoomHeaderViewModel()
            model.name = name
            model.label = label
            model.senderId = senderId
            model.role = role
            model.mode = mode
            model.keyword = keyword
            model.image = image
            intent.putExtra(PARAM_HEADER, model)
            return intent
        }
    }
}
