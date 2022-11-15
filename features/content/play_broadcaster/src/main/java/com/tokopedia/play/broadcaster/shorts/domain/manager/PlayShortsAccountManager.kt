package com.tokopedia.play.broadcaster.shorts.domain.manager

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on November 14, 2022
 */
interface PlayShortsAccountManager {

    suspend fun getBestEligibleAccount(
        accountList: List<ContentAccountUiModel>,
        preferredAccountType: String
    ): ContentAccountUiModel

    fun isAllowChangeAccount(
        accountList: List<ContentAccountUiModel>
    ): Boolean

    fun switchAccount(
        accountList: List<ContentAccountUiModel>,
        currentAccountType: String
    ): ContentAccountUiModel
}
