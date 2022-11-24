package com.tokopedia.play.broadcaster.shorts.analytic.helper

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.analytic.const.Value

/**
 * Created By : Jonathan Darwin on November 24, 2022
 */
object PlayShortsAnalyticHelper {

    fun getEventLabelByAccount(account: ContentAccountUiModel): String {
        return "${account.id} - ${getAccountType(account)}"
    }

    fun getAccountType(account: ContentAccountUiModel): String {
        return when {
            account.isShop -> Value.SHORTS_TYPE_SELLER
            account.isUser -> Value.SHORTS_TYPE_USER
            else -> ""
        }
    }
}
