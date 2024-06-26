package com.tokopedia.scp_rewards.celebration.presentation.fragment

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.scp_rewards.R
import com.tokopedia.scp_rewards.celebration.analytics.CelebrationAnalytics
import com.tokopedia.scp_rewards.celebration.di.CelebrationComponent
import com.tokopedia.scp_rewards.celebration.domain.model.ScpRewardsCelebrationModel
import com.tokopedia.scp_rewards.celebration.presentation.viewmodel.MedalCelebrationViewModel
import com.tokopedia.scp_rewards.celebration.presentation.viewmodel.MedalCelebrationViewModel.ScpResult.AllMedaliCelebratedError
import com.tokopedia.scp_rewards.celebration.presentation.viewmodel.MedalCelebrationViewModel.ScpResult.Error
import com.tokopedia.scp_rewards.celebration.presentation.viewmodel.MedalCelebrationViewModel.ScpResult.Loading
import com.tokopedia.scp_rewards.celebration.presentation.viewmodel.MedalCelebrationViewModel.ScpResult.Success
import com.tokopedia.scp_rewards.common.constants.NON_WHITELISTED_USER_ERROR_CODE
import com.tokopedia.scp_rewards.common.utils.hide
import com.tokopedia.scp_rewards.common.utils.isNullOrZero
import com.tokopedia.scp_rewards.common.utils.show
import com.tokopedia.scp_rewards.databinding.CelebrationFragmentLayoutBinding
import com.tokopedia.scp_rewards_common.constants.EASE_IN
import com.tokopedia.scp_rewards_common.constants.LINEAR
import com.tokopedia.scp_rewards_common.utils.AudioFactory
import com.tokopedia.scp_rewards_common.utils.DeviceInfo
import com.tokopedia.scp_rewards_common.utils.ViewUtil.fadeView
import com.tokopedia.scp_rewards_common.utils.ViewUtil.rotate
import com.tokopedia.scp_rewards_common.utils.ViewUtil.scaleAndFadeView
import com.tokopedia.scp_rewards_common.utils.ViewUtil.translateAndFadeView
import com.tokopedia.scp_rewards_common.utils.dpToPx
import com.tokopedia.scp_rewards_common.utils.parseColor
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.scp_rewards_common.R as scp_rewards_commonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@Suppress("UNCHECKED_CAST")
class MedalCelebrationFragment : BaseDaggerFragment() {

    companion object {
        private const val ANIMATION_DURATION = 300L
        private const val ROTATION_DURATION = 5000L
        private const val ANIMATION_INITIAL_DELAY = 400L
        private const val ASSET_TOTAL_COUNT = 5
        private const val FALLBACK_DELAY = 2000L
        private const val MDPI_SCREEN_SIZE = 5.0

        // UI States
        const val LOADING_STATE = 0
        const val HAPPY_STATE = 1
        const val ERROR_STATE = 2
    }

    // Assets
    private var badgeImage: Drawable? = null
    private var spotlightImage: Drawable? = null
    private var sunburstImage: Drawable? = null
    private var celebrationLottieComposition: LottieComposition? = null
    private var starsLottieComposition: LottieComposition? = null
    private var isSoundAvailable = false
    private var isSoundLoading = false
    private var isBackgroundImageAvailable = false
    private var audioManager: AudioFactory? = null

    // urls
    private var badgeUrl = ""
    private var sunflareUrl = ""
    private var spotlightUrl = ""
    private var celebrationLottieUrl = ""
    private var starsLottieUrl = ""
    private var soundaEffectUrl = ""
    private var backgroundImageUrl = ""

    private var medaliSlug = ""

    private var isFallbackCase = false
    private var mandatoryAssetFailure = false

    private var binding: CelebrationFragmentLayoutBinding? = null
    private val handler = Handler(Looper.getMainLooper())

