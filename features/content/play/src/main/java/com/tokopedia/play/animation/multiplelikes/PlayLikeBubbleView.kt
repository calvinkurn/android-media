package com.tokopedia.play.animation.multiplelikes

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.play.R
import com.tokopedia.play.util.animation.DefaultAnimatorListener
import com.tokopedia.play.view.uimodel.PlayLikeBubbleUiModel
import com.tokopedia.play_common.view.RoundedImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */
class PlayLikeBubbleView(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {

    private val job = SupervisorJob()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    private val view: View = View.inflate(context, R.layout.layout_play_spam_like, this)

    private val prepareImageFlow = MutableSharedFlow<Triple<
        List<PlayLikeBubbleUiModel>,
        Boolean,
        Boolean>>(extraBufferCapacity = 500)
    private val imageViewHolderList = ConcurrentLinkedQueue<ImageViewHolder>()
    private val animationList = ConcurrentLinkedQueue<Animator>()

    private val prepareJob = SupervisorJob()

    /**
     * Config
     */
    private val duration = ANIMATION_DURATION_IN_MS
    private var isBouncing: Boolean = false
    private var blurOpacity: Float = 0.5F

    private var parentView: ViewGroup? = null

    init {
        val type = context.obtainStyledAttributes(attributeSet, R.styleable.PlayLikeBubbleView)

        isBouncing = type.getBoolean(R.styleable.PlayLikeBubbleView_bouncing, false)

        type.recycle()

        scope.launch(prepareJob) {
            prepareImageFlow.collect { (bubbleList, reduceOpacity, forceShow) ->
                if (
                    !forceShow &&
                    imageViewHolderList.filter { it.isOccupied }.size > MAX_BUBBLE
                ) return@collect
                prepareImage(bubbleList, reduceOpacity)
            }
        }
    }

    /**
     * setParentView(parentView: ViewGroup)
     * should be provided before calling shot()
     */
    fun setParentView(parentView: ViewGroup) {
        this.parentView = parentView
    }

    fun shot(
        likeAmount: Int,
        shotPerBatch: Int,
        delayInMs: Long = 0L,
        reduceOpacity: Boolean = false,
        bubbleList: List<PlayLikeBubbleUiModel> = emptyList(),
        forceShow: Boolean = false,
    ) {
        scope.launch(Dispatchers.Default) {
            for(i in 1..likeAmount) {
                if (delayInMs > 0) delay(delayInMs)
                for(j in 1..shotPerBatch) {
                    delay(DEFAULT_DELAY)
                    shotInternal(reduceOpacity, bubbleList, forceShow)
                }
            }
        }
    }

    /**
     * Step shotInternal()
     * 1. Randomize config
     * 2. Create the Image
     * 3. Set Image Coordinate
     * 4. Add Image to Layout
     * 5. Start Animation
     * 6. Put Image to Queue
     * 7. Set Coroutine for Popping Image from Queue
     * 8. Check Whether Additional Shot is Required Or Not
     */
    private suspend fun shotInternal(
        reduceOpacity: Boolean,
        bubbleList: List<PlayLikeBubbleUiModel>,
        forceShow: Boolean,
    ) = withContext(Dispatchers.Default) {
        if(bubbleList.isEmpty() || parentView == null
        ) return@withContext

        prepareImageFlow.tryEmit(Triple(bubbleList, reduceOpacity, forceShow))
    }

    private fun getImageCoordinate(): Pair<Float, Float> {
        val fixCoordinate = IntArray(2)
        view.getLocationInWindow(fixCoordinate)

        val x = (fixCoordinate[0]..(fixCoordinate[0] + UPPER_LIMIT_RANDOM_X_POSITION)).random().toFloat()
        val y = fixCoordinate[1].toFloat() + view.measuredHeight

        return Pair(x, y)
    }

    private fun prepareImage(
        bubbleList: List<PlayLikeBubbleUiModel>,
        reduceOpacity: Boolean,
    ) {
        val chosenBubble = bubbleList.random()
        val icon = chosenBubble.icon

        val unoccupiedImageHolder = imageViewHolderList.firstOrNull { !it.isOccupied }
        val imageHolder = if (unoccupiedImageHolder == null) {
            val image = RoundedImageView(context).apply {
                setCornerRadius(500f)
            }
            image.id = View.generateViewId()
            val newHolder = ImageViewHolder(image, true, System.currentTimeMillis())
            imageViewHolderList.add(newHolder)
            newHolder
        } else unoccupiedImageHolder
        imageHolder.isOccupied = true

        val image = imageHolder.imageView

        val coordinate = getImageCoordinate()
        image.x = coordinate.first
        image.y = coordinate.second

        if (image.parent == null) parentView?.addView(image)
        startAnimate(imageHolder)

        image.setImageDrawable(icon)

        image.alpha = if(reduceOpacity) blurOpacity else 1f

        image.setBackgroundColor(chosenBubble.colorList.random())

        val padding = icon.intrinsicWidth / 2
        image.setPadding(padding, padding, padding, padding)
    }

    private fun removeImageFromView(imageHolder: ImageViewHolder) {
        imageHolder.imageView.alpha = 0f
        imageHolder.isOccupied = false
    }

    /**
     * Step startAnimate()
     * 1. Setup Y Coordinate Start & End
     * 2. Setup Y Threshold for Fade Away
     * 3. Setup X Boundary for Bouncing Effect
     * 4. Setup Initial X Direction
     * 5. Animate
     */
    private fun startAnimate(imageHolder: ImageViewHolder) {
        val image = imageHolder.imageView
        image.alpha = 1f
        image.scaleX = 1f
        image.scaleY = 1f

        /**
         * Setup Distance
         */
        val start = image.y
        val end = image.y - SHOT_DISTANCE

        /**
         * Setup fade out constraint
         */
        val threshold = (end - ((end - start) / 2))
        var alpha = image.alpha

        /**
         * Setup bouncing constraint
         */
        val bouncingLimit = (LOWER_LIMIT_BOUNCING_DISTANCE..UPPER_LIMIT_BOUNCING_DISTANCE).random()
        val bouncingMultiplier = (LOWER_BOUNCING_MULTIPLIER_X..UPPER_BOUNCING_MULTIPLIER_X).random()

        val xStart = image.x - bouncingLimit
        val xEnd = image.x + bouncingLimit
        var xCurrent = image.x
        var xMove: PlayLikeBubbleMove = if((0..1).random() % 2 == 0) PlayLikeBubbleMove.Right else PlayLikeBubbleMove.Left

        /**
         * Setup scaling constraint
         */
        image.scaleX = 0F
        image.scaleY = 0F

        val move = ValueAnimator.ofFloat(start, end)
        move.addUpdateListener {
            /**
             * Change Y coordinate
             */
            val value = it.animatedValue as Float
            image.y = value

            /**
             * Scaling up when appear first time
             */
            if(image.scaleX < 1F && value > threshold) {
                image.scaleX = image.scaleX + SCALING_UP_MULTIPLIER
                image.scaleY = image.scaleY + SCALING_UP_MULTIPLIER
            }

            /**
             * Change alpha when current Y coordinate below the threshold
             */
            if(value <= threshold) {
                alpha -= FADE_OUT_MULTIPLIER
                image.alpha = alpha

                if (image.scaleX > 0f) {
                    image.scaleX = image.scaleX - SCALING_UP_MULTIPLIER
                    image.scaleY = image.scaleY - SCALING_UP_MULTIPLIER
                }
            }

            /**
             * Change X coordinate
             */
            if(isBouncing) {
                when(xMove) {
                    is PlayLikeBubbleMove.Center -> {
                        image.x = xCurrent
                        val xMoveCenter = (xMove as PlayLikeBubbleMove.Center)
                        if(xMoveCenter.counter+1 == xMove.threshold) {
                            xMove = xMoveCenter.next
                        }
                        else xMoveCenter.counter++
                    }
                    PlayLikeBubbleMove.Right -> {
                        xCurrent += bouncingMultiplier
                        image.x = xCurrent
                        if(xCurrent > xEnd) {
                            xMove = PlayLikeBubbleMove.Center(PlayLikeBubbleMove.Left, 0)
                        }
                    }
                    PlayLikeBubbleMove.Left -> {
                        xCurrent -= bouncingMultiplier
                        image.x = xCurrent
                        if(xCurrent < xStart) {
                            xMove = PlayLikeBubbleMove.Center(PlayLikeBubbleMove.Right, 0)
                        }
                    }
                }
            }
        }

        move.interpolator = LinearInterpolator()
        move.addListener(object : DefaultAnimatorListener() {
            override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
                removeImageFromView(imageHolder)
                move.removeAllUpdateListeners()
                move.removeAllListeners()
                animationList.remove(move)
            }
        })
        move.duration = duration
        animationList.add(move)
        move.start()
    }

    fun stop() {
        job.cancelChildren()
        cleanUp()
    }

    fun release() {
        job.cancel()
        cleanUp()
    }

    private fun cleanUp() {
        animationList.forEach { it.cancel() }
        imageViewHolderList.forEach {
            if (it.isOccupied ||
                abs(it.time - System.currentTimeMillis()) < TimeUnit.SECONDS.toMillis(5)
            ) return@forEach

            parentView?.removeView(it.imageView)
            imageViewHolderList.remove(it)
        }
    }

    private data class ImageViewHolder(
        val imageView: ImageView,
        var isOccupied: Boolean,
        val time: Long,
    )

    private companion object {
        const val DEFAULT_DELAY = 50L
        const val SHOT_DISTANCE = 750
        const val LOWER_LIMIT_BOUNCING_DISTANCE = 10
        const val UPPER_LIMIT_BOUNCING_DISTANCE = 40
        const val LOWER_BOUNCING_MULTIPLIER_X = 3
        const val UPPER_BOUNCING_MULTIPLIER_X = 8
        const val UPPER_LIMIT_RANDOM_X_POSITION = 50
        const val FADE_OUT_MULTIPLIER = 0.05F
        const val SCALING_UP_MULTIPLIER = 0.05F

        const val MAX_BUBBLE = 30

        private const val ANIMATION_DURATION_IN_MS = 2500L
    }
}