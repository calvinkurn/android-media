package com.tokopedia.navigation_common.util

import android.content.Context
import com.airbnb.lottie.LottieCompositionFactory

class LottieCacheManager(
    private val context: Context
) {

    private val loadedLottieAssets = mutableSetOf<String>()

    fun preloadFromUrls(urls: List<String>) {
        urls.forEach { preloadFromUrl(it) }
    }

    fun preloadFromUrl(url: String) {
        LottieCompositionFactory.fromUrl(context, url)
            .addListener { loadedLottieAssets.add(url) }
    }

    fun isUrlLoaded(url: String): Boolean {
        return loadedLottieAssets.contains(url)
    }
}
