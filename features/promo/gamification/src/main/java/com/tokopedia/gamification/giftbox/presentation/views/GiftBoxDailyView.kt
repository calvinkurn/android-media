package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.Companion.NEGATIVE_DELAY_FOR_LARGE_REWARD_ANIM
import java.util.concurrent.atomic.AtomicInteger

open class GiftBoxDailyView : FrameLayout {

    lateinit var imageGiftBoxLid: AppCompatImageView
    lateinit var fmGiftBox: FrameLayout
    lateinit var imageFlatGlow: AppCompatImageView
    lateinit var imageBoxFront: AppCompatImageView

    var boxCallback: BoxCallback? = null
    var initialBounceAnimatorSet: AnimatorSet? = null
    var giftBoxState: GiftBoxState = GiftBoxState.CLOSED

    val TOTAL_ASYNC_IMAGES = 2
    var imagesLoaded = AtomicInteger(0)
    val GIFT_BOX_START_DELAY = 300L

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        setup(attrs)
    }

    constructor(context: Context) : super(context) {
        setup(null)
    }

    open fun getLayout() = R.layout.view_gift_box_daily

    private fun setup(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    open fun initViews() {

        fmGiftBox = findViewById(R.id.fm_gift_box)
        imageGiftBoxLid = findViewById(R.id.image_gift_box_lid)
        imageFlatGlow = findViewById(R.id.image_flat_glow)
        imageBoxFront = findViewById(R.id.image_box_front)

        imageFlatGlow.alpha = 0f

        setClicks()
    }

    fun setClicks() {

    }

    open fun handleTapOnGiftBox() {
        //todo Rahul make api call first
        if (giftBoxState == GiftBoxState.CLOSED) {
//            boxCallback?.onBoxScaleDownAnimationStart()
            giftBoxState = GiftBoxState.OPEN
            startBoxOpenAnimation()
        }
    }


    fun startBoxOpenAnimation() {
        initialBounceAnimatorSet?.end()

        fmGiftBox.clearAnimation()
        val pairAnimation = openGiftBox(fmGiftBox)
        val animatorSet = pairAnimation.first
        animatorSet.addListener(onEnd = {
            boxCallback?.onBoxOpened()
        })
        boxCallback?.onBoxOpenAnimationStart(pairAnimation.second + GIFT_BOX_START_DELAY - NEGATIVE_DELAY_FOR_LARGE_REWARD_ANIM)
        animatorSet.start()

    }

    fun loadFiles(imageCallback: ((isLoaded: Boolean) -> Unit)) {
        Glide.with(this)
                .load(R.drawable.gf_ic_lid_frame_0)
                .dontAnimate()
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        imageCallback.invoke(false)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val count = imagesLoaded.incrementAndGet()
                        if (count == TOTAL_ASYNC_IMAGES) {
                            imageCallback.invoke(true)
                        }
                        return false
                    }
                })
                .into(imageGiftBoxLid)

        Glide.with(this)
                .load(R.drawable.gf_ic_gift_box)
                .dontAnimate()
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        imageCallback.invoke(false)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val count = imagesLoaded.incrementAndGet()
                        if (count == TOTAL_ASYNC_IMAGES) {
                            imageCallback.invoke(true)
                        }
                        return false
                    }
                })
                .into(imageBoxFront)

    }

    open fun startInitialAnimation(): Animator? {

        val bounceAnimation = bouncingAnimation(fmGiftBox)
        val idleAnimation = idleAnimation(fmGiftBox)

        if (initialBounceAnimatorSet == null) {
            initialBounceAnimatorSet = AnimatorSet()
        }
        initialBounceAnimatorSet?.addListener(onEnd = {
            if (giftBoxState == GiftBoxState.CLOSED) {
                initialBounceAnimatorSet?.startDelay = 700L
                initialBounceAnimatorSet?.start()
            }
        })
        initialBounceAnimatorSet?.playSequentially(bounceAnimation, idleAnimation)
        initialBounceAnimatorSet?.start()
        return initialBounceAnimatorSet
    }


    fun bouncingAnimation(view: View): Animator {
        val duration = 300L
        view.pivotY = view.height.toFloat()
        val scaleYAnim = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.9f)
        val bounceAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleYAnim)
        bounceAnim.repeatCount = 3
        bounceAnim.repeatMode = ValueAnimator.REVERSE
        bounceAnim.duration = duration
        return bounceAnim
    }

    fun idleAnimation(view: View): Animator {
        val duration = 800L
        view.pivotY = view.height.toFloat()
        val scaleXAnim = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        val idleAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXAnim)
        idleAnim.duration = duration
        return idleAnim
    }

    fun adjustGlowImagePosition() {
        val array = IntArray(2)
        imageBoxFront.getLocationInWindow(array)
        imageFlatGlow.y = array[1].toFloat() - imageFlatGlow.height - getStatusBarHeight(context)
    }

    fun stageGlowAnimation(): Animator {
        //show stage light
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageFlatGlow, alphaProp)
        alphaAnim.duration = 1000L
        alphaAnim.repeatCount = ValueAnimator.INFINITE
        alphaAnim.repeatMode = ValueAnimator.REVERSE

        return alphaAnim

    }

    fun openGiftBox(view: View): Pair<AnimatorSet, Long> {
        val duration = 500L

        view.pivotY = view.height.toFloat()

        //down
        val scaleYAnimDown = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.5f)
        val bounceAnimDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleYAnimDown)
        bounceAnimDown.startDelay = GIFT_BOX_START_DELAY
        bounceAnimDown.duration = duration
        bounceAnimDown.interpolator = CubicBezierInterpolator(.645, 0.045, .355, 1.0)

