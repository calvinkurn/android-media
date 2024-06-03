package com.tokopedia.navigation_common.util

import android.content.Context
import androidx.annotation.WorkerThread
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LottieCacheManager(
    private val context: Context,
) {

    private val loadedLottieAssets = mutableSetOf<String>()

    fun preloadFromUrl(url: String) {
        LottieCompositionFactory.fromUrl(context, url)
            .addListener { loadedLottieAssets.add(url) }
    }

    @WorkerThread
    fun preloadSyncFromUrl(url: String): LottieResult<LottieComposition> {
        return LottieCompositionFactory.fromUrlSync(context, url)
    }

    fun isUrlLoaded(url: String): Boolean {
        return loadedLottieAssets.contains(url)
    }
}
