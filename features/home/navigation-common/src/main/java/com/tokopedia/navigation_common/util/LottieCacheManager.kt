package com.tokopedia.navigation_common.util

import android.content.Context
import com.airbnb.lottie.LottieCompositionFactory

class LottieCacheManager(
    private val context: Context
) {

    private val loadedLottieAssets = mutableSetOf<String>()

    fun cacheFromUrls(urls: List<String>) {
        urls.forEach { cacheFromUrl(it) }
    }

    fun cacheFromUrl(url: String) {
        LottieCompositionFactory.fromUrl(context, url)
            .addListener { loadedLottieAssets.add(url) }
    }

    fun isUrlCached(url: String): Boolean {
        return loadedLottieAssets.contains(url)
    }
}
