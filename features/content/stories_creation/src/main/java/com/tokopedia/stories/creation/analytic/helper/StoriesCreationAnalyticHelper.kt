package com.tokopedia.stories.creation.analytic.helper

import com.tokopedia.content.analytic.Value
import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on October 17, 2023
 */
object StoriesCreationAnalyticHelper {

    fun getEventLabelByAccount(account: ContentAccountUiModel): String {
        return "${account.id} - ${getAccountType(account)}"
    }

    private fun getAccountType(account: ContentAccountUiModel): String {
        return when {
            account.isShop -> Value.seller
            account.isUser -> Value.user
            else -> ""
        }
    }
}
