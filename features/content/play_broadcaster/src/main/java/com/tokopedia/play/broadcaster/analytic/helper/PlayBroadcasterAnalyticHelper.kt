package com.tokopedia.play.broadcaster.analytic.helper

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.const.Value

/**
 * Created By : Jonathan Darwin on March 17, 2023
 */
internal object PlayBroadcasterAnalyticHelper {

    fun getEventLabelByAccount(account: ContentAccountUiModel): String {
        return "${account.id} - ${getAccountType(account)}"
    }

    fun getEventLabelByAccount(authorId: String, authorType: String): String {
        return "$authorId - ${getAccountType(authorType)}"
    }

    fun getAccountType(account: ContentAccountUiModel): String {
        return when {
            account.isShop -> Value.BROADCASTER_TYPE_SELLER
            account.isUser -> Value.BROADCASTER_TYPE_USER
            else -> ""
        }
    }

    fun getAccountType(authorType: String): String {
        return when (authorType) {
            ContentCommonUserType.TYPE_SHOP -> Value.BROADCASTER_TYPE_SELLER
            ContentCommonUserType.TYPE_USER -> Value.BROADCASTER_TYPE_USER
            else -> ""
        }
    }

    fun getTrackerIdBySite(mainAppTrackerId: String, sellerAppTrackerId: String): String {
        return if (GlobalConfig.isSellerApp()) {
            sellerAppTrackerId
        } else {
            mainAppTrackerId
        }
    }
}
