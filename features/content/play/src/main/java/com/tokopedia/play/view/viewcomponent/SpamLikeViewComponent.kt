package com.tokopedia.play.view.viewcomponent

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
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
import com.tokopedia.play.animation.spamlike.PlaySpamLikeView
import com.tokopedia.play.view.uimodel.PlayLikeBubbleUiModel
import com.tokopedia.play.view.uimodel.recom.multiplelikes.PlayMultipleLikesConfig
import com.tokopedia.play_common.view.getDrawableFromUrl
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */
class SpamLikeViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val spamLike = findViewById<PlaySpamLikeView>(R.id.spam_like_animation)

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

    private var bubbleList: List<PlayLikeBubbleUiModel> = iconList.map {
        PlayLikeBubbleUiModel(it, bgColorList.map(this::getColor))
    }

    init {
        spamLike.setParentView(container)
    }

    fun shot(
        amount: Int = 1,
        reduceOpacity: Boolean = false
    ) {
        spamLike.shot(
            likeAmount = amount,
            shotPerBatch = SHOT_PER_BATCH,
            delayInMs = SPAMMING_LIKE_DELAY,
            reduceOpacity
        )
    }

    fun shotBurst(
        amount: Int = 1,
        isOpaque: Boolean = false
    ) {
        spamLike.shot(
            likeAmount = amount,
            shotPerBatch = SHOT_PER_BATCH,
            isOpaque = isOpaque
        )
    }

    fun setNewBubbleConfig(config: PlayMultipleLikesConfig) {
        scope.launch {
            val newBubbleList = loadNewConfig(config.bubbleConfig)
            if (newBubbleList.isNotEmpty()) bubbleList = newBubbleList
            spamLike.setBubbleList(bubbleList)
        }
    }

    private suspend fun loadNewConfig(bubbles: Map<String, List<String>>): List<PlayLikeBubbleUiModel> {
        return bubbles.mapNotNull { entry ->
            val iconUrl = entry.key
            val drawable = try { getDrawableFromUrl(iconUrl) } catch (e: Throwable) { null }
            if (drawable == null) return@mapNotNull null
            else {
                val parsedColors = loadNewColorConfig(entry.value)
                return@mapNotNull if (parsedColors.isEmpty()) null
                else PlayLikeBubbleUiModel(drawable, parsedColors)
            }
        }
    }

    private fun loadNewColorConfig(bgColors: List<String>): List<Int> {
        return bgColors.mapNotNull {
            try {
                Color.parseColor(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
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
        spamLike.clear(true)
        job.cancelChildren()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        job.cancelChildren()
        spamLike.clear(false)
    }

    private companion object {
        const val SHOT_PER_BATCH = 3
        const val SPAMMING_LIKE_DELAY = 200L
    }
}