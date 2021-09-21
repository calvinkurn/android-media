package com.tokopedia.play.view.viewcomponent

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Size
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play.R
import com.tokopedia.play.animation.multiplelikes.PlayLikeBubbleView
import com.tokopedia.play.view.storage.multiplelikes.MultipleLikesIconCacheStorage
import com.tokopedia.play.view.uimodel.PlayLikeBubbleUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeBubbleConfig
import com.tokopedia.play_common.view.getDrawableFromUrl
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */
class LikeBubbleViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val iconCacheStorage: MultipleLikesIconCacheStorage,
) : ViewComponent(container, idRes) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val bubbleLikeView = findViewById<PlayLikeBubbleView>(R.id.bubble_like_view)

    private val preferredIconSize = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl5)

    private val iconList: List<Drawable> = listOfNotNull(
        getResizedIconUnifyDrawable(IconUnify.STAR_FILLED),
        getResizedIconUnifyDrawable(IconUnify.HEART_FILLED),
        getResizedIconUnifyDrawable(IconUnify.THUMB_FILLED),
    )

    private val bgColorList: List<Int> = listOf(
        R.color.play_dms_multiplelike_red,
        R.color.play_dms_multiplelike_green,
        R.color.play_dms_multiplelike_purple,
    )

    private val defaultBubbleList: List<PlayLikeBubbleUiModel> = iconList.map {
        PlayLikeBubbleUiModel(it, bgColorList.map(this::getColor))
    }

    init {
        bubbleLikeView.setParentView(container)
    }

    fun preloadIcons(iconUrls: Set<String>) {
        scope.launch(Dispatchers.Default) {
            iconUrls.forEach { iconUrl ->
                val isCached = iconCacheStorage.getDrawable(iconUrl) != null
                if (!isCached) {
                    val drawableFromNetwork = try {
                        getDrawableFromUrl(iconUrl)
                    } catch (e: Throwable) { null }

                    if (drawableFromNetwork != null) {
                        iconCacheStorage.addCache(iconUrl, drawableFromNetwork)
                    }
                }
            }
        }
    }

    fun shot(
        amount: Int,
        reduceOpacity: Boolean,
        config: PlayLikeBubbleConfig,
    ) {
        bubbleLikeView.shot(
            likeAmount = amount,
            shotPerBatch = SHOT_PER_BATCH,
            delayInMs = SPAMMING_LIKE_DELAY,
            reduceOpacity = reduceOpacity,
            bubbleList = loadBubblesFromConfig(config)
        )
    }

    fun shotBurst(
        amount: Int,
        reduceOpacity: Boolean,
        config: PlayLikeBubbleConfig,
    ) {
        bubbleLikeView.shot(
            likeAmount = amount,
            shotPerBatch = SHOT_PER_BATCH,
            reduceOpacity = reduceOpacity,
            bubbleList = loadBubblesFromConfig(config)
        )
    }

    private fun loadBubblesFromConfig(config: PlayLikeBubbleConfig): List<PlayLikeBubbleUiModel> {
        val bubbleList = config.bubbleMap.mapNotNull { entry ->
            val drawable = iconCacheStorage.getDrawable(entry.key) ?: return@mapNotNull null
            PlayLikeBubbleUiModel(
                icon = drawable,
                colorList = entry.value,
            )
        }
        return if (bubbleList.isEmpty()) this.defaultBubbleList else bubbleList
    }

    private suspend fun getDrawableFromUrl(url: String): Drawable? {
        return rootView.context.getDrawableFromUrl(
            url,
            size = Size(preferredIconSize, preferredIconSize)
        )
    }

    private fun getResizedIconUnifyDrawable(iconId: Int): Drawable? {
        return getIconUnifyDrawable(
            rootView.context,
            iconId,
            getColor(com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        )?.resize(preferredIconSize, preferredIconSize)
    }

    /**
     * Helper to resize IconUnify drawable
     */
    private fun Drawable.resize(width: Int, height: Int): Drawable {
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        val bmpScaled = Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDrawable(resources, bmpScaled)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        bubbleLikeView.stop()
        job.cancelChildren()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        job.cancelChildren()
        bubbleLikeView.release()
    }

    private companion object {
        const val SHOT_PER_BATCH = 3
        const val SPAMMING_LIKE_DELAY = 200L
    }
}