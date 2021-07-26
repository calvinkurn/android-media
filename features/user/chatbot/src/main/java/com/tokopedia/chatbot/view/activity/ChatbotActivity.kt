package com.tokopedia.chatbot.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.view.fragment.ChatbotFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.pushnotif.PushNotification
import com.tokopedia.pushnotif.data.constant.Constant


/**
 * @author by nisie on 23/11/18.
 */
class ChatbotActivity : BaseChatToolbarActivity() {


    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        val list = UriUtil.destructureUri(ApplinkConstInternalGlobal.CHAT_BOT+"/{id}",intent.data!!,true)
        if(!list.isNullOrEmpty()){
            bundle.putString(MESSAGE_ID,list[0])
        }
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

    override fun onResume() {
        super.onResume()
        PushNotification.setIsChatBotWindowOpen(true)
        NotificationManagerCompat.from(this).cancel(Constant.NotificationId.CHAT_BOT)
    }

    override fun onPause() {
        super.onPause()
        PushNotification.setIsChatBotWindowOpen(false)
    }

    override fun getChatHeaderLayout() :Int = R.layout.chatbot_header_layout

    override fun setupToolbar() {
        super.setupToolbar()
        findViewById<ImageView>(R.id.user_avatar).setImageResource(R.drawable.chatbot_avatar)
        (findViewById<TextView>(R.id.title)).text = getString(R.string.cb_bot_toolbar_title)
    }

    fun upadateToolbar(profileName: String?, profileImage: String?, badgeImage: ToolbarAttributes.BadgeImage?) {
        profileImage?.let { ImageHandler.loadImageCircle2(this, findViewById<ImageView>(R.id.user_avatar), it) }
        (findViewById<TextView>(R.id.title)).text = profileName
        val badge = findViewById<ImageView>(R.id.chatbotHeaderBadge)
        if (badgeImage?.light.isNullOrEmpty()) {
            badge.hide()
        } else {
            badge.show()
            ImageHandler.loadImageFitCenter(this, badge, badgeImage?.light)
        }


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}