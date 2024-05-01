package com.tokopedia.navigation.util

import android.content.Context
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.navigation_common.util.LottieCacheManager
import java.lang.ref.WeakReference
import javax.inject.Inject

class AssetPreloadManager @Inject constructor(
    private val weakRefContext: WeakReference<Context>,
    private val lottieCacheManager: LottieCacheManager
) {

    fun preloadLottieAsset(url: String) {
        lottieCacheManager.preloadFromUrl(url)
    }

    fun preloadLottieAssets(urls: List<String>) {
        lottieCacheManager.preloadFromUrls(urls)
    }

    fun preloadImage(url: String) {
        val context = this.weakRefContext.get() ?: return
        GlideApp.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .submit()
    }
}
