package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.data.di.modules.AppModule
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxImageDownloadViewModel
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.Companion.NEGATIVE_DELAY_FOR_LARGE_REWARD_ANIM
import com.tokopedia.gamification.pdp.data.LiveDataResult
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

open class GiftBoxDailyView : FrameLayout {

    lateinit var imageGiftBoxLid: AppCompatImageView
    lateinit var fmGiftBox: FrameLayout
    lateinit var imageFlatGlow: DeferredImageView
    lateinit var imageBoxFront: AppCompatImageView
    lateinit var imageBg: AppCompatImageView
    lateinit var imageShadow: AppCompatImageView
    val lidImages = arrayListOf<Bitmap>()

    var boxCallback: BoxCallback? = null
    var initialBounceAnimatorSet: AnimatorSet? = null
    var giftBoxState: GiftBoxState = GiftBoxState.CLOSED

    open var TOTAL_ASYNC_IMAGES = 3
    var imagesLoaded = AtomicInteger(0)
    open var GIFT_BOX_START_DELAY = 300L
    var LID_ANIMATION_DURATION = 450L
    var SCALE_UP_ANIMATION_DURATION = 500L

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GiftBoxImageDownloadViewModel


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

    open fun getLayout() = com.tokopedia.gamification.R.layout.view_gift_box_daily

    private fun setup(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(getLayout(), this, true)
        initViews()
    }

    fun initInjector() {
        context?.let {

            val component = DaggerGiftBoxComponent.builder()
                    .activityContextModule(ActivityContextModule(it))
                    .appModule(AppModule((context as AppCompatActivity).application))
                    .build()
            component.inject(this)

            if (it is AppCompatActivity) {
                val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
                viewModel = viewModelProvider[GiftBoxImageDownloadViewModel::class.java]
            }
        }
    }

    open fun initViews() {
        fmGiftBox = findViewById(R.id.fm_gift_box)
        imageGiftBoxLid = findViewById(R.id.image_gift_box_lid)
        imageFlatGlow = findViewById(R.id.image_flat_glow)
        imageBoxFront = findViewById(R.id.image_box_front)
        imageBg = findViewById(R.id.imageBg)
        imageShadow = findViewById(R.id.imageShadow)

        imageFlatGlow.alpha = 0f
        initInjector()

        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams = lp
    }


    open fun handleTapOnGiftBox() {
        if (giftBoxState == GiftBoxState.CLOSED) {
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

        //total duration ins 750
        boxCallback?.onBoxOpenAnimationStart(pairAnimation.second + GIFT_BOX_START_DELAY - NEGATIVE_DELAY_FOR_LARGE_REWARD_ANIM)
        animatorSet.start()

    }

    fun loadFiles(@TokenUserState state: String,
                  imageFrontUrl: String?,
                  imageBgUrl: String,
                  lidImageList: List<String>,
                  lifecycleOwner: LifecycleOwner,
                  imageCallback: ((isLoaded: Boolean) -> Unit)) {

        fun incrementAndSendCallback() {
            val count = imagesLoaded.incrementAndGet()
            if (count == TOTAL_ASYNC_IMAGES) {
                imageCallback.invoke(true)
            }
        }

        //Lid images
        fun handleSuccessOfImageListDownload(images: List<Bitmap>?) {
            try {
                if (images.isNullOrEmpty()) {
                    imageCallback.invoke(false)
                } else {
                    lidImages.addAll(images)
                    var bmp = images.last()
                    if (state == TokenUserState.ACTIVE) {
                        bmp = images.first()
                    }
                    loadOriginalImages(bmp, imageCallback)
                }
            } catch (ex: Exception) {
                imageCallback.invoke(false)
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

        //Front Image
        Glide.with(imageBoxFront)
                .load(imageFrontUrl)
                .dontAnimate()
                .addListener(getGlideListener(imageCallback))
                .into(imageBoxFront)

        //Bg Image
        fun handleSuccessOfDownloadImage(bmp: Bitmap?){
            try {
                if (bmp != null) {
                    Glide.with(this)
                            .load(bmp)
                            .dontAnimate()
                            .into(imageBg)
                }
                incrementAndSendCallback()
            }catch (ex:Exception){
                imageCallback.invoke(false)
            }
        }

        fun handleFailureOfDownloadImage(){
            imageCallback.invoke(false)
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
        viewModel.downloadImage(imageBgUrl, imageLiveData)
    }

    fun getGlideListener(imageCallback: ((isLoaded: Boolean) -> Unit)): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                Timber.e(e)
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
        }
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
        imageFlatGlow.y = (fmGiftBox.top + imageBoxFront.top - imageFlatGlow.height).toFloat()
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
        val duration = 250L

        view.pivotY = view.height.toFloat()

        //down
        val scaleYAnimDown = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.5f)
        val bounceAnimDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleYAnimDown)
        bounceAnimDown.startDelay = GIFT_BOX_START_DELAY
        bounceAnimDown.duration = duration
        bounceAnimDown.interpolator = CubicBezierInterpolator(.645, 0.045, .355, 1.0)

        val lidAnimator = loadLidFrames()

        val scaleYAnimUp = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1.3f, 1f)
        val bounceAnimUp: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleYAnimUp)
        bounceAnimUp.duration = SCALE_UP_ANIMATION_DURATION

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(bounceAnimUp, lidAnimator)
        animatorSet.playSequentially(bounceAnimDown, bounceAnimUp)
        val totalDuration = bounceAnimDown.duration + bounceAnimUp.duration
        return Pair(animatorSet, totalDuration)
    }

    private fun loadBackupImages(): Animator {
        val drawableArray = arrayOf(
                R.drawable.gf_ic_lid_0,
                R.drawable.gf_ic_lid_0,
                R.drawable.gf_ic_lid_0,
                R.drawable.gf_ic_lid_3,
                R.drawable.gf_ic_lid_3,
                R.drawable.gf_ic_lid_5,
                R.drawable.gf_ic_lid_6,
                R.drawable.gf_ic_lid_7
        )
        val valueAnimator = ValueAnimator.ofInt(drawableArray.size - 1)
        valueAnimator.addUpdateListener {
            imageGiftBoxLid.setImageResource(drawableArray[it.animatedValue as Int])
        }
        valueAnimator.duration = LID_ANIMATION_DURATION
        return valueAnimator
    }

    open fun loadLidFrames(): Animator {
        return if (lidImages.isEmpty()) {
            loadBackupImages()
        } else {
            val valueAnimator = ValueAnimator.ofInt(lidImages.size - 1)
            valueAnimator.addUpdateListener {
                imageGiftBoxLid.setImageBitmap(lidImages[it.animatedValue as Int])
            }
            valueAnimator.duration = LID_ANIMATION_DURATION
            valueAnimator
        }
    }

    fun loadOriginalImages(bmp: Bitmap?, imageCallback: ((isLoaded: Boolean) -> Unit), @DrawableRes resId: Int = R.drawable.gf_ic_lid_0) {
        try {
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
        } catch (ex: Exception) {
            imageCallback.invoke(false)
        }
    }

    enum class GiftBoxState {
        CLOSED, OPEN
    }

    interface BoxCallback {
        fun onBoxScaleDownAnimationStart()
        fun onBoxOpenAnimationStart(startDelay: Long)
        fun onBoxOpened()
    }
}