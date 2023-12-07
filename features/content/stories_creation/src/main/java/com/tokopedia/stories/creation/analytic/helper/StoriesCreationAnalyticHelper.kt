package com.tokopedia.stories.creation.analytic.helper

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.Value
import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on October 17, 2023
 */
object StoriesCreationAnalyticHelper {

    fun getEventLabelByAccount(account: ContentAccountUiModel): String {
        return "${account.id} - ${getAccountType(account)}"
    }

    fun getEventLabel(account: ContentAccountUiModel, storyId: String): String {
        return "${getEventLabelByAccount(account)} - $storyId"
    }

    fun getTrackerIdBySite(mainAppTrackerId: String, sellerAppTrackerId: String): String {
        return if (GlobalConfig.isSellerApp()) {
            sellerAppTrackerId
        } else {
            mainAppTrackerId
        }
    }

    private fun getAccountType(account: ContentAccountUiModel): String {
        return when {
            account.isShop -> Value.seller
            account.isUser -> Value.user
            else -> ""
        }
    }
}
