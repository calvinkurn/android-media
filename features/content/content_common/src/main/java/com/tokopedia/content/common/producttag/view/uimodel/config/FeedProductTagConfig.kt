package com.tokopedia.content.common.producttag.view.uimodel.config

/**
 * Created By : Jonathan Darwin on August 23, 2022
 */
class FeedProductTagConfig : ContentProductTagConfig() {

    override val isMultipleSelectionProduct: Boolean
        get() = false

    override val isFullPageAutocomplete: Boolean
        get() = false
}