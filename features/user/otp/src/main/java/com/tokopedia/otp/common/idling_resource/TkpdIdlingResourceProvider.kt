package com.tokopedia.otp.common.idling_resource

object TkpdIdlingResourceProvider {
    var resource: TkpdIdlingResource? = null
    fun provideIdlingResource(name: String): TkpdIdlingResource? {
        if (resource == null)
            resource = TkpdIdlingResource(name)
        return resource
    }
}