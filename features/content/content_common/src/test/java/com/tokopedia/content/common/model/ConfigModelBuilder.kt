package com.tokopedia.content.common.model

import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig

/**
 * Created By : Jonathan Darwin on September 01, 2022
 */
class ConfigModelBuilder {

    fun buildFeedConfig() = ContentProductTagConfig(
        isMultipleSelectionProduct = false,
        isFullPageAutocomplete = true,
        maxSelectedProduct = 0,
        backButton = ContentProductTagConfig.BackButton.Back,
        isShowActionBarDivider = true,
        appLinkAfterAutocomplete = "",
    )

    fun buildPlayConfig() = ContentProductTagConfig(
        isMultipleSelectionProduct = true,
        isFullPageAutocomplete = false,
        maxSelectedProduct = 5,
        backButton = ContentProductTagConfig.BackButton.Close,
        isShowActionBarDivider = false,
        appLinkAfterAutocomplete = "",
    )
}
