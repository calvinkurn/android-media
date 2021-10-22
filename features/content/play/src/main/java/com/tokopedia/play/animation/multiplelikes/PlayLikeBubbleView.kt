package com.tokopedia.play.animation.multiplelikes

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.PlayLikeBubbleUiModel
import com.tokopedia.play_common.view.RoundedImageView
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */
class PlayLikeBubbleView(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {

    private val job = SupervisorJob()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)
    
    private val view: View = View.inflate(context, R.layout.layout_play_spam_like, this)

    /**
     * Custom
     */
    private val defaultSize = resources.getDimensionPixelSize(R.dimen.play_like_bubble_original_size)
    private val sizeList = listOf(defaultSize to defaultSize)
    private val sizeMultiplyList = listOf(0.3f)
    private var shot = 0

    /**
     * Config
     */
    private var maxShot = 30
    private var sizeType = PlayLikeBubbleSize.EXACT
    private val duration = ANIMATION_DURATION_IN_MS
    private var isBouncing: Boolean = false
    private var blurOpacity: Float = 0.5F

    private var parentView: ViewGroup? = null

    private val imageList = mutableListOf<ImageView>()

    init {
        val type = context.obtainStyledAttributes(attributeSet, R.styleable.PlayLikeBubbleView)

        isBouncing = type.getBoolean(R.styleable.PlayLikeBubbleView_bouncing, false)
        maxShot = type.getInteger(R.styleable.PlayLikeBubbleView_maxShot, 30)

        type.recycle()
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
    ) {
        scope.launch {
            for(i in 1..likeAmount) {
                if (delayInMs > 0) delay(delayInMs)
                for(j in 1..shotPerBatch) {
                    delay(DEFAULT_DELAY)
                    withContext(Dispatchers.Main) {
                        shotInternal(reduceOpacity, bubbleList)
                    }
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
    private fun shotInternal(reduceOpacity: Boolean, bubbleList: List<PlayLikeBubbleUiModel>) {
        if(bubbleList.isEmpty() || sizeMultiplyList.isEmpty() || sizeList.isEmpty() || parentView == null) return

        if(shot < maxShot) {
            val chosenBubble = bubbleList.random()
            val icon = chosenBubble.icon
            val sizeMultiply = sizeMultiplyList.random()
            val size = sizeList.random()

            val dimension = when(sizeType) {
                PlayLikeBubbleSize.EXACT -> size
                PlayLikeBubbleSize.MULTIPLY -> getDimensionMultiply(icon, sizeMultiply)
            }

            val image = prepareImage(icon, chosenBubble.colorList, dimension, reduceOpacity)

            parentView?.addView(image)
            startAnimate(image)
            imageList.add(image)

            setShot(INCREASE_SHOT)

            scope.launch {
                delay(duration)
                removeImageFromView(image)
            }
        }
    }

    private fun getImageCoordinate(): Pair<Float, Float> {
        val fixCoordinate = IntArray(2)
        view.getLocationInWindow(fixCoordinate)

        val x = (fixCoordinate[0]..(fixCoordinate[0] + UPPER_LIMIT_RANDOM_X_POSITION)).random().toFloat()
        val y = fixCoordinate[1].toFloat() + view.measuredHeight

        return Pair(x, y)
    }

    private fun prepareImage(
        drawable: Drawable,
        possibleColors: List<Int>,
        size: Pair<Int, Int>,
        reduceOpacity: Boolean
    ): ImageView {
        val image = RoundedImageView(context).apply {
            setCornerRadius(500f)
        }
        image.setImageBitmap(Bitmap.createScaledBitmap(
            drawable.toBitmap(),
            size.first,
            size.second,
            true)
        )

        val coordinate = getImageCoordinate()
        image.x = coordinate.first
        image.y = coordinate.second
        image.id = View.generateViewId()

        if(reduceOpacity) image.alpha = blurOpacity

        image.setBackgroundColor(possibleColors.random())

        val padding = size.first / 2
        image.setPadding(padding, padding, padding, padding)

        return image
    }

    private suspend fun removeImageFromView(image: ImageView, isDecreaseShot: Boolean = true) {
        withContext(Dispatchers.Main) {
            synchronized(imageList) {
                parentView?.removeView(image)
                imageList.remove(image)
                if(isDecreaseShot) setShot(DECREASE_SHOT)
            }
        }
    }

    /**
     * Step startAnimate()
     * 1. Setup Y Coordinate Start & End
     * 2. Setup Y Threshold for Fade Away
     * 3. Setup X Boundary for Bouncing Effect
     * 4. Setup Initial X Direction
     * 5. Animate
     */
    private fun startAnimate(image: ImageView) {
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
        move.duration = duration
        move.start()
    }

    private fun getDimensionMultiply(res: Drawable, sizeMultiply: Float): Pair<Int, Int> =
        Pair((res.intrinsicWidth * sizeMultiply).toInt(), (res.intrinsicHeight * sizeMultiply).toInt())

    private fun setShot(type: Int) {
        synchronized(shot) {
            shot += type
        }
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
        imageList.forEach {
            parentView?.removeView(it)
        }
        imageList.clear()
    }

    private companion object {
        const val INCREASE_SHOT = 1
        const val DECREASE_SHOT = -1

        const val DEFAULT_DELAY = 50L
        const val SHOT_DISTANCE = 750
        const val LOWER_LIMIT_BOUNCING_DISTANCE = 10
        const val UPPER_LIMIT_BOUNCING_DISTANCE = 40
        const val LOWER_BOUNCING_MULTIPLIER_X = 3
        const val UPPER_BOUNCING_MULTIPLIER_X = 8
        const val UPPER_LIMIT_RANDOM_X_POSITION = 50
        const val FADE_OUT_MULTIPLIER = 0.05F
        const val SCALING_UP_MULTIPLIER = 0.05F

        private const val ANIMATION_DURATION_IN_MS = 2500L
    }
}