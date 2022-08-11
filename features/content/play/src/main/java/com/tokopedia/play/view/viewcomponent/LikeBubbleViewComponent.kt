package com.tokopedia.play.view.viewcomponent

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Size
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play.R
import com.tokopedia.play.view.custom.multiplelikes.PlayLikeBubblesManager
import com.tokopedia.play.view.custom.multiplelikes.PlayLikeBubblesView
import com.tokopedia.play.view.storage.multiplelikes.MultipleLikesIconCacheStorage
import com.tokopedia.play.view.uimodel.PlayLikeBubbleUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeBubbleConfig
import com.tokopedia.play_common.util.PerformanceClassConfig
import com.tokopedia.play_common.view.getBitmapFromUrl
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */
class LikeBubbleViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val scope: CoroutineScope,
    private val iconCacheStorage: MultipleLikesIconCacheStorage,
    private val performanceClassConfig: PerformanceClassConfig,
) : ViewComponent(container, idRes) {

    private val bubbleLikeView = findViewById<PlayLikeBubblesView>(R.id.bubble_like_view)

    private val preferredIconSize = resources.getDimensionPixelSize(
        R.dimen.play_like_bubble_original_size
    )

    private val iconList: List<Bitmap> = listOfNotNull(
        getResizedIconUnifyBitmap(IconUnify.STAR_FILLED),
        getResizedIconUnifyBitmap(IconUnify.HEART_FILLED),
        getResizedIconUnifyBitmap(IconUnify.THUMB_FILLED),
    )

    private val bgColorList: List<Int> = listOf(
        R.color.play_dms_multiplelike_red,
        R.color.play_dms_multiplelike_green,
        R.color.play_dms_multiplelike_purple,
    )

    private val defaultBubbleList: List<PlayLikeBubbleUiModel> = iconList.map {
        PlayLikeBubbleUiModel(it, bgColorList.map(this::getColor))
    }

    private val manager = PlayLikeBubblesManager(
        scope,
        when (performanceClassConfig.performanceClass) {
            PerformanceClassConfig.PERFORMANCE_CLASS_LOW -> MAX_BUBBLES_LOW
            PerformanceClassConfig.PERFORMANCE_CLASS_HIGH -> MAX_BUBBLES_HIGH
            else -> MAX_BUBBLES_AVERAGE
        }
    )

    init {
        manager.setView(bubbleLikeView)
        manager.start()
    }

    fun preloadIcons(iconUrls: Set<String>) {
        scope.launch(Dispatchers.Default) {
            iconUrls.forEach { iconUrl ->
                val isCached = iconCacheStorage.getBitmap(iconUrl) != null
                if (!isCached) {
                    val drawableFromNetwork = try {
                        getBitmapFromUrl(iconUrl)
                    } catch (e: Throwable) { null }

                    if (drawableFromNetwork != null) {
                        iconCacheStorage.addCache(iconUrl, drawableFromNetwork)
                    }
                }
            }
        }
    }

    fun shot(
        amount: Long,
        reduceOpacity: Boolean,
        config: PlayLikeBubbleConfig,
    ) {
        manager.shot(
            likeAmount = amount,
            shotPerBatch = SHOT_PER_BATCH,
            delayPerBatchInMs = SPAMMING_LIKE_DELAY,
            reduceOpacity = reduceOpacity,
            prioritize = !reduceOpacity,
            bubbleList = loadBubblesFromConfig(config),
        )
    }

    fun shotBurst(
        amount: Long,
        reduceOpacity: Boolean,
        config: PlayLikeBubbleConfig,
    ) {
        manager.shot(
            likeAmount = amount,
            shotPerBatch = SHOT_PER_BATCH,
            reduceOpacity = reduceOpacity,
            prioritize = !reduceOpacity,
            bubbleList = loadBubblesFromConfig(config),
        )
    }

    private fun loadBubblesFromConfig(config: PlayLikeBubbleConfig): List<PlayLikeBubbleUiModel> {
        val bubbleList = config.bubbleMap.mapNotNull { entry ->
            val bitmap = iconCacheStorage.getBitmap(entry.key) ?: return@mapNotNull null
            PlayLikeBubbleUiModel(
                icon = bitmap,
                colorList = entry.value,
            )
        }
        return if (bubbleList.isEmpty()) this.defaultBubbleList else bubbleList
    }

    private suspend fun getBitmapFromUrl(url: String): Bitmap? {
        return rootView.context.getBitmapFromUrl(
            url,
            size = Size(preferredIconSize, preferredIconSize)
        )
    }

    private fun getResizedIconUnifyBitmap(iconId: Int): Bitmap? {
        return getIconUnifyDrawable(
            rootView.context,
            iconId,
            getColor(com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        )?.resize(preferredIconSize, preferredIconSize)
    }

    /**
     * Helper to resize IconUnify drawable
     */
    private fun Drawable.resize(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        val bmpScaled = Bitmap.createScaledBitmap(bitmap, width, height, false)
        bitmap.recycle()
        return bmpScaled
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        manager.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        manager.stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        manager.stop()
        manager.setView(null)
    }

    private companion object {
        const val MAX_BUBBLES_LOW = 10
        const val MAX_BUBBLES_AVERAGE = 20
        const val MAX_BUBBLES_HIGH = 25

        const val SHOT_PER_BATCH = 3L
        const val SPAMMING_LIKE_DELAY = 200L
    }
}