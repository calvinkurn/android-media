package com.tokopedia.scp_rewards.celebration.presentation.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.PathInterpolator
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.celebration.analytics.CelebrationAnalytics
import com.tokopedia.scp_rewards.celebration.di.CelebrationComponent
import com.tokopedia.scp_rewards.celebration.domain.model.ScpRewardsCelebrationModel
import com.tokopedia.scp_rewards.celebration.presentation.viewmodel.MedalCelebrationViewModel
import com.tokopedia.scp_rewards.common.constants.EASE_IN
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.AudioFactory
import com.tokopedia.scp_rewards.common.utils.dpToPx
import com.tokopedia.scp_rewards.common.utils.hide
import com.tokopedia.scp_rewards.common.utils.isNullOrZero
import com.tokopedia.scp_rewards.common.utils.show
import com.tokopedia.scp_rewards.databinding.CelebrationFragmentLayoutBinding
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MedalCelebrationFragment : BaseDaggerFragment() {

    companion object {
        private const val ANIMATION_DURATION = 300L
        private const val ROTATION_DURATION = 5000L
        private const val ANIMATION_INITIAL_DELAY = 400L
        private const val CONFETTI_URL = "https://json.extendsclass.com/bin/8c8a50287360"
        private const val STARS_URL = "https://json.extendsclass.com/bin/8124418c86cf"
        private const val ASSET_TOTAL_COUNT = 5
        private const val SOUND_EFFECT_URL = "https://res.cloudinary.com/dv9upuqs7/video/upload/v1683464110/samples/people/GoTo_Medali_-_SFX_FINAL_lgskby.mp3"
        private const val FALLBACK_DELAY = 2000L
    }

    // Assets
    private var badgeImage: Drawable? = null
    private var spotlightImage: Drawable? = null
    private var sunburstImage: Drawable? = null
    private var backgroundImage: Drawable? = null
    private var celebrationLottieComposition: LottieComposition? = null
    private var starsLottieComposition: LottieComposition? = null
    private var isBackgroundImageAvailable = false
    private var isBackgroundLoading = false
    private var isSoundAvailable = false
    private var isSoundLoading = false
    private var audioManager: AudioFactory? = null

    // urls
    private var badgeUrl = ""
    private var sunflareUrl = ""
    private var spotlightUrl = ""
    private var celebrationLottieUrl = ""
    private var starsLottieUrl = ""
    private var soundaEffectUrl = ""
    private var backgroundImageUrl = ""

    private var medaliSlug = "INJECT_BADGE_1"

    private var binding: CelebrationFragmentLayoutBinding? = null
    private val handler = Handler(Looper.getMainLooper())

    private var assetCount = 0

    @Inject
    @JvmField
    var viewModelFactory: ViewModelFactory? = null

    private val medalCelebrationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory!!)[MedalCelebrationViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(CelebrationComponent::class.java).inject(this)
    }

    override fun getScreenName() = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CelebrationFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusBarSetup()
        logDeviceInfo()
        setupViewModelObservers()
        medalCelebrationViewModel.getRewards()
    }

    private fun logDeviceInfo(){
        val height = getDeviceHeight()
        val ydpi = Resources.getSystem().displayMetrics.ydpi
        val xdpi = Resources.getSystem().displayMetrics.xdpi
        val disply = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getRealMetrics(disply)
        val height2 = disply.heightPixels
        val ydpi2 = disply.ydpi
        val xdpi2 = disply.xdpi
        val deviceInfo="""
            Device Name - ${Build.MODEL}
            Device Height Px - $height
            Device ydpi - $ydpi
            Device xdpi - $xdpi
            Device real height - ${height / ydpi}
        """.trimIndent()
        val deviceInfo2="""
            Device Name - ${Build.MODEL}
            Device Height Px - $height2
            Device ydpi - $ydpi2
            Device xdpi - $xdpi2
            Device real height - ${height2 / ydpi2}
        """.trimIndent()
        Log.i("from medal celeb","device info 1 -> $deviceInfo")
        Log.i("from medal celeb","device info 2 -> $deviceInfo2")
    }

    private fun setupViewModelObservers() {
        medalCelebrationViewModel.badgeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success<*> -> {
                    changeStatusBarIconsToLight()
                    setupBackground()
                    downloadAssets()
                }
                is Error -> {
                    showErrorView()
                }
                is Loading -> {
                    binding?.mainFlipper?.displayedChild = 0
                }
            }
        }
    }

    private fun setupBackground(){
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply{
            binding?.apply {
                val color = scpRewardsCelebrationPage?.celebrationPage?.backgroundColor
                if (!color.isNullOrBlank()) {
                    val parsedColor = Color.parseColor(color)
                    mainFlipper.setBackgroundColor(parsedColor)
                    binding?.loader?.type = LoaderUnify.TYPE_DECORATIVE_WHITE
                }
            }
        }
    }

    private fun initStatusBarSetup(){
        activity?.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

    private fun changeStatusBarIconsToLight(){
        activity?.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun downloadAssets() {
        setAssetUrls()
        loadSound()
        loadBadge()
        loadSunflare()
        loadSpotlight()
        loadBackgroundImage()
        loadCelebrationLottie()
        loadStarsLottie()
    }

    private fun setAssetUrls() {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            badgeUrl = scpRewardsCelebrationPage?.celebrationPage?.medaliIconImageURL ?: ""
            sunflareUrl = scpRewardsCelebrationPage?.celebrationPage?.medaliEffectImageURL ?: ""
            spotlightUrl = scpRewardsCelebrationPage?.celebrationPage?.medaliSpotLightImageURL ?: ""
            starsLottieUrl = scpRewardsCelebrationPage?.celebrationPage?.medaliBlinkingLottieURL ?: ""
            celebrationLottieUrl = scpRewardsCelebrationPage?.celebrationPage?.medaliConfettiLottieURL ?: ""
            soundaEffectUrl = scpRewardsCelebrationPage?.celebrationPage?.medaliSoundEffectURL ?: ""
            backgroundImageUrl = scpRewardsCelebrationPage?.celebrationPage?.backgroundImageURL ?: ""
        }
    }

    private fun loadSound() {
        isSoundLoading = true
        audioManager = AudioFactory.createAudioFromUrl(
            url = soundaEffectUrl,
            onPrepare = {
                isSoundAvailable = true
                isSoundLoading = false
                incrementAssetCount(false)
            },
            onError = {
                isSoundAvailable = false
                isSoundLoading = false
                incrementAssetCount(false)
            }
        ).withTimeout()
    }

    private fun loadBadge() {
        loadImageFromUrl(badgeUrl, {
            badgeImage = it
            incrementAssetCount()
        }) {
            redirectToMedaliDetail()
        }
    }

    private fun loadSunflare() {
        loadImageFromUrl(sunflareUrl, {
            sunburstImage = it
            incrementAssetCount()
        }) {
            redirectToMedaliDetail()
        }
    }

    private fun loadSpotlight() {
        loadImageFromUrl(spotlightUrl, {
            spotlightImage = it
            incrementAssetCount()
        }) {
            redirectToMedaliDetail()
        }
    }

    private fun loadBackgroundImage() {
        isBackgroundLoading = true
        loadImageFromUrl(backgroundImageUrl, {
            isBackgroundLoading = false
            isBackgroundImageAvailable = true
            backgroundImage = it
            incrementAssetCount(mandatory = false)
        }, {
            isBackgroundLoading = false
            isBackgroundImageAvailable = false
            incrementAssetCount(false)
        })
    }

    private fun loadCelebrationLottie() {
        loadLottieFromUrl(binding?.mainView?.celebrationView, celebrationLottieUrl, {
            celebrationLottieComposition = it
            incrementAssetCount()
        }) {
            redirectToMedaliDetail()
        }
    }

    private fun loadStarsLottie() {
        loadLottieFromUrl(binding?.mainView?.lottieStars, starsLottieUrl, {
            starsLottieComposition = it
            incrementAssetCount()
        }) {
            redirectToMedaliDetail()
        }
    }

    private fun incrementAssetCount(mandatory: Boolean = true) {
        if (mandatory) assetCount++
        if (assetCount == ASSET_TOTAL_COUNT && !isSoundLoading && !isBackgroundLoading) {
            showMainView()
        }
    }

    private fun showMainView() {
        binding?.mainFlipper?.displayedChild = 1
        initViewSetup()
//        configureStatusBar()
//        setupHeader()
        handler.postDelayed(
            {
                startPageAnimation()
            },
            ANIMATION_INITIAL_DELAY
        )
    }

    private fun initViewSetup() {
        binding?.mainView?.apply {
            celebrationHeading.alpha = 0f
            badgeName.alpha = 0f
            badgeDescription.alpha = 0f
            sunflare.alpha = 0f
            sponsorCard.alpha = 0f
            spotlight.alpha = 0f
            badgeImage.alpha = 0f
            celebrationView.setImageDrawable(null)
            lottieStars.setImageDrawable(null)
            setAllText()
            configureBackgroundImage()
        }
    }

    private fun setAllText() {
        binding?.mainView?.apply {
            (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
                celebrationHeading.text = scpRewardsCelebrationPage?.celebrationPage?.title
                badgeName.text = scpRewardsCelebrationPage?.celebrationPage?.medaliName
                brandTag.text = scpRewardsCelebrationPage?.celebrationPage?.medaliSourceText
                badgeDescription.text = scpRewardsCelebrationPage?.celebrationPage?.medaliDescription
            }
        }
    }

    private fun configureBackgroundImage() {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            binding?.mainView?.apply {
                if (isBackgroundImageAvailable) {
                    backgroundImage.setImageDrawable(this@MedalCelebrationFragment.backgroundImage)
                } else {
                    backgroundImage.hide()
                }
            }
        }
    }

