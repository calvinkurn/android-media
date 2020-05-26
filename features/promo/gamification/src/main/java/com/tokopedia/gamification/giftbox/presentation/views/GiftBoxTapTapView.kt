package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener

class GiftBoxTapTapView : GiftBoxDailyView {

    lateinit var imageBoxWhite: AppCompatImageView
    lateinit var imageBoxGlow: AppCompatImageView
    lateinit var confettiView: LottieAnimationView

    var glowingAnimator: Animator? = null
    var isGiftTapAble = false
    var isBoxAlreadyOpened = false
    var tapCount = 0
    var targetTapCount = 0
    var boxRewardCallback: BoxRewardCallback? = null
    var isTimeOut = false

    var disableConfettiAnimation = false

    override var TOTAL_ASYNC_IMAGES = 5

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    constructor(context: Context) : super(context)

    override fun getLayout(): Int = com.tokopedia.gamification.R.layout.view_gift_box_tap_tap

    override fun initViews() {

        confettiView = findViewById(R.id.lottie_particles)
        imageBoxWhite = findViewById(R.id.image_box_white)
        imageBoxGlow = findViewById(R.id.image_box_glow)
        super.initViews()
    }

    override fun handleTapOnGiftBox() {
        if (isTimeOut) {
            //Do nothing
        } else if (isGiftTapAble) {
            if (isBoxAlreadyOpened) {
                secondTimeBoxOpenAnimation()
            } else {
                //first time animations
                isGiftTapAble = false
                isBoxAlreadyOpened = true
                firstTimeBoxOpenAnimation()
                targetTapCount = getRandomNumber()
            }
        }
    }

    fun firstTimeBoxOpenAnimation() {
        boxCallback?.onBoxScaleDownAnimationStart()
        giftBoxState = GiftBoxState.OPEN
        startBoxOpenAnimation()
    }

    //-----------NEW CODE STARTS===============
    fun showRewardAnimation(){
        showRewardsForNextTap()
        boxBounceAnimation().start()
        //todo Rahul set isGiftTapAble = true here as well, when animation ends
        //todo Rahul enable disableConfettiAnimation after animation is ended before enabling tap
    }
    fun showConfettiAnimation(){
        showParticleAnimation()
        val bounceAnim = boxBounceAnimation()
        bounceAnim.addListener(onEnd = { isGiftTapAble = true })
        bounceAnim.start()
    }

    //-----------NEW CODE ENDS===============


    fun secondTimeBoxOpenAnimation() {
        //show particles or show prize based on random number
        //todo Rahul later hasRewardFromBackend
        //todo Reset the tap count next rewards are dissappeared
        //todo need to handle rapid taps
        val hasRewardFromBackend = true
        isGiftTapAble = false

        if (tapCount == targetTapCount && hasRewardFromBackend) {
            //show reward
            resetTapCount()
            showRewardsForNextTap()
            boxBounceAnimation().start()
        } else {
            // show confetti
            showParticleAnimation()
            val bounceAnim = boxBounceAnimation()
            bounceAnim.addListener(onEnd = { isGiftTapAble = true })
            bounceAnim.start()
        }
        incrementTapCount()

    }

    fun boxBounceAnimation(): Animator {
        fmGiftBox.pivotY = fmGiftBox.height.toFloat()
        val duration = 500L

        val scaleYAnimDown = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.9f, 1f)
        val bounceAnimDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(fmGiftBox, scaleYAnimDown)
        bounceAnimDown.duration = duration
        bounceAnimDown.interpolator = CubicBezierInterpolator(.645, 0.045, .355, 1.0)
        return bounceAnimDown
    }

    fun showRewardsForNextTap():Animator {
        //todo Rahul take care of these !!
        if(true){
            return boxRewardCallback?.showPointsWithCoupons()!!
        }
        val rand = (0..2).random()
        when (rand) {
            0 -> {
                //showPoints
                return boxRewardCallback?.showPoints()!!
            }
            1 -> {
                //showPointsWithCoupons
                return boxRewardCallback?.showPointsWithCoupons()!!
            }
            else -> {
                //showCoupons
                return boxRewardCallback?.showCoupons()!!
            }
        }
    }

    fun incrementTapCount() {
        ++tapCount
    }

    fun resetTapCount() {
        tapCount = 0
    }

    fun getRandomNumber(): Int {
        return (2..5).random()
    }

    fun showParticleAnimation() {
        confettiView.playAnimation()
    }

    override fun startInitialAnimation(): Animator? {
        glowingAnimator = glowingAnimation()
        return glowingAnimator
    }

    fun glowingAnimation(): Animator {
        val duration = 500L
        val startDelay = 1000L
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageBoxGlow, alphaProp)
        alphaAnim.repeatCount = ValueAnimator.INFINITE
        alphaAnim.repeatMode = ValueAnimator.REVERSE
        alphaAnim.startDelay = startDelay
        alphaAnim.duration = duration
        return alphaAnim
    }

    fun fadeOutGlowingAndFadeInGiftBoxAnimation(): Animator {
        val duration = 500L
        val fadeOutProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)

        val imageBoxGlowAlphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageBoxGlow, fadeOutProp)
        val imageBoxWhiteAlphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageBoxWhite, fadeOutProp)

        val fadeInProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val fadeInAnim = ObjectAnimator.ofPropertyValuesHolder(fmGiftBox, fadeInProp)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(imageBoxGlowAlphaAnim, imageBoxWhiteAlphaAnim, fadeInAnim)

        animatorSet.duration = duration
        return animatorSet
    }

    fun loadFilesForTapTap(@TokenUserState state: String,
                           glowImageUrl: String?,
                           glowImageShadowUrl: String?,
                           imageFrontUrl: String,
                           bgUrl: String,
                           imageCallback: ((isLoaded: Boolean) -> Unit)) {

        if (glowImageUrl != null) {
            Glide.with(this)
                    .load(glowImageUrl)
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
                    .into(imageBoxWhite)
        } else {
            TOTAL_ASYNC_IMAGES -= 1
        }

        if(glowImageShadowUrl!=null) {
            Glide.with(this)
                    .load(glowImageShadowUrl)
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
                    .into(imageBoxGlow)
        }else{
            TOTAL_ASYNC_IMAGES -= 1
        }

//        var drawableRedForLid = com.tokopedia.gamification.R.drawable.gf_ic_lid_frame_7
//        if (state == TokenUserState.ACTIVE) {
            var drawableRedForLid = com.tokopedia.gamification.R.drawable.gf_ic_lid_frame_0
//        }

        Glide.with(this)
                .load(drawableRedForLid)
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
                .load(bgUrl)
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
                .into(imageBg)

        Glide.with(this)
                .load(imageFrontUrl)
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


}