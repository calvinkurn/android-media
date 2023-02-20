package com.tokopedia.media.loader.options

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import com.bumptech.glide.request.RequestOptions
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.data.PARAM_BLURHASH
import com.tokopedia.media.loader.data.PLACEHOLDER_RES_UNIFY
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.transform.BlurHashDecoder
import com.tokopedia.media.loader.utils.AspectRatio
import com.tokopedia.media.loader.utils.isValidUrl
import com.tokopedia.media.loader.utils.toUri

class PlaceholderOptions constructor(
    private val context: Context,
    private val properties: Properties
) : RequestOptions() {

    /*
    * The blurhash (built-in) list,
    * it will be use as default if the image didn't have the hash
    * in the URL. the blurhashes will randomly rendering */
    private val blurHashes = listOf(
        "A4ADcRuO_2y?",
        "A9K{0B#R3WyY",
        "AHHUnD~V^ia~",
        "A2N+X[~qv]IU",
        "ABP?2U~X5J^~"
    )

    init {
        val placeHolder = properties.placeHolder

        when {
            placeHolder.isZero() -> {
                setStaticPlaceHolder()
            }
            placeHolder.isMoreThanZero() -> {
                setCustomPlaceHolder()
            }
        }
    }

    private fun setCustomPlaceHolder() {
        placeholder(
            ContextCompat.getDrawable(
                context,
                properties.placeHolder
            )
        )
    }

    private fun setStaticPlaceHolder() {
        val blurHash = properties.blurHash
        val data = properties.data.toString()

        if (blurHash && data.isValidUrl()) {
            val hash = data.toUri()
                ?.getQueryParameter(PARAM_BLURHASH)
                ?: blurHashes.random()

            placeholder(
                BitmapDrawable(
                    context.resources, generateBlurHash(
                        hash = hash,
                        width = properties.imageViewSize.first,
                        height = properties.imageViewSize.second
                    )
                )
            )
        } else {
            placeholder(
                ContextCompat.getDrawable(
                    context,
                    PLACEHOLDER_RES_UNIFY
                )
            )
        }
    }

    private fun generateBlurHash(hash: String?, width: Int?, height: Int?): Bitmap? {
        val ratio = AspectRatio.calculate(
            (width ?: 2) * 10, // default value is 2*10 = 20 px
            (height ?: 2) * 10 // default value is 2*10 = 20 px
        )

        return BlurHashDecoder.decode(
            blurHash = hash,
            width = ratio.first,
            height = ratio.second
        )
    }

    companion object {
        @Volatile
        private var factory: PlaceholderOptions? = null

        fun build(context: Context, properties: Properties): RequestOptions {
            return factory ?: PlaceholderOptions(context, properties).also {
                factory = it
            }
        }
    }
}
