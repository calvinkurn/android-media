package com.tokopedia.topchat.chatroom.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.MODE_DEFAULT_GET_CHAT
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.common.InboxChatConstant
import com.tokopedia.topchat.common.InboxMessageConstant


class TopChatRoomActivity : BaseChatToolbarActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return TopChatRoomFragment.createInstance(bundle)
    }

    companion object {

        public val REQUEST_CODE_CHAT_IMAGE = 2325
        val MAX_SIZE_IMAGE_PICKER = 5
        val CHAT_DELETED_RESULT_CODE = 101
        val CHAT_GO_TO_SHOP_DETAILS_REQUEST = 202
        val LABEL_USER = "Pengguna"
        val LABEL_SELLER = "Penjual"
        val ROLE_SELLER = "shop"
        val ROLE_USER = "user"

        /**
         * To create intent with header already initialized.
         */
        @JvmStatic
        fun getCallingIntent(context: Context, messageId: String, name: String,
                             label: String, senderId: String, role: String, mode: Int,
                             keyword: String, image: String): Intent {
            val intent = Intent(context, TopChatRoomActivity::class.java)
            intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
            intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, senderId)
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

        @JvmStatic
        fun getAskSellerIntent(context: Context, toShopId: String,
                               shopName: String, source: String, avatar: String): Intent {
            val intent = getCallingIntent(context, "", shopName, LABEL_SELLER, toShopId, ROLE_SELLER,
                    MODE_DEFAULT_GET_CHAT, "", avatar)
            intent.putExtra(ApplinkConst.Chat.SOURCE, source)
            return intent
        }

        @JvmStatic
        fun getAskSellerIntent(context: Context, toShopId: String, shopName: String,
                               customMessage: String, source: String,
                               avatar: String): Intent {
            val intent = getAskSellerIntent(context, toShopId, shopName, source,
                    avatar)
            val bundle = intent.extras
            bundle.putString(ApplinkConst.Chat.CUSTOM_MESSAGE, customMessage)
            intent.putExtras(bundle)
            return intent
        }

        @JvmStatic
        fun getAskUserIntent(context: Context, userId: String,
                             userName: String, source: String,
                             avatar: String): Intent {
            val intent = getCallingIntent(context, "", userName, LABEL_USER, userId, ROLE_USER,
                    MODE_DEFAULT_GET_CHAT, "", avatar)
            intent.putExtra(ApplinkConst.Chat.SOURCE, source)
            return intent
        }

        @JvmStatic
        fun getAskBuyerIntent(context: Context, toUserId: String, customerName: String,
                              customMessage: String, source: String,
                              avatar: String): Intent {
            val intent = getAskUserIntent(context, toUserId, customerName, source, avatar)
            val bundle = intent.extras
            bundle.putString(ApplinkConst.Chat.CUSTOM_MESSAGE, customMessage)
            intent.putExtras(bundle)
            return intent
        }

    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag(tagFragment).let {
            if (it is TopChatRoomFragment) it.onBackPressedEvent()
            else super.onBackPressed()
        }
    }

    object DeepLinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.TOPCHAT)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, TopChatRoomActivity::class.java)
                    .setData(uri.build())
                    .putExtras(extras)
        }

        @JvmStatic
        @DeepLink(ApplinkConst.TOPCHAT_ASKSELLER)
        fun getAskSellerIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            val toShopId = extras.getString(ApplinkConst.Chat.TO_SHOP_ID,"0")
            val shopName = extras.getString(ApplinkConst.Chat.OPPONENT_NAME,"")
            val customMessage = extras.getString(ApplinkConst.Chat.CUSTOM_MESSAGE,"")
            val avatar = extras.getString(ApplinkConst.Chat.AVATAR,"")
            val source = extras.getString(ApplinkConst.Chat.SOURCE,"deeplink")

            val intent = getAskSellerIntent(context,
                    toShopId,
                    shopName,
                    customMessage,
                    source,
                    avatar
            )
            return intent
        }

    }
}
