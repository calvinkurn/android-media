package com.tokopedia.catalog.utils

import androidx.annotation.VisibleForTesting

object TkpdIdlingResourceProvider
{

    var resource: TkpdIdlingResource? = null

    @VisibleForTesting
    fun provideIdlingResource(name: String): TkpdIdlingResource? {
        if (resource == null)
            resource = TkpdIdlingResource(name)
        return resource
    }
}
