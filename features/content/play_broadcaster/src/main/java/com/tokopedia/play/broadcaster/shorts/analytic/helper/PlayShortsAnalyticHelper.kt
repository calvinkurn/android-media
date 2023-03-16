package com.tokopedia.play.broadcaster.shorts.analytic.helper

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.analytic.const.Value

/**
 * Created By : Jonathan Darwin on November 24, 2022
 */
object PlayShortsAnalyticHelper {

    fun getEventLabelByAccount(account: ContentAccountUiModel): String {
        return "${account.id} - ${getAccountType(account)}"
    }

    fun getEventLabelByAccount(authorId: String, authorType: String): String {
        return "$authorId - ${getAccountType(authorType)}"
    }

    fun getAccountType(account: ContentAccountUiModel): String {
        return when {
            account.isShop -> Value.SHORTS_TYPE_SELLER
            account.isUser -> Value.SHORTS_TYPE_USER
            else -> ""
        }
    }

    fun getAccountType(authorType: String): String {
        return when(authorType) {
            ContentCommonUserType.TYPE_SHOP -> Value.SHORTS_TYPE_SELLER
            ContentCommonUserType.TYPE_USER -> Value.SHORTS_TYPE_USER
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
