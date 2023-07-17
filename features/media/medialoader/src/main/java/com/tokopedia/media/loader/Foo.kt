package com.tokopedia.media.loader

import android.widget.ImageView
import com.tokopedia.media.loader.data.Properties

object Foo {

    inline fun ImageView.loadImage1(
        resource: Int,
        crossinline properties: Properties.() -> Unit
    ) = call(resource, Properties()
        .apply(properties))

    inline fun ImageView.loadImage2(
        resource: Int,
        properties: Properties.() -> Unit
    ) = call(resource, Properties()
        .apply(properties))

    inline fun ImageView.loadImage3(
        resource: Int,
        noinline properties: Properties.() -> Unit
    ) = call(resource, Properties()
        .apply(properties))

    fun ImageView.loadImage4(
        resource: Int,
        properties: Properties.() -> Unit
    ) = call(resource, Properties()
        .apply(properties))
}
