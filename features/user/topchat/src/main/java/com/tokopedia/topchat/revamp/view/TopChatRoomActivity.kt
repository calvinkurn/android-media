package com.tokopedia.topchat.revamp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel

class TopChatRoomActivity : BaseChatToolbarActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putString(PARAM_MESSAGE_ID, intent.getStringExtra(PARAM_MESSAGE_ID))
        bundle.putString(PARAM_SENDER_ID, intent.getStringExtra(PARAM_SENDER_ID))
        return TopChatRoomFragment.createInstance(bundle)
    }



    companion object {

        public val REQUEST_CODE_CHAT_IMAGE = 2325
        val MAX_SIZE_IMAGE_PICKER = 5
        val CHAT_DELETED_RESULT_CODE = 101
        val CHAT_GO_TO_SHOP_DETAILS_REQUEST = 202

        /**
         * To create intent with header already initialized.
         */
        @JvmStatic
        fun getCallingIntent(context: Context, messageId: String, name: String,
                             label: String, senderId: String, role: String, mode: Int,
                             keyword: String, image: String): Intent {
            val intent = Intent(context, TopChatRoomActivity::class.java)
            intent.putExtra(PARAM_MESSAGE_ID, messageId)
            intent.putExtra(PARAM_SENDER_ID, senderId)
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

    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag(tagFragment).let {
            if (it is TopChatRoomFragment) it.onBackPressedEvent()
            else super.onBackPressed()
        }
    }
}
