package com.tokopedia.chatbot.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.chatbot.view.fragment.ChatbotFragment

class InstrumentationChatbotTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = Bundle()
        val list = UriUtil.destructureUri(ApplinkConstInternalGlobal.CHAT_BOT+"/{id}",intent.data!!,true)
        if(!list.isNullOrEmpty()){
            bundle.putString(ChatbotActivity.MESSAGE_ID,list[0])
        }
        bundle.putString(ChatbotActivity.DEEP_LINK_URI,intent.data.toString())
        val fragment = ChatbotFragment()
        fragment.arguments = bundle
        val fragmentTransaction = supportFragmentManager
            .beginTransaction()
        fragmentTransaction
            .replace(com.tokopedia.abstraction.R.id.parent_view, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}