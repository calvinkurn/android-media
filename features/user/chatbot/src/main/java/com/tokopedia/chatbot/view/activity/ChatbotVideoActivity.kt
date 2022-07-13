package com.tokopedia.chatbot.view.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chatbot.ChatbotConstant.VIDEO_URL
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.fragment.ChatbotVideoFragment
import com.tokopedia.kotlin.extensions.view.gone

class ChatbotVideoActivity : BaseChatToolbarActivity() {

    override fun getNewFragment(): Fragment {
        val extras = Bundle()
        extras.putString(PARAM_VIDEO_URL, intent.getStringExtra(VIDEO_URL))
        return ChatbotVideoFragment.getInstance(extras)
    }

    override fun setupToolbar() {
        super.setupToolbar()
        supportActionBar?.run {
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.chatbot_dms_video_fragment_bg)))
            val upArrow = MethodChecker.getDrawable(applicationContext, com.tokopedia.abstraction.R.drawable.ic_action_back)
            this.setHomeAsUpIndicator(upArrow)
        }
        val textView = (findViewById<TextView>(com.tokopedia.chatbot.R.id.title))
        textView.text =  getString(com.tokopedia.chatbot.R.string.chatbot_video_title)
        textView.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))

        val avatarContainer =  findViewById<FrameLayout>(com.tokopedia.chatbot.R.id.flPhotoContainer)
        avatarContainer.gone()
    }

    companion object {
        const val PARAM_VIDEO_URL = "video_url"
    }
}