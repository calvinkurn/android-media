package com.tokopedia.chatbot.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.chatbot.view.fragment.ChatbotFragment
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import android.R.attr.keySet
import com.tokopedia.applink.RouteManager


/**
 * @author by nisie on 23/11/18.
 */
class ChatbotActivity : BaseChatToolbarActivity() {


    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        val list = UriUtil.destructureUri(ApplinkConst.CHATBOT,intent.data,true)
        bundle.putString("message_id",list[0])
        bundle.putString("deep_link_uri",intent.data.toString())
//        if (intent.extras != null) {
//            bundle.putAll(intent.extras)
//        }
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

    //object DeepLinkIntents {
       // @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, ChatbotActivity::class.java)
                    .setData(uri.build())
                    .putExtras(extras)
        }

    //}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //UriUtil.destructureUri("", )
//        val params = UriUtil.destructureUri(ApplinkConst.CHATBOT, intent.data,true)
//        val bundle = Bundle()
//        bundle.putString("data",intent.data.toString())
//        if(bundle!=null){
//            getCallingIntent(this, bundle)
//        }
//        RouteManager.route(this,)
//
//        val extras = Bundle()
//        var uri = "tokopedia://chatbot/76418634"
//        var list = UriUtil.destructureUri(,uri);
//        extras.put

        val list = UriUtil.destructureUri(ApplinkConst.CHATBOT,intent.data,true)
        //messageId = list[0]



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