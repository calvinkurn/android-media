package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.LidImagesDownloader
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
    var isTimeOut = false

    var disableConfettiAnimation = false
    val lidImagesDownloader = LidImagesDownloader()
    private val lidImages = arrayListOf<Bitmap>()

    override var TOTAL_ASYNC_IMAGES = 5
    override var GIFT_BOX_START_DELAY = 0L

    companion object {
        const val REWARD_START_DELAY = 300L // added because GIFT_BOX_START_DELAY was 300L in base class
    }


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

    override fun handleTapOnGiftBox() {}

    fun firstTimeBoxOpenAnimation() {
        boxCallback?.onBoxScaleDownAnimationStart()
        giftBoxState = GiftBoxState.OPEN
        startBoxOpenAnimation()
    }

    fun showConfettiAnimation() {
        showParticleAnimation()
        val bounceAnim = boxBounceAnimation()
        bounceAnim.addListener(onEnd = { isGiftTapAble = true })
        bounceAnim.start()
    }

    fun boxBounceAnimation(): Animator {
        fmGiftBox.pivotY = fmGiftBox.height.toFloat()
        val duration = 250L

        val scaleYAnimDown = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.9f, 1f)
        val bounceAnimDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(fmGiftBox, scaleYAnimDown)
        bounceAnimDown.duration = duration
        bounceAnimDown.interpolator = CubicBezierInterpolator(.645, 0.045, .355, 1.0)
        return bounceAnimDown
    }

    fun incrementTapCount() {
        ++tapCount
    }

    fun resetTapCount() {
        tapCount = 0
    }

    fun getRandomNumber(): Int {
        return (3..5).random()
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

    fun loadFilesForTapTap(glowImageUrl: String?,
                           glowImageShadowUrl: String?,
                           imageFrontUrl: String,
                           bgUrl: String,
                           lidImageList: List<String>,
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

        if (glowImageShadowUrl != null) {
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
        } else {
            TOTAL_ASYNC_IMAGES -= 1
        }

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

        //Download lid images

        lidImagesDownloader.downloadImages(this.context, lidImageList) { images ->
            if (images.isNullOrEmpty()) {
                loadOriginalImages(null, imageCallback)
            } else {
                lidImages.addAll(images)
                loadOriginalImages(images[0], imageCallback)

            }
        }
    }

    override fun loadLidFrames(): Animator {
        return if (lidImages.isEmpty()) {
            super.loadLidFrames()
        } else {
            val valueAnimator = ValueAnimator.ofInt(lidImages.size - 1)
            valueAnimator.addUpdateListener {
                imageGiftBoxLid.setImageBitmap(lidImages[it.animatedValue as Int])
            }
            valueAnimator.duration = LID_ANIMATION_DURATION
            valueAnimator
        }
    }

    private fun loadOriginalImages(bmp: Bitmap?, imageCallback: ((isLoaded: Boolean) -> Unit), @DrawableRes resId: Int = R.drawable.gf_ic_lid_0) {
        val rp = if (bmp != null) {
            Glide.with(this)
                    .load(bmp)

        } else {
            Glide.with(this)
                    .load(resId)
        }
        rp.dontAnimate()
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
    }
}