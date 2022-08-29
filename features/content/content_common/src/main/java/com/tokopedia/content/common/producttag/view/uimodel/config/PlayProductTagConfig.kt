package com.tokopedia.content.common.producttag.view.uimodel.config

/**
 * Created By : Jonathan Darwin on August 23, 2022
 */
class PlayProductTagConfig : ContentProductTagConfig() {

    override val isMultipleSelectionProduct: Boolean
        get() = true

    override val isFullPageAutocomplete: Boolean
        get() = false
}