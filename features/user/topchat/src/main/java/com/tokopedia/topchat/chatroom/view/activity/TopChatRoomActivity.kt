package com.tokopedia.topchat.chatroom.view.activity

import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.BaseChatToolbarActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.MODE_DEFAULT_GET_CHAT
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNull
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX
import com.tokopedia.topchat.common.analytics.TopChatAnalytics

open class TopChatRoomActivity : BaseChatToolbarActivity() {

    override fun getScreenName(): String {
        return "/${TopChatAnalytics.Category.CHAT_DETAIL}"
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        scanPathQuery(intent.data)

        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }

        return createChatRoomFragment(bundle)
    }

    protected open fun createChatRoomFragment(bundle: Bundle) = TopChatRoomFragment.createInstance(bundle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        useLightNotificationBar()
        initWindowBackground()
        initTopchatToolbar()
    }

    private fun initWindowBackground() {
        window.decorView.setBackgroundColor(Color.WHITE)
    }

    private fun initTopchatToolbar() {
        supportActionBar?.setBackgroundDrawable(null)
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    override fun setupToolbar() {
        super.setupToolbar()
        decreaseToolbarElevation()
    }

    private fun decreaseToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }

    private fun scanPathQuery(data: Uri?) {
        data?.let {
            val pathSegments = it.pathSegments
            when {
                pathSegments.contains(ApplinkConst.Chat.PATH_ASK_SELLER) -> {
                    val toShopId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    val shopName = it.getQueryParameter(ApplinkConst.Chat.OPPONENT_NAME).toEmptyStringIfNull()
                    val customMessage = it.getQueryParameter(ApplinkConst.Chat.CUSTOM_MESSAGE).toEmptyStringIfNull()
                    val avatar = it.getQueryParameter(ApplinkConst.Chat.AVATAR).toEmptyStringIfNull()
                    val source = it.getQueryParameter(ApplinkConst.Chat.SOURCE) ?: "deeplink"
                    intent.putExtra(ApplinkConst.Chat.SOURCE, source)

                    val model = ChatRoomHeaderViewModel()
                    model.name = shopName
                    model.label = LABEL_SELLER
                    model.senderId = toShopId
                    model.role = ROLE_SELLER
                    model.mode = MODE_DEFAULT_GET_CHAT
                    model.keyword = ""
                    model.image = avatar
                    intent.putExtra(ApplinkConst.Chat.PARAM_HEADER, model)
                    intent.putExtra(ApplinkConst.Chat.CUSTOM_MESSAGE, customMessage)
                    intent.putExtra(RESULT_INBOX_CHAT_PARAM_INDEX, -1)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, "")
                    intent.putExtra(ApplinkConst.Chat.TO_SHOP_ID, toShopId)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, toShopId)
                }
                pathSegments.contains(ApplinkConst.Chat.PATH_ASK_BUYER) -> {
                    val toUserId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    val shopName = it.getQueryParameter(ApplinkConst.Chat.OPPONENT_NAME).toEmptyStringIfNull()
                    val customMessage = it.getQueryParameter(ApplinkConst.Chat.CUSTOM_MESSAGE).toEmptyStringIfNull()
                    val avatar = it.getQueryParameter(ApplinkConst.Chat.AVATAR).toEmptyStringIfNull()
                    val source = it.getQueryParameter(ApplinkConst.Chat.SOURCE) ?: "deeplink"
                    intent.putExtra(ApplinkConst.Chat.SOURCE, source)

                    val model = ChatRoomHeaderViewModel()
                    model.name = shopName
                    model.label = LABEL_USER
                    model.senderId = toUserId
                    model.role = ROLE_USER
                    model.mode = MODE_DEFAULT_GET_CHAT
                    model.keyword = ""
                    model.image = avatar
                    intent.putExtra(ApplinkConst.Chat.PARAM_HEADER, model)
                    intent.putExtra(ApplinkConst.Chat.CUSTOM_MESSAGE, customMessage)
                    intent.putExtra(RESULT_INBOX_CHAT_PARAM_INDEX, -1)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, "")
                    intent.putExtra(ApplinkConst.Chat.TO_USER_ID, toUserId)
                    intent.putExtra(ApplinkConst.Chat.OPPONENT_ID, toUserId)
                }
                else -> {
                    val messageId = intent?.data?.lastPathSegment.toZeroStringIfNull()
                    intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
                }
            }
        }
    }

    companion object {
        val REQUEST_CODE_CHAT_IMAGE = 2325
        val LABEL_USER = "Pengguna"
        val LABEL_SELLER = "Penjual"
        val ROLE_SELLER = "shop"
        val ROLE_USER = "user"
    }

}