//        post { finalLoadGif(lidView) }
        loadLidFrames()

        //up
        val scaleYAnimUp = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
        val bounceAnimUp: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleYAnimUp)
        bounceAnimUp.interpolator = CubicBezierInterpolator(.56, 1.59, .49, 1.02)
        bounceAnimUp.duration = duration

        //translate
        val transYProp = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -dpToPx(10f), 0f)
        val transYAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, transYProp)
        transYAnim.duration = duration


        val animatorSet = AnimatorSet()
        animatorSet.playTogether(bounceAnimUp, transYAnim)
        animatorSet.playSequentially(bounceAnimDown, bounceAnimUp)
        val totalDuration = bounceAnimDown.duration + bounceAnimUp.duration
        return Pair(animatorSet, totalDuration)
    }

    fun boxScaleDownAnimation(view: View): DurationAnimation {
        val duration = 1000L
        val startDelay = 300L
        view.pivotY = view.height.toFloat()
        val scaleYAnim = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.3f, 1.2f, 1f)
        val bounceAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleYAnim)
        bounceAnim.startDelay = startDelay
        bounceAnim.duration = duration
        return DurationAnimation(duration, bounceAnim, startDelay)
    }

    fun boxTranslateAnimation(view: View, delay: Long, startDelay: Long): Animator {
        val duration = 600L
        val transYProp = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -50f, 0f)
        val transYAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, transYProp)
        transYAnim.interpolator = AccelerateInterpolator()
        transYAnim.startDelay = delay + 500L
        transYAnim.duration = duration
        return transYAnim
    }

    fun boxScaleUpAnimation(view: View): Animator {
        val duration = 1200L
        view.pivotY = view.height.toFloat()
        val scaleYAnim = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.3f, 1f)
        val scaleXAnim = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.1f, 1f)
        val bounceAnim: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleYAnim, scaleXAnim)
        bounceAnim.duration = duration
        return bounceAnim
    }

    fun loadGifFirstFrame(imageView: AppCompatImageView) {
        Glide.with(this)
                .load(R.drawable.gf_ic_lid_frame_0)
                .into(imageView)
    }

    fun loadLidFrames() {
        val drawableArray = arrayOf(
                R.drawable.gf_ic_lid_frame_0,
                R.drawable.gf_ic_lid_frame_1,
                R.drawable.gf_ic_lid_frame_2,
                R.drawable.gf_ic_lid_frame_3,
                R.drawable.gf_ic_lid_frame_4,
                R.drawable.gf_ic_lid_frame_5,
                R.drawable.gf_ic_lid_frame_6,
                R.drawable.gf_ic_lid_frame_7
        )
        val valueAnimator = ValueAnimator.ofInt(drawableArray.size - 1)
        valueAnimator.addUpdateListener {
            imageGiftBoxLid.setImageResource(drawableArray[it.animatedValue as Int])
        }
        valueAnimator.duration = 500L
        valueAnimator.startDelay = 500L
        valueAnimator.start()
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = (24 * context.resources.displayMetrics.density + 0.5f).toInt()
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun dpToPx(dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    enum class GiftBoxState {
        CLOSED, OPEN
    }

    interface BoxCallback {
        fun onBoxScaleDownAnimationStart()
        fun onBoxOpenAnimationStart(startDelay: Long)
        fun onBoxOpened()
    }

    interface BoxRewardCallback {
        fun showPoints()
        fun showPointsWithCoupons()
        fun showCoupons()
    }
}

class DurationAnimation(val duration: Long, val animator: Animator, val startDelay: Long)