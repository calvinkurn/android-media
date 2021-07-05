package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.*
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieTask
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.gamification.R
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.Constants
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxImageDownloadViewModel
import com.tokopedia.gamification.pdp.data.LiveDataResult
import java.util.zip.ZipInputStream
import javax.inject.Inject

class GiftBoxTapTapView : GiftBoxDailyView {

    lateinit var imageBoxWhite: AppCompatImageView
    lateinit var imageBoxGlow: AppCompatImageView
    lateinit var confettiView: LottieAnimationView

    var glowingAnimator: Animator? = null
    var isGiftTapAble = false
    var isBoxAlreadyOpened = false
    var tapCount = 0
    var targetTapCount = 0

    var disableConfettiAnimation = false

    override var TOTAL_ASYNC_IMAGES = 5
    override var GIFT_BOX_START_DELAY = 0L
    val GLIDE_TIME_OUT = 6000

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
        clipChildren = false
        clipToPadding = false
        val task = prepareLoaderLottieTask(Constants.CONFETTI_ZIP_FILE)
        if (task != null)
            addLottieAnimationToView(confettiView, task)
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
        val duration = 350L

        val scaleYAnimDown = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.9f, 1f)
        val bounceAnimDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(fmGiftBox, scaleYAnimDown)
        bounceAnimDown.duration = duration
        bounceAnimDown.interpolator = CubicBezierInterpolator(.645, 0.045, .355, 1.0)
        return bounceAnimDown
    }

    fun incrementTapCount() {
        tapCount += 1
    }

    fun resetTapCount() {
        tapCount = -1
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
                           lifecycleOwner: LifecycleOwner,
                           imageCallback: ((isLoaded: Boolean) -> Unit)) {

        fun incrementAndSendCallback() {
            val count = imagesLoaded.incrementAndGet()
            if (count == TOTAL_ASYNC_IMAGES) {
                imageCallback.invoke(true)
            }
        }

        if (glowImageUrl != null) {
            Glide.with(this)
                    .load(glowImageUrl)
                    .dontAnimate()
                    .timeout(GLIDE_TIME_OUT)
                    .error(R.drawable.gf_gift_white_waktu)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            incrementAndSendCallback()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            incrementAndSendCallback()
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
                    .timeout(GLIDE_TIME_OUT)
                    .error(R.drawable.gf_gift_glow)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            incrementAndSendCallback()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            incrementAndSendCallback()
                            return false
                        }
                    })
                    .into(imageBoxGlow)
        } else {
            TOTAL_ASYNC_IMAGES -= 1
        }

        //Bg Image
        fun handleSuccessOfDownloadImage(bmp: Bitmap?){
            try {
                val rp = if (bmp != null) {
                    Glide.with(this)
                            .load(bmp)
                } else {
                    Glide.with(this)
                            .load(R.drawable.gf_gift_bg)
                }
                rp.dontAnimate()
                        .into(imageBg)
                incrementAndSendCallback()
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }

        fun handleFailureOfDownloadImage(){
            try {
                Glide.with(this)
                        .load(R.drawable.gf_gift_bg)
                        .dontAnimate()
                        .into(imageBg)

                incrementAndSendCallback()
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }

        val imageLiveData = MutableLiveData<LiveDataResult<Bitmap?>>()
        imageLiveData.observe(lifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    val bmp = it.data
                    handleSuccessOfDownloadImage(bmp)
                }
                LiveDataResult.STATUS.ERROR -> {
                    handleFailureOfDownloadImage()
                }
            }
        })
        viewModel.downloadImage(bgUrl, imageLiveData)

        Glide.with(this)
                .load(imageFrontUrl)
                .dontAnimate()
                .timeout(GLIDE_TIME_OUT)
                .error(R.drawable.gf_gift_green_front)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        incrementAndSendCallback()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        incrementAndSendCallback()
                        return false
                    }
                })
                .into(imageBoxFront)

        //Download lid images
        fun handleSuccessOfImageListDownload(images: List<Bitmap>?){

            if (images.isNullOrEmpty()) {
                loadOriginalImages(null, imageCallback)
            } else {
                lidImages.addAll(images)
                loadOriginalImages(images[0], imageCallback)
            }
        }

        val imageListLiveData = MutableLiveData<LiveDataResult<List<Bitmap>?>>()
        imageListLiveData.observe(lifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    handleSuccessOfImageListDownload(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    loadOriginalImages(null, imageCallback)
                }
            }
        })
        viewModel.downloadImages(lidImageList, imageListLiveData)
    }

    private fun prepareLoaderLottieTask(fileName: String): LottieTask<LottieComposition>? {
        context?.let { c ->
            val lottieFileZipStream = ZipInputStream(c.assets.open(fileName))
            return LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
        }
        return null
    }

    private fun addLottieAnimationToView(lottie: LottieAnimationView, task: LottieTask<LottieComposition>) {
        task.addListener { result: LottieComposition? ->
            result?.let {
                lottie.setComposition(result)
            }
        }
    }
}