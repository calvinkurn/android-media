package com.tokopedia.content.common.ui.analytic

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface FeedAccountTypeAnalytic {

    fun clickAccountTypeItem(item: ContentAccountUiModel)

    fun clickAccountInfo()

    fun clickChangeAccountToSeller()
}