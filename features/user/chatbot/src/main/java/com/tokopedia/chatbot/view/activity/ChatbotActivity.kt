package com.tokopedia.chatbot.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.chatbot.view.fragment.ChatbotFragment

/**
 * @author by nisie on 23/11/18.
 */
class ChatbotActivity : BaseChatToolbarActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        val fragment = ChatbotFragment()
        fragment.arguments = bundle
        return fragment
    }

    companion object {

        @JvmStatic
        fun getCallingIntent(messageId: String, context: Context): Intent {
            val intent = Intent(context, ChatbotActivity::class.java)
            val bundle = Bundle()
            bundle.putString(ApplinkConst.Chat.MESSAGE_ID, messageId)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun getCallingIntent(context: Context, messageId: String, name: String,
                             label: String, senderId: String, role: String, mode: Int,
                             keyword: String, image: String): Intent {
            val intent = Intent(context, ChatbotActivity::class.java)
            intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
            val model = ChatRoomHeaderViewModel()
            model.name = name
            model.label = label
            model.senderId = senderId
            model.role = role
            model.mode = mode
            model.keyword = keyword
            model.image = image
            intent.putExtra(ApplinkConst.Chat.PARAM_HEADER, model)
            return intent
        }
    }

    object DeepLinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.CHATBOT)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, ChatbotActivity::class.java)
                    .setData(uri.build())
                    .putExtras(extras)
        }

    }
}