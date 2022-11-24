package com.tokopedia.play.broadcaster.analytic.entrypoint

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on November 24, 2022
 */
interface PlayShortsEntryPointAnalytic {

    fun clickShortsEntryPoint(accountId: String, accountType: String)

    fun clickCloseShortsEntryPointCoachMark(accountId: String, accountType: String)

    fun viewShortsEntryPoint(accountId: String, accountType: String)
}
