package com.tokopedia.play.animation.spamlike

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.play.R
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */

class PlaySpamLikeAnimation(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {

    private val view: View = View.inflate(context, R.layout.layout_play_spam_like, this)

    /**
     * Custom
     */
    private var loveList = mutableListOf<Drawable?>()
    private var sizeList = mutableListOf<Pair<Int, Int>>()
    private var sizeMultiplyList = mutableListOf<Float>()
    private val dotColorList = mutableListOf<Int>()
    private var shot = 0
    private var dot: Drawable? = null

    /**
     * Config
     */
    private var maxShot = 30
    private var sizeType = PlaySpamLikeSize.MULTIPLY
    private val duration = 1500L
    private var isAdditionalShot: Boolean = false
    private var isBouncing: Boolean = false

    private var parentView: ViewGroup? = null
    private val job = SupervisorJob()

    private val imageList = mutableListOf<ImageView>()

    init {
        val type = context.obtainStyledAttributes(attributeSet, R.styleable.PlaySpamLikeAnimation)

        isBouncing = type.getBoolean(R.styleable.PlaySpamLikeAnimation_bouncing, false)
        isAdditionalShot = type.getBoolean(R.styleable.PlaySpamLikeAnimation_additionalShot, false)
        maxShot = type.getInteger(R.styleable.PlaySpamLikeAnimation_maxShot, 30)

        type.recycle()

        setDefaultLoveList()
        setDefaultSizeList()
        setDefaultDotList()
        setDefaultDotColorList()
    }

    private fun setDefaultLoveList() {
        loveList.add(ContextCompat.getDrawable(context, R.drawable.ic_play_shipping_voucher))
        loveList.add(ContextCompat.getDrawable(context, R.drawable.ic_play_special_voucher))
    }

    private fun setDefaultSizeList() {
        sizeList.add(Pair(50, 50))

        sizeMultiplyList.add(1.0f)
    }

    private fun setDefaultDotList() {
        dot = ContextCompat.getDrawable(context, R.drawable.shape_play_cart_dots)
    }

    private fun setDefaultDotColorList() {
        dotColorList.add(Color.BLUE)
        dotColorList.add(Color.RED)
        dotColorList.add(Color.GREEN)
        dotColorList.add(Color.CYAN)
    }

    /**
     * setParentView(parentView: ViewGroup)
     * should be provided before calling shot()
     */
    fun setParentView(parentView: ViewGroup) {
        this.parentView = parentView
    }

    fun setAdditionalShot(isAdditionalShot: Boolean) {
        this.isAdditionalShot = isAdditionalShot
    }

    fun setBouncing(isBouncing: Boolean) {
        this.isBouncing = isBouncing
    }

    fun setLoveList(loveList: List<Drawable?>) {
        this.loveList.clear()
        this.loveList.addAll(loveList)
    }

    fun setSizeList(sizeList: List<Pair<Int, Int>>) {
        sizeType = PlaySpamLikeSize.EXACT
        this.sizeList.clear()
        this.sizeList.addAll(sizeList)
    }

    fun setSizeMultiplyList(sizeMultiplyList: List<Float>) {
        sizeType = PlaySpamLikeSize.MULTIPLY
        this.sizeMultiplyList.clear()
        this.sizeMultiplyList.addAll(sizeMultiplyList)
    }

    fun setDot(dot: Drawable) {
        this.dot = dot
    }

    fun setDotColorList(dotColorList: List<Int>) {
        this.dotColorList.clear()
        this.dotColorList.addAll(dotColorList)
    }

    fun setMaxShot(maxShot: Int) {
        this.maxShot = maxShot
    }

    fun getMaxShot(): Int = maxShot

    /**
     * Step shot()
     * 1. Randomize config
     * 2. Create the Image
     * 3. Set Image Coordinate
     * 4. Add Image to Layout
     * 5. Start Animation
     * 6. Put Image to Queue
     * 7. Set Coroutine for Popping Image from Queue
     * 8. Check Whether Additional Shot is Required Or Not
     */
    fun shot() {
        if(loveList.isEmpty() || sizeMultiplyList.isEmpty() || sizeList.isEmpty() || parentView == null) return

        if(shot < maxShot) {
            val love = loveList[(0 until loveList.size).random()]
            val sizeMultiply = sizeMultiplyList[(0 until sizeMultiplyList.size).random()]
            val size = sizeList[(0 until sizeList.size).random()]

            love?.let {
                val dimension = when(sizeType) {
                    PlaySpamLikeSize.EXACT -> size
                    PlaySpamLikeSize.MULTIPLY -> getDimensionMultiply(love, sizeMultiply)
                }

                val image = prepareImage(love, dimension)

                parentView?.addView(image)
                startAnimate(image)
                imageList.add(image)

                setShot(INCREASE_SHOT)

                CoroutineScope(Dispatchers.IO + job).launch {
                    delay(duration)
                    removeImageFromView(image)
                }

                if(isAdditionalShot) {
                    if(dotColorList.isEmpty()) return

                    val showOrNot = (0..10).random() % 2 == 0
                    if(showOrNot) {
                        shotAdditional()
                    }
                }
            }
        }
    }

    private fun shotAdditional() {
        CoroutineScope(Dispatchers.IO + job).launch {
            delay((50..200).random().toLong())

            withContext(Dispatchers.Main) {
                dot?.let {
                    DrawableCompat.setTint(it, dotColorList[(0 until dotColorList.size).random()])

                    val image = prepareImage(it, Pair(20, 20))

                    parentView?.addView(image)
                    startAnimate(image)
                    imageList.add(image)

                    CoroutineScope(Dispatchers.IO + job).launch {
                        delay(duration)
                        removeImageFromView(image, false)
                    }
                }
            }
        }
    }

    private fun getImageCoordinate(): Pair<Float, Float> {
        val fixCoordinate = IntArray(2)
        view.getLocationInWindow(fixCoordinate)

        val x = (fixCoordinate[0]..fixCoordinate[0] + view.measuredWidth).random().toFloat()
        val y = fixCoordinate[1].toFloat() + view.measuredHeight

        return Pair(x, y)
    }

    private fun prepareImage(drawable: Drawable, size: Pair<Int, Int>): ImageView {
        val image = ImageView(context)
        image.setImageBitmap(Bitmap.createScaledBitmap(drawable.toBitmap(), size.first, size.second, true))

        val coordinate = getImageCoordinate()
        image.x = coordinate.first
        image.y = coordinate.second
        image.id = View.generateViewId()

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
        val start = image.y
        val end = image.y - 500

        val threshold = (end - ((end - start) / 2))
        var alpha = 1.0f

        val xStart = image.x - 20
        val xEnd = image.x + 20
        var xCurrent = image.x

        var xMove: PlaySpamLikeMove = if((0..1).random() % 2 == 0) PlaySpamLikeMove.Right else PlaySpamLikeMove.Left

        val move = ValueAnimator.ofFloat(start, end)
        move.addUpdateListener {
            /**
             * Change Y coordinate
             */
            val value = it.animatedValue as Float
            image.y = value


            /**
             * Change alpha when current Y coordinate below the threshold
             */
            if(value <= threshold) {
                alpha -= 0.05f
                image.alpha = alpha
            }

            /**
             * Change X coordinate
             */
            if(isBouncing) {
                when(xMove) {
                    is PlaySpamLikeMove.Center -> {
                        image.x = xCurrent
                        val xMoveCenter = (xMove as PlaySpamLikeMove.Center)
                        if(xMoveCenter.counter+1 == xMove.threshold) {
                            xMove = xMoveCenter.next
                        }
                        else xMoveCenter.counter++
                    }
                    PlaySpamLikeMove.Right -> {
                        xCurrent += 5
                        image.x = xCurrent
                        if(xCurrent > xEnd) {
                            xMove = PlaySpamLikeMove.Center(PlaySpamLikeMove.Left, 0)
                        }
                    }
                    PlaySpamLikeMove.Left -> {
                        xCurrent -= 5
                        image.x = xCurrent
                        if(xCurrent < xStart) {
                            xMove = PlaySpamLikeMove.Center(PlaySpamLikeMove.Right, 0)
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

    fun clear() {
        job.cancel()
        imageList.forEach {
            parentView?.removeView(it)
        }
        imageList.clear()
    }

    companion object {
        private const val INCREASE_SHOT = 1
        private const val DECREASE_SHOT = -1
    }
}