    private var assetLoadCount = 0

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.intent?.let {
            medaliSlug = it.data?.pathSegments?.last() ?: ""
        }
    }

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
        setupViewModelObservers()
        medalCelebrationViewModel.getRewards(medaliSlug, "homepage")
        CelebrationAnalytics.sendImpressionCelebrationLoading(medaliSlug)
    }

    private fun setupViewModelObservers() {
        medalCelebrationViewModel.badgeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success<*> -> {
                    changeStatusBarIconsToLight()
                    showMainView()
                    downloadAssets()
                }
                is AllMedaliCelebratedError -> {
                    redirectToAppLink(
                        it.data.scpRewardsCelebrationPage?.celebrationPage?.redirectAppLink,
                        it.data.scpRewardsCelebrationPage?.celebrationPage?.redirectSourceName ?: ""
                    )
                }
                is Error -> {
                    CelebrationAnalytics.sendImpressionCelebrationError(medaliSlug)
                    handleError(it)
                }
                is Loading -> {
                    binding?.mainFlipper?.displayedChild = LOADING_STATE
                }
                else -> {}
            }
        }
    }

    private fun showMainView() {
        binding?.apply {
            mainFlipper.displayedChild = HAPPY_STATE
        }
        setupBackground()
    }

    private fun setupBackground() {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            binding?.apply {
                val color = scpRewardsCelebrationPage?.celebrationPage?.backgroundColor
                context?.let {
                    mainView.container.setBackgroundColor(parseColor(color) ?: ContextCompat.getColor(it, unifyprinciplesR.color.Unify_NN0))
                }
            }
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun initStatusBarSetup() {
        activity?.window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun changeStatusBarIconsToLight() {
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
            starsLottieUrl =
                scpRewardsCelebrationPage?.celebrationPage?.medaliBlinkingLottieURL ?: ""
            celebrationLottieUrl =
                scpRewardsCelebrationPage?.celebrationPage?.medaliConfettiLottieURL ?: ""
            soundaEffectUrl = scpRewardsCelebrationPage?.celebrationPage?.medaliSoundEffectURL ?: ""
            backgroundImageUrl =
                scpRewardsCelebrationPage?.celebrationPage?.backgroundImageURL ?: ""
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
            isFallbackCase = true
            showAnimatedView()
        }
    }

    private fun loadSunflare() {
        loadImageFromUrl(sunflareUrl, {
            sunburstImage = it
            incrementAssetCount()
        }) {
            mandatoryAssetFailure = true
            incrementAssetCount()
        }
    }

    private fun loadSpotlight() {
        loadImageFromUrl(spotlightUrl, {
            spotlightImage = it
            incrementAssetCount()
        }) {
            mandatoryAssetFailure = true
            incrementAssetCount()
        }
    }

    private fun loadBackgroundImage() {
        loadImageFromUrl(backgroundImageUrl, {
            isBackgroundImageAvailable = true
            configureBackgroundImage(it)
        }, {
            isBackgroundImageAvailable = false
        })
    }

    private fun loadCelebrationLottie() {
        loadLottieFromUrl(binding?.mainView?.celebrationView, celebrationLottieUrl, {
            celebrationLottieComposition = it
            incrementAssetCount()
        }) {
            mandatoryAssetFailure = true
            incrementAssetCount()
        }
    }

    private fun loadStarsLottie() {
        loadLottieFromUrl(binding?.mainView?.lottieStars, starsLottieUrl, {
            starsLottieComposition = it
            incrementAssetCount()
        }) {
            mandatoryAssetFailure = true
            incrementAssetCount()
        }
    }

    private fun incrementAssetCount(mandatory: Boolean = true) {
        if (mandatory) assetLoadCount++
        if (assetLoadCount == ASSET_TOTAL_COUNT && !isSoundLoading) {
            showAnimatedView()
        }
    }

    private fun showAnimatedView() {
        binding?.mainView?.animationViewFlipper?.displayedChild = HAPPY_STATE
        initViewSetup()
        handler.postDelayed(
            {
                startPageAnimation()
            },
            ANIMATION_INITIAL_DELAY
        )
    }

    private fun initViewSetup() {
        setupHeadingMarginBasedOnDeviceSize()
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
            setupSponsorCard()
        }
    }

    private fun setupHeadingMarginBasedOnDeviceSize() {
        val screenSize = DeviceInfo.getScreenSizeInInches(context)
        if (screenSize > MDPI_SCREEN_SIZE) {
            binding?.mainView?.celebrationHeading?.apply {
                val lp = layoutParams as ConstraintLayout.LayoutParams
                val topMargin = resources.getDimensionPixelSize(R.dimen.mdpi_device_top_margin)
                lp.setMargins(0, topMargin, 0, 0)
            }
        }
    }

    private fun setAllText() {
        binding?.mainView?.apply {
            (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
                celebrationHeading.text = scpRewardsCelebrationPage?.celebrationPage?.title
                badgeName.text = scpRewardsCelebrationPage?.celebrationPage?.medaliName
                badgeDescription.text = scpRewardsCelebrationPage?.celebrationPage?.medaliDescription
            }
        }
    }

    private fun setupSponsorCard() {
        binding?.mainView?.apply {
            (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
                val medaliSourceText = scpRewardsCelebrationPage?.celebrationPage?.medaliSourceText.orEmpty()
                val medaliSourceBgColor = scpRewardsCelebrationPage?.celebrationPage?.medaliSourceBackgroundColor.orEmpty()
                val medaliSourceFontColor = scpRewardsCelebrationPage?.celebrationPage?.medaliSourceFontColor.orEmpty()
                if (medaliSourceText.isNotEmpty()) {
                    brandTag.text = scpRewardsCelebrationPage?.celebrationPage?.medaliSourceText
                    context?.let {
                        brandTag.setTextColor(parseColor(medaliSourceFontColor) ?: ContextCompat.getColor(it, unifyprinciplesR.color.Unify_NN0))
                        sponsorCard.setCardBackgroundColor(parseColor(medaliSourceBgColor) ?: ContextCompat.getColor(it, unifyprinciplesR.color.Unify_NN1000))
                    }
                } else {
                    hideSponsorCard()
                }
            }
        }
    }

    private fun hideSponsorCard() {
        binding?.mainView?.sponsorCard?.hide()
        binding?.mainView?.badgeName?.apply {
            val lp = layoutParams as ConstraintLayout.LayoutParams
            lp.topMargin = resources.getDimensionPixelSize(R.dimen.margin_20_dp)
        }
    }

    private fun configureBackgroundImage(bgImage: Drawable) {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            binding?.mainView?.backgroundImage?.apply {
                show()
                setImageDrawable(bgImage)
            }
        }
    }

    private fun rotateSunflare() {
        binding?.mainView?.sunflare?.rotate(animationDuration = ROTATION_DURATION)
    }

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
            url.getBitmapImageUrl(it, {}, MediaBitmapEmptyTarget(
                onReady = { bitmap ->
                    success.invoke(bitmap.toDrawable(it.resources))
                },
                onFailed = {
                    error?.invoke()
                }
            ))
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
        val badgeDrawable = if (isFallbackCase) {
            CelebrationAnalytics.sendImpressionFallbackBadge(medaliSlug)
            changeBadgeSize()
            ResourcesCompat.getDrawable(resources, scp_rewards_commonR.drawable.fallback_badge, null)
        } else {
            badgeImage
        }
        binding?.mainView?.badgeImage?.setImageDrawable(badgeDrawable)
        val listener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                if (isFallbackCase || mandatoryAssetFailure) {
                    startRedirection()
                }
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }
        binding?.mainView?.badgeImage?.scaleAndFadeView(
            duration = ANIMATION_DURATION,
            interpolatorType = EASE_IN,
            listener = listener
        )
    }

    private fun changeBadgeSize() {
        binding?.mainView?.badgeImage?.apply {
            val newWidth = resources.getDimensionPixelSize(R.dimen.fallback_badge_width)
            val newHeight = resources.getDimensionPixelSize(R.dimen.fallback_badge_height)
            val lp = layoutParams as ConstraintLayout.LayoutParams
            lp.width = newWidth
            lp.height = newHeight
            layoutParams = lp
        }
    }

    private fun animateSpotlight() {
        if (shouldShowAsset()) {
            binding?.mainView?.spotlight?.setImageDrawable(spotlightImage)
            binding?.mainView?.spotlight?.apply {
                pivotX = width / 2f
                pivotY = 0f
            }
            binding?.mainView?.spotlight?.scaleAndFadeView(
                duration = ANIMATION_DURATION,
                interpolatorType = EASE_IN
            )
        }
    }

    private fun animateSunflare() {
        if (shouldShowAsset()) {
            binding?.mainView?.sunflare?.setImageDrawable(sunburstImage)
            val listener = object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    rotateSunflare()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            }
            binding?.mainView?.sunflare?.scaleAndFadeView(
                duration = ANIMATION_DURATION,
                interpolatorType = EASE_IN,
                listener = listener
            )
        }
    }

    private fun animateRest() {
        binding?.mainView?.apply {
            context?.let {
                val dimen53 = it.resources.getDimension(R.dimen.dimen_53).toInt()
                val dimen40 = it.resources.getDimension(R.dimen.dimen_40).toInt()
                val dimen44 = it.resources.getDimension(R.dimen.dimen_44).toInt()
                celebrationHeading.translateAndFadeView(
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, -dimen53).toInt(),
                    interpolatorType = EASE_IN
                )
                badgeName.translateAndFadeView(
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, dimen40).toInt(),
                    interpolatorType = EASE_IN
                )
                sponsorCard.translateAndFadeView(
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, dimen44).toInt(),
                    interpolatorType = EASE_IN
                )
                badgeDescription.translateAndFadeView(
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, dimen40).toInt(),
                    interpolatorType = EASE_IN
                )
            }
        }
    }

    private fun showCelebrationConfetti() {
        if (shouldShowAsset()) {
            celebrationLottieComposition?.let {
                binding?.mainView?.celebrationView?.apply {
                    setComposition(it)
                    playAnimation()
                    addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationEnd(animation: Animator) {
                            startRedirection()
                        }
                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                }
            }
        }
    }

    private fun showStarsConfetti() {
        if (shouldShowAsset()) {
            starsLottieComposition?.let {
                binding?.mainView?.lottieStars?.apply {
                    setComposition(it)
                    playAnimation()
                }
            }
        }
    }

    private fun shouldShowAsset() = !isFallbackCase && !mandatoryAssetFailure

    private fun playSound() {
        if (isSoundAvailable && shouldShowAsset()) {
            audioManager?.playAudio()
        }
    }

    private fun handleError(scpError: Error) {
        context?.let {
            val defaultBg = ContextCompat.getColor(it, unifyprinciplesR.color.Unify_NN0)
            binding?.mainFlipper?.setBackgroundColor(defaultBg)
        }
        binding?.mainFlipper?.displayedChild = ERROR_STATE

        val error = scpError.error
        when {
            error is UnknownHostException || error is SocketTimeoutException -> {
                binding?.errorView?.apply {
                    setType(GlobalError.NO_CONNECTION)
                    setActionClickListener {
                        CelebrationAnalytics.sendClickRetryCelebration(medaliSlug)
                        resetPage()
                    }
                }
            }
            scpError.errorCode == NON_WHITELISTED_USER_ERROR_CODE -> {
                binding?.errorView?.apply {
                    setType(GlobalError.PAGE_NOT_FOUND)
                    errorTitle.text = context.getText(R.string.error_non_whitelisted_user_title)
                    errorDescription.text = context.getText(R.string.error_non_whitelisted_user_description)
                    errorAction.text = context.getText(R.string.error_non_whitelisted_user_action)
                    setActionClickListener {
                        CelebrationAnalytics.sendNonWhitelistedUserCtaClick()
                        RouteManager.route(context, ApplinkConst.HOME)
                        activity?.finish()
                    }
                }
                CelebrationAnalytics.sendImpressionNonWhitelistedError()
            }
            else -> {
                binding?.errorView?.apply {
                    setActionClickListener {
                        CelebrationAnalytics.sendClickRetryCelebration(medaliSlug)
                        resetPage()
                    }
                }
            }
        }
    }

    private fun resetPage() {
        binding?.mainFlipper?.displayedChild = LOADING_STATE
        medalCelebrationViewModel.getRewards(medaliSlug, "homepage")
    }

    private fun startRedirection() {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            val delay = scpRewardsCelebrationPage?.celebrationPage?.redirectDelayInMilliseconds.isNullOrZero(
                FALLBACK_DELAY
            )
            handler.postDelayed({
                fadeOutPage()
            }, delay)
        }
    }

    private fun fadeOutPage() {
        binding?.mainView?.root?.fadeView(
            from = 255,
            to = 0,
            duration = ANIMATION_DURATION,
            interpolatorType = LINEAR,
            listener = object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    redirectToMedaliDetail()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            }
        )
    }

    private fun redirectToMedaliDetail() {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            redirectToAppLink(
                scpRewardsCelebrationPage?.celebrationPage?.redirectAppLink,
                scpRewardsCelebrationPage?.celebrationPage?.redirectSourceName ?: ""
            )
        }
    }

    private fun redirectToAppLink(appLink: String?, sourceName: String = "") {
        val args = Bundle().apply { putString(ApplinkConstInternalPromo.SOURCE_PARAM, sourceName) }
        RouteManager.route(context, args, appLink)
        activity?.finish()
    }

    override fun onStop() {
        super.onStop()
        binding?.mainView?.apply {
            celebrationView.clearAnimation()
            lottieStars.clearAnimation()
        }
        audioManager?.releaseMediaPlayer()
    }

    override fun onFragmentBackPressed(): Boolean {
        val state = medalCelebrationViewModel.badgeLiveData.value
        if (state is Error && state.errorCode == NON_WHITELISTED_USER_ERROR_CODE) {
            CelebrationAnalytics.sendNonWhitelistedBackClick()
        }
        return super.onFragmentBackPressed()
    }
}
