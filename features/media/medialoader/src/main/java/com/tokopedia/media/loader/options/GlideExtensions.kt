package com.tokopedia.media.loader.options

import android.content.Context
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.BaseRequestOptions
import com.tokopedia.media.loader.data.Properties

@GlideExtension
/** @suppress */
open class GlideExtensions private constructor() {

    companion object {
        @JvmStatic
        @GlideOption
        fun commonOptions(
            requestOptions: BaseRequestOptions<*>,
            properties: Properties
        ) = requestOptions.apply {
            CommonOptions(properties, this)
        }

        @JvmStatic
        @GlideOption
        fun transform(
            requestOptions: BaseRequestOptions<*>,
            properties: Properties
        ) = requestOptions.apply {
            MultiTransformationOptions(properties, this)
        }

        @JvmStatic
        @GlideOption
        fun dynamicPlaceHolder(
            requestOptions: BaseRequestOptions<*>,
            context: Context,
            properties: Properties
        ) = requestOptions.apply {
            PlaceholderOptions(context, properties, this)
        }
    }
}
