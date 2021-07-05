package com.tokopedia.thankyou_native

object TkpdIdlingResourceProvider {

    var resource: TkpdIdlingResource? = null
    fun provideIdlingResource(name: String): TkpdIdlingResource? {
        if (resource == null)
            resource = TkpdIdlingResource(name)
        return resource
    }
}