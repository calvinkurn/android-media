package com.tokopedia.play.widget.util

import android.content.Context
import com.airbnb.lottie.LottieCompositionFactory

/**
 * Created by kenny.hadisaputra on 23/10/23
 */
class PlayWidgetLottieLoadHelper(private val context: Context) {

    private val downloadedLottieSet = mutableSetOf<String>()

    fun preload(vararg urls: String) {
        urls.forEach { url ->
            LottieCompositionFactory.fromUrl(context, url)
                .addListener {
                    downloadedLottieSet.add(url)
                }
        }
    }

    fun hasLoaded(url: String): Boolean {
        return downloadedLottieSet.contains(url)
    }
}
