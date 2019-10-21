package com.tokopedia.chatbot.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.chatbot.view.fragment.ChatbotFragment


/**
 * @author by nisie on 23/11/18.
 */
class ChatbotActivity : BaseChatToolbarActivity() {


    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        val list = UriUtil.destructureUri(ApplinkConstInternalGlobal.CHAT_BOT+"/{id}",intent.data,true)
        bundle.putString(MESSAGE_ID,list[0])
        bundle.putString(DEEP_LINK_URI,intent.data.toString())
        val fragment = ChatbotFragment()
        fragment.arguments = bundle
        return fragment
    }

    companion object {

        const val MESSAGE_ID = "message_id"
        const val DEEP_LINK_URI = "deep_link_uri"

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

   fun upadateToolbar(profileName: String?, profileImage: String?) {
        ImageHandler.loadImageCircle2(this, findViewById<ImageView>(R.id.user_avatar), profileImage)
        (findViewById<TextView>(R.id.title)).text = profileName
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        for (mFragment in fragments) {
            if (mFragment != null && mFragment is ChatbotFragment) {
                mFragment.onBackPressed()
            }
        }
    }

    interface OnBackPressed {
        fun onBackPressed()
    }
}