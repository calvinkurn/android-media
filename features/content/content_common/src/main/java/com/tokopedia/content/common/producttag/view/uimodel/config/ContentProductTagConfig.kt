package com.tokopedia.content.common.producttag.view.uimodel.config

import com.tokopedia.content.common.producttag.util.PAGE_SOURCE_FEED
import com.tokopedia.content.common.producttag.util.PAGE_SOURCE_PLAY

/**
 * Created By : Jonathan Darwin on August 23, 2022
 */
abstract class ContentProductTagConfig {

    abstract val isMultipleSelectionProduct: Boolean
    abstract val isFullPageAutocomplete: Boolean

    companion object {
        fun mapFromString(s: String): ContentProductTagConfig {
            return when(s) {
                PAGE_SOURCE_FEED -> FeedProductTagConfig()
                PAGE_SOURCE_PLAY -> PlayProductTagConfig()
                else -> UnknownProductTagConfig()
            }
        }
    }
}