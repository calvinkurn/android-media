package com.tokopedia.chatbot.view.activity

import RemoteConfigHelper
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.fragment.ChatbotFragment2
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.view.fragment.ChatbotFragment
import com.tokopedia.chatbot.view.util.isInDarkMode
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
        var list = emptyList<String>()
        var pageSource = ""
        var isChatbotActive = ""
        intent?.data?.let {
            bundle.putString(DEEP_LINK_URI, it.toString())
            list = UriUtil.destructureUri(
                ApplinkConstInternalGlobal.CHAT_BOT + "/{id}",
                it,
                true
            )
            pageSource = it.getQueryParameter("page_source").orEmpty()
            isChatbotActive = it.getQueryParameter("is_chatbot_active").orEmpty()
            bundle.putString(PAGE_SOURCE, pageSource)
            if (isChatbotActive.isEmpty() || isChatbotActive=="true")
                bundle.putBoolean(IS_CHATBOT_ACTIVE, true)
            else
                bundle.putBoolean(IS_CHATBOT_ACTIVE, false)
        }
        if (!list.isNullOrEmpty()) {
            bundle.putString(MESSAGE_ID, list[0])
        }

        val state = remoteConfigForChatbotMVVM()
        return if (state) {
            val fragment = ChatbotFragment2()
            fragment.arguments = bundle
            fragment
        } else {
            val fragment = ChatbotFragment()
            fragment.arguments = bundle
            fragment
        }
    }

    private fun remoteConfigForChatbotMVVM(): Boolean {
        return RemoteConfigHelper.isRemoteConfigForMVVM(this)
    }

    companion object {

        const val MESSAGE_ID = "message_id"
        const val PAGE_SOURCE = "page_source"
        const val DEEP_LINK_URI = "deep_link_uri"
        const val IS_CHATBOT_ACTIVE = "is_chatbot_active"
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

    override fun getChatHeaderLayout(): Int = R.layout.customview_chatbot_header_layout

    override fun setupToolbar() {
        super.setupToolbar()
        supportActionBar?.run {
            setBackgroundDrawable(ColorDrawable(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0)))
        }
        val userAvatar = findViewById<ImageView>(R.id.user_avatar)
        userAvatar.apply {
            if (userAvatar.isInDarkMode()) {
                setImageResource(R.drawable.ic_tanya_dark_mode)
            } else {
                setImageResource(R.drawable.chatbot_avatar)
            }
        }
        (findViewById<TextView>(R.id.title)).text = getString(R.string.cb_bot_toolbar_title)
    }

    fun upadateToolbar(profileName: String?, profileImage: String?, badgeImage: ToolbarAttributes.BadgeImage?) {
        profileImage?.let { ImageHandler.loadImageCircle2(this, findViewById(R.id.user_avatar), it) }
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
