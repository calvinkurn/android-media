package com.tokopedia.content.common.ui.analytic

import com.tokopedia.content.common.ui.model.FeedAccountUiModel

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface FeedAccountTypeAnalytic {

    fun clickAccountTypeItem(item: FeedAccountUiModel)

    fun clickAccountInfo()

    fun clickChangeAccountToSeller()
}