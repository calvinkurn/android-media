package com.tokopedia.topchat.common

import android.content.Context
import android.content.Intent
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatBlockResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatBlockStatus
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.view.activity.ChatRoomSettingsActivity

/**
 * @author by nisie on 07/01/19.
 */
open class TopChatInternalRouter {

    object Companion {

        const val CHAT_DELETED_RESULT_CODE = 111
        const val CHAT_READ_RESULT_CODE = 112

        const val RESULT_INBOX_CHAT_PARAM_INDEX = "position"
        const val RESULT_INBOX_CHAT_PARAM_MUST_REFRESH = "must_refresh"
        const val RESULT_INBOX_CHAT_PARAM_MOVE_TO_TOP = "move_to_top"

        const val RESULT_CHAT_SETTING_IS_BLOCKED = "is_blocked"
        const val RESULT_CHAT_SETTING_IS_PROMO_BLOCKED = "is_promo_blocked"
        const val RESULT_CHAT_SETTING_BLOCKED_UNTIL = "blocked_until"


        const val TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY = "SELECTED_INVOICE"

        fun getAttachProductIntent(context: Context, shopId: String, shopName: String,
                                   isSeller: Boolean): Intent {
            return AttachProductActivity.createInstance(context, shopId, shopName, isSeller,
                    AttachProductActivity.SOURCE_TOPCHAT)
        }

        fun getChatSettingIntent(context: Context, messageId: String, opponentRole: String,
                                 opponentName: String, isBlocked: Boolean, isPromoBlocked:
                                 Boolean, blockedUntil: String, shopId: Int): Intent {
            return ChatRoomSettingsActivity.getIntent(context,
                    messageId,
                    ChatSettingsResponse(ChatBlockResponse(
                            isBlocked,
                            ChatBlockStatus(
                                    isBlocked,
                                    isPromoBlocked,
                                    blockedUntil
                            )
                    )),
                    isChatEnabled(opponentRole, isBlocked, isPromoBlocked),
                    opponentRole,
                    opponentName,
                    shopId)
        }

        private fun isChatEnabled(opponentRole: String, isBlocked: Boolean, isPromoBlocked:
        Boolean): Boolean {
            return when {
                opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_OFFICIAL)
                -> { !isPromoBlocked }
                opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_SHOP)
                -> { !isBlocked && !isPromoBlocked}
                opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_USER)
                -> { !isBlocked }
                else -> { true }
            }
        }

    }
}