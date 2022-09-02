package com.tokopedia.imagepicker_insta.common.ui.analytic

import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface FeedAccountTypeAnalytic {

    fun clickAccountTypeItem(item: FeedAccountUiModel)

    fun clickAccountInfo()

    fun clickChangeAccountToSeller()
}