//    private fun setupHeader(){
//        binding?.mainView?.closeBtn?.setOnClickListener {
//            activity?.finish()
//        }
//    }

    private fun scaleView(view: View?, duration: Long, from: Int = 0, to: Int = 1, interpolatorType: Int, listener: Animator.AnimatorListener? = null) {
        view?.let { _ ->
            val scaleXPvh = getScaleXPropertyValueHolder()
            val scaleYPvh = getScaleYPropertyValueHolder()
            val opacityPvh = getOpacityPropertyValueHolder()
            ObjectAnimator.ofPropertyValuesHolder(view, scaleXPvh, scaleYPvh, opacityPvh).apply {
                this.duration = duration
                interpolator = when (interpolatorType) {
                    EASE_IN -> AccelerateDecelerateInterpolator()
                    else -> PathInterpolator(0.63f, 0.01f, 0.29f, 1f)
                }
                addListener()
                listener?.let {
                    addListener(it)
                }
                start()
            }
        }
    }

    private fun translateView(view: View?, duration: Long, from: Int, to: Int = 0, interpolatorType: Int) {
        view?.let { _ ->
            val translatePvh = getTranslationPropertyValueHolder(from, to)
            val opacityPvh = getOpacityPropertyValueHolder()
            ObjectAnimator.ofPropertyValuesHolder(view, translatePvh, opacityPvh).apply {
                this.duration = duration
                interpolator = when (interpolatorType) {
                    EASE_IN -> AccelerateDecelerateInterpolator()
                    else -> PathInterpolator(0.63f, 0.01f, 0.29f, 1f)
                }
                start()
            }
        }
    }

    private fun rotateSunflare() {
        binding?.mainView?.sunflare?.apply {
            ObjectAnimator.ofFloat(this, "rotation", 0f, 360f).apply {
                duration = ROTATION_DURATION
                interpolator = null
                repeatCount = ValueAnimator.INFINITE
                start()
            }
        }
    }

    private fun getTranslationPropertyValueHolder(from: Int, to: Int = 0) = PropertyValuesHolder.ofFloat(
        "translationY",
        from.toFloat(),
        to.toFloat()
    )

    private fun getOpacityPropertyValueHolder(from: Int = 0, to: Int = 255) = PropertyValuesHolder.ofFloat(
        "alpha",
        from.toFloat(),
        to.toFloat()
    )

    private fun getScaleXPropertyValueHolder(from: Int = 0, to: Int = 1) = PropertyValuesHolder.ofFloat(
        "scaleX",
        from.toFloat(),
        to.toFloat()
    )

    private fun getScaleYPropertyValueHolder(from: Int = 0, to: Int = 1) = PropertyValuesHolder.ofFloat(
        "scaleY",
        from.toFloat(),
        to.toFloat()
    )

    private fun loadLottieFromUrl(view: LottieAnimationView?, url: String, success: (composition: LottieComposition) -> Unit, error: (() -> Unit)? = null) {
        view?.let {
            context?.let { it2 ->
                val lottieCompositionTask = LottieCompositionFactory.fromUrl(it2, url)
                lottieCompositionTask.addListener { composition ->
                    success.invoke(composition)
                }
                lottieCompositionTask.addFailureListener {
                    error?.invoke()
                }
            }
        }
    }

    private fun loadImageFromUrl(url: String, success: (drawable: Drawable) -> Unit, error: (() -> Unit)? = null) {
        context?.let {
            Glide.with(it)
                .load(url)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        success.invoke(resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        error?.invoke()
                    }
                })
        }
    }

    private fun startPageAnimation() {
        animateBadge()
        animateSunflare()
        animateSpotlight()
        animateRest()
        playSound()
        handler.postDelayed(
            {
                CelebrationAnalytics.sendImpressionMedalCelebration(medaliSlug)
                showCelebrationConfetti()
                showStarsConfetti()
            },
            ANIMATION_DURATION
        )
    }

    private fun animateBadge() {
        binding?.mainView?.badgeImage?.setImageDrawable(badgeImage)
        val listener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
//                startRedirection()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }
        scaleView(
            view = binding?.mainView?.badgeImage,
            duration = ANIMATION_DURATION,
            interpolatorType = EASE_IN,
            listener = listener
        )
    }

    private fun animateSpotlight() {
        binding?.mainView?.spotlight?.setImageDrawable(spotlightImage)
        scaleView(
            view = binding?.mainView?.spotlight,
            duration = ANIMATION_DURATION,
            interpolatorType = EASE_IN
        )
    }

    private fun animateSunflare() {
        binding?.mainView?.sunflare?.setImageDrawable(sunburstImage)
        val listener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                rotateSunflare()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }
        scaleView(
            view = binding?.mainView?.sunflare,
            duration = ANIMATION_DURATION,
            interpolatorType = EASE_IN,
            listener = listener
        )
    }

    private fun animateRest() {
        binding?.mainView?.apply {
            context?.let {
                translateView(
                    view = celebrationHeading,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, -53).toInt(),
                    interpolatorType = EASE_IN
                )
                translateView(
                    view = badgeName,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, 40).toInt(),
                    interpolatorType = EASE_IN
                )
                translateView(
                    view = sponsorCard,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, 44).toInt(),
                    interpolatorType = EASE_IN
                )
                translateView(
                    view = badgeDescription,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, 40).toInt(),
                    interpolatorType = EASE_IN
                )
            }
        }
    }

    private fun showCelebrationConfetti() {
        celebrationLottieComposition?.let {
            binding?.mainView?.celebrationView?.apply {
                setComposition(it)
                playAnimation()
            }
        }
    }

    private fun showStarsConfetti() {
        starsLottieComposition?.let {
            binding?.mainView?.lottieStars?.apply {
                setComposition(it)
                playAnimation()
            }
        }
    }

    private fun playSound() {
        if (isSoundAvailable) {
            audioManager?.playAudio()
        }
    }

    private fun configureStatusBar() {
        activity?.let {
            it.window.apply {
                val layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                val statusBarHeight = getStatusBarHeight()
                layoutParams.setMargins(0, statusBarHeight, 0, 0)
                binding?.mainView?.mainViewContainer?.layoutParams = layoutParams
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun getStatusBarHeight(): Int {
        var height = 0
        context?.let {
            val resId = it.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resId > 0) {
                height = it.resources.getDimensionPixelSize(resId)
            }
        }
        return height
    }

    private fun showErrorView() {
        context?.let {
            val defaultBg = ContextCompat.getColor(it,R.color.white)
            binding?.mainFlipper?.setBackgroundColor(defaultBg)
        }
        binding?.mainFlipper?.displayedChild = 2
        binding?.errorView?.apply {
            errorSecondaryAction.show()
            if (errorSecondaryAction is UnifyButton) {
                (errorSecondaryAction as UnifyButton).buttonVariant = UnifyButton.Variant.TEXT_ONLY
            }
            errorSecondaryAction.text = context?.getString(R.string.go_back_text)
            errorSecondaryAction.setTextColor(ContextCompat.getColor(context, R.color.dark_grey_nav_color))
            val buttonColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            errorSecondaryAction.setBackgroundColor(buttonColor)
            setActionClickListener { resetPage() }
            setSecondaryActionClickListener { activity?.finish() }
        }
    }

    private fun resetPage() {
        binding?.mainFlipper?.displayedChild = 0
        medalCelebrationViewModel.getRewards()
    }

    private fun fadeOutConfetti() {
        binding?.mainView?.apply {
            val opacityPvh = getOpacityPropertyValueHolder(from = 255, to = 0)
            ObjectAnimator.ofPropertyValuesHolder(this, opacityPvh).apply {
                duration = ANIMATION_DURATION
                interpolator = LinearInterpolator()
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        redirectToMedaliDetail()
                    }
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                start()
            }
        }
    }

    private fun startRedirection() {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            val delay = scpRewardsCelebrationPage?.celebrationPage?.redirectDelayInMilliseconds.isNullOrZero(
                FALLBACK_DELAY
            )
            handler.postDelayed({
                fadeOutConfetti()
            }, delay)
        }
    }

    private fun redirectToMedaliDetail() {
        Toast.makeText(context, "redirecting to medali detail", Toast.LENGTH_LONG).show()
        activity?.finish()
    }

    private fun getDeviceHeight() : Int{
        return Resources.getSystem().displayMetrics.heightPixels
    }

    override fun onStop() {
        super.onStop()
        binding?.mainView?.apply {
            celebrationView.clearAnimation()
            lottieStars.clearAnimation()
        }
        audioManager?.releaseMediaPlayer()
    }
}
