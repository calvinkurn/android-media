package com.tokopedia.chatbot.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chatbot.ChatbotConstant.VIDEO_URL
import com.tokopedia.chatbot.view.fragment.ChatbotVideoFragment

class ChatbotVideoActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val extras = Bundle()
        extras.putString(PARAM_VIDEO_URL, intent.getStringExtra(VIDEO_URL))
        return ChatbotVideoFragment.getInstance(extras)
    }

    companion object {
        const val PARAM_VIDEO_URL = "video_url"
    }
}