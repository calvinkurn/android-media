package com.tokopedia.navigation.util

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Type
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.util.LottieCacheManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class AssetPreloadManager @Inject constructor(
    private val context: Context,
    private val lottieCacheManager: LottieCacheManager,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun preloadByCacheTask(cacheTask: CompletableTask<List<BottomNavBarUiModel>>) = withContext(dispatchers.io) {
        val assets = cacheTask.items.flatMap { it.assets.values }
        val jumperAssets = cacheTask.items.flatMap { it.jumper?.assets?.values.orEmpty() }
        val imageAssets = assets.filterIsInstance<Type.ImageUrl>() + jumperAssets.filterIsInstance<Type.ImageUrl>()
        val lottieAssets = assets.filterIsInstance<Type.LottieUrl>() + jumperAssets.filterIsInstance<Type.LottieUrl>()

        val cacheImages = async { cacheImageAssets(imageAssets) }
        val cacheLotties = async { cacheLottieAssets(lottieAssets) }

        if (cacheImages.await()) cacheTask.completeTask()
    }

    private suspend fun cacheImageAssets(assets: List<Type.ImageUrl>) = coroutineScope {
        val results = assets.map {
            async {
                suspendCancellableCoroutine { cont ->
                    it.url.getBitmapImageUrl(
                        context,
                        properties = {
                            setCacheStrategy(MediaCacheStrategy.DATA)
                        },
                        target = MediaBitmapEmptyTarget(
                            onReady = {
                                cont.resume(it)
                            },
                            onFailed = {
                                cont.resume(null)
                            }
                        )
                    )

                    cont.invokeOnCancellation { }
                }
            }
        }.map {
            val result = it.await()
            result != null
        }

        results.all { it }
    }

    private suspend fun cacheLottieAssets(assets: List<Type.LottieUrl>) = coroutineScope {
        val results = assets.map {
            async {
                lottieCacheManager.preloadSyncFromUrl(it.url)
            }
        }.map {
            val result = it.await()
            result.value != null
        }

        results.all { it }
    }
}
