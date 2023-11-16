package com.tokopedia.stories.utils.provider

import com.tokopedia.universal_sharing.view.model.LinkProperties

/**
 * Created By : Jonathan Darwin on November 14, 2023
 */
object StoriesLinkPropertiesProvider {

    private val emptyLinkProperties = LinkProperties()

    fun get(): LinkProperties {
        return emptyLinkProperties
    }
}
