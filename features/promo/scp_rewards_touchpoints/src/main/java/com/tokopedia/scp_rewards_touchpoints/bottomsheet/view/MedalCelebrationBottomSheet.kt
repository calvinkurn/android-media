package com.tokopedia.scp_rewards_touchpoints.bottomsheet.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.PathInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.scp_rewards_touchpoints.R
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.analytics.CelebrationBottomSheetAnalytics
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.constants.EASE_IN
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.ScpRewardsCelebrationModel
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.getBenefitCta
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.getMessage
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.AudioFactory
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.DeviceInfo
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.dpToPx
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.launchLink
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils.parseColor
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.viewmodel.MedalCelebrationViewModel
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Loading
import com.tokopedia.scp_rewards_touchpoints.common.PRIMARY_STYLE
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.common.di.DaggerCelebrationComponent
import com.tokopedia.scp_rewards_touchpoints.databinding.CelebrationBottomSheetFragmentLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.design.R as designR
import com.tokopedia.scp_rewards_common.R as scp_rewards_commonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class MedalCelebrationBottomSheet : BottomSheetUnify() {

    // Assets
    private var badge_image: Drawable? = null
    private var spotlightImage: Drawable? = null
    private var sunburstImage: Drawable? = null
    private var coupon_image: Drawable? = null
    private var celebrationLottieComposition: LottieComposition? = null
    private var starsLottieComposition: LottieComposition? = null
    private var isSoundAvailable = false
    private var isSoundLoading = false
    private var isBackgroundImageAvailable = false
    private var audioManager: AudioFactory? = null
    private var bgColor: String? = ""

    // urls
    private var badgeUrl = ""
    private var sunflareUrl = ""
    private var spotlightUrl = ""
    private var celebrationLottieUrl = ""
    private var starsLottieUrl = ""
    private var soundaEffectUrl = ""
    private var backgroundImageUrl = ""
    private var couponImageUrl = ""

    private var medaliSlug = "UNILEVER_CLUB"

    private var isFallbackCase = false
    private var mandatoryAssetFailure = false

    private var binding: CelebrationBottomSheetFragmentLayoutBinding? = null
    private val handler = Handler(Looper.getMainLooper())

    private var assetLoadCount = 0

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val medalCelebrationViewModel: MedalCelebrationViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider[MedalCelebrationViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.intent?.let {
            medaliSlug = it.data?.pathSegments?.last() ?: ""
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerCelebrationComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CelebrationBottomSheetFragmentLayoutBinding.inflate(inflater, container, false)

        setDefaultParams()
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCloseButtonBehaviour()
        initStatusBarSetup()
        bottomSheetWrapper.apply {
            setPadding(0, 0, 0, 0)
        }
//        CelebrationAnalytics.sendImpressionCelebrationLoading(medaliSlug)
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
                    showErrorView(it.error)
                }
                is Loading -> {
                    binding?.mainFlipper?.displayedChild = LOADING_STATE
                    hideCloseButton()
                }

                else -> {}
            }
        }

        medalCelebrationViewModel.autoApplyLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MedalCelebrationViewModel.AutoApplyState.SuccessCoupon -> {
                    binding?.mainView?.couponUi?.btnPrimary?.isLoading = false
                    showToastAndNavigateToLink(
                        message = it.autoApplyData.getMessage(),
                        url = it.benefitData?.url ?: "",
                        appLink = it.benefitData?.appLink ?: ""
                    )
                }

                is MedalCelebrationViewModel.AutoApplyState.Error -> {
                    binding?.mainView?.couponUi?.btnPrimary?.isLoading = false
                    showToastAndNavigateToLink(
                        message = it.throwable.localizedMessage ?: "",
                        url = it.benefitData?.url ?: "",
                        appLink = it.benefitData?.appLink ?: ""
                    )
                }

                is MedalCelebrationViewModel.AutoApplyState.Loading -> {
                    binding?.mainView?.couponUi?.btnPrimary?.isLoading = true
                }

                else -> {}
            }
        }
    }

    @SuppressLint("Range")
    private fun setupBackground() {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            binding?.apply {
                bgColor = scpRewardsCelebrationPage?.celebrationPage?.backgroundColor
                parentContainer.backgroundTintList = ColorStateList.valueOf(Color.parseColor(bgColor))
                loader.type = LoaderUnify.TYPE_DECORATIVE_WHITE
            }
        }
    }

    private fun setupCloseButtonBehaviour() {
        binding?.btnClose?.setOnClickListener {
            CelebrationBottomSheetAnalytics.clickCloseBottomSheet(medaliSlug, getBenefitCta(PRIMARY_STYLE)?.couponCode ?: "")
            dismiss()
        }
    }

    private fun setCloseBtnColor(whiteBtn: Boolean = true) {
        context?.let {
            val iconColor = if (whiteBtn) {
                ResourcesCompat.getColor(it.resources, unifyprinciplesR.color.Unify_NN0, null)
            } else {
                ResourcesCompat.getColor(it.resources, unifyprinciplesR.color.Unify_NN900, null)
            }
            binding?.btnClose?.setImage(
                newLightEnable = iconColor,
                newDarkEnable = iconColor
            )
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
        loadCouponImage()
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
            backgroundImageUrl = scpRewardsCelebrationPage?.celebrationPage?.backgroundImageURL ?: ""
            couponImageUrl = scpRewardsCelebrationPage?.celebrationPage?.benefit?.firstOrNull()?.imageURL ?: ""
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
            badge_image = it
            incrementAssetCount()
        }) {
            isFallbackCase = true
            showAnimatedView()
        }
    }

    private fun loadCouponImage() {
        loadImageFromUrl(couponImageUrl, {
            coupon_image = it
            incrementAssetCount()
        }) {
            incrementAssetCount()
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
        binding?.mainFlipper?.displayedChild = HAPPY_STATE
        initViewSetup()
        setupCouponCtaListeners()
        handler.postDelayed(
            {
                startPageAnimation()
            },
            ANIMATION_INITIAL_DELAY
        )
    }

    private fun initViewSetup() {
//        setupHeadingMarginBasedOnDeviceSize()
        binding?.mainView?.apply {
            celebrationHeading.alpha = 0f
            badgeName.alpha = 0f
            sunflare.alpha = 0f
            sponsorCard.alpha = 0f
            spotlight.alpha = 0f
            badgeImage.alpha = 0f
            binding?.mainView?.couponUi?.root?.alpha = 0f
            celebrationView.setImageDrawable(null)
            lottieStars.setImageDrawable(null)
            setAllText()
            setupSponsorCard()
            showCloseButton()
            setCloseBtnColor()
            hideAllBenefitCta()
        }
    }

    private fun hideAllBenefitCta() {
        binding?.mainView?.couponUi?.apply {
            btnPrimary.hide()
            btnSecondary.hide()
        }
    }

    private fun setupHeadingMarginBasedOnDeviceSize() {
        val screenSize = DeviceInfo.getScreenSizeInInches(context)
        if (screenSize > MDPI_SCREEN_SIZE) {
            binding?.mainView?.celebrationHeading?.apply {
                val lp = layoutParams as ConstraintLayout.LayoutParams
                val topMargin =
                    context.resources.getDimensionPixelSize(R.dimen.mdpi_device_top_margin)
                lp.setMargins(0, topMargin, 0, 0)
            }
        }
    }

    private fun setAllText() {
        binding?.mainView?.apply {
            (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
                celebrationHeading.text = scpRewardsCelebrationPage?.celebrationPage?.title
                badgeName.text = scpRewardsCelebrationPage?.celebrationPage?.medaliName
                val benefitList = scpRewardsCelebrationPage?.celebrationPage?.benefitButton ?: listOf()
                benefitList.forEach { benefitBtn ->
                    if (benefitBtn.unifiedStyle == PRIMARY_STYLE) {
                        couponUi.btnPrimary.text = benefitBtn.text
                    } else {
                        couponUi.btnSecondary.text = benefitBtn.text
                    }
                }
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
                    brandTag.setTextColor(parseColor(medaliSourceFontColor) ?: Color.WHITE)
                    sponsorCard.setCardBackgroundColor(parseColor(medaliSourceBgColor) ?: Color.BLACK)
                } else {
                    hideSponsorCard()
                }
            }
        }
    }

    private fun hideSponsorCard() {
        binding?.mainView?.sponsorCard?.hide()
    }

    private fun configureBackgroundImage(bgImage: Drawable) {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            binding?.mainView?.backgroundImage?.apply {
                show()
                cornerRadius = 16
                setImageDrawable(bgImage)
            }
        }
    }

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
        view?.let {
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

    private fun fadeView(view: View?, duration: Long, from: Int = 0, to: Int = 255, interpolatorType: Int) {
        view?.let {
            val opacityPvh = getOpacityPropertyValueHolder()
            ObjectAnimator.ofPropertyValuesHolder(view, opacityPvh).apply {
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
            ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 360f).apply {
                duration = ROTATION_DURATION
                interpolator = null
                repeatCount = ValueAnimator.INFINITE
                start()
            }
        }
    }

    private fun getTranslationPropertyValueHolder(from: Int, to: Int = 0) = PropertyValuesHolder.ofFloat(
        View.TRANSLATION_Y,
        from.toFloat(),
        to.toFloat()
    )

    private fun getOpacityPropertyValueHolder(from: Int = 0, to: Int = 255) = PropertyValuesHolder.ofFloat(
        View.ALPHA,
        from.toFloat(),
        to.toFloat()
    )

    private fun getScaleXPropertyValueHolder(from: Int = 0, to: Int = 1) = PropertyValuesHolder.ofFloat(
        View.SCALE_X,
        from.toFloat(),
        to.toFloat()
    )

    private fun getScaleYPropertyValueHolder(from: Int = 0, to: Int = 1) = PropertyValuesHolder.ofFloat(
        View.SCALE_Y,
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
        animateCoupon()
        animateSunflare()
        animateSpotlight()
        animateRest()
        playSound()
        handler.postDelayed(
            {
                CelebrationBottomSheetAnalytics.impressionCelebrationBottomSheet(medaliSlug, getBenefitCta(PRIMARY_STYLE)?.couponCode ?: "")
                showCelebrationConfetti()
                showStarsConfetti()
            },
            ANIMATION_DURATION
        )
    }

    private fun setupCouponCtaListeners() {
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            binding?.mainView?.couponUi?.apply {
                val benefitList = scpRewardsCelebrationPage?.celebrationPage?.benefitButton ?: listOf()
                benefitList.forEach { benefit ->
                    val button = if (benefit.unifiedStyle == PRIMARY_STYLE) {
                        btnPrimary.show()
                        btnPrimary
                    } else {
                        btnSecondary.show()
                        btnSecondary
                    }
                    val primaryBenefit = getBenefitCta(PRIMARY_STYLE)
                    button.setOnClickListener {
                        if (benefit.isAutoApply) {
                            medalCelebrationViewModel.autoApplyCoupon(
                                couponCode = benefit.couponCode,
                                benefitData = benefit
                            )
                        } else {
                            context?.launchLink(benefit.appLink, benefit.url)
                            activity?.finish()
                        }
                        if (benefit.unifiedStyle == PRIMARY_STYLE) {
                            CelebrationBottomSheetAnalytics.clickPrimaryCta(medaliSlug, primaryBenefit?.couponCode ?: "")
                        } else {
                            CelebrationBottomSheetAnalytics.clickSecondaryCta(medaliSlug, primaryBenefit?.couponCode ?: "")
                        }
                    }
                }
            }
        }
    }

    private fun animateBadge() {
        val badgeDrawable = if (isFallbackCase) {
            changeBadgeSize()
            context?.let {
                ResourcesCompat.getDrawable(it.resources, scp_rewards_commonR.drawable.fallback_badge, null)
            }
        } else {
            badge_image
        }
        binding?.mainView?.badgeImage?.setImageDrawable(badgeDrawable)
        val listener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
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

    private fun animateCoupon() {
        val couponDrawable = if (coupon_image == null) {
            binding?.mainView?.couponUi?.couponImage?.isEdgeControl = false
            context?.let {
                ResourcesCompat.getDrawable(it.resources, R.drawable.coupon_fallback, null)
            }
        } else {
            binding?.mainView?.couponUi?.couponImage?.isEdgeControl = true
            binding?.mainView?.couponUi?.couponImage?.circularEdgeColor = Color.parseColor(bgColor)
            coupon_image
        }
        binding?.mainView?.couponUi?.couponImage?.setImageDrawable(couponDrawable)
        context?.let {
            val dimen53 = it.resources.getDimension(R.dimen.dimen_53).toInt()
            translateView(
                view = binding?.mainView?.couponUi?.root,
                duration = ANIMATION_DURATION,
                from = dpToPx(it, dimen53).toInt(),
                interpolatorType = EASE_IN
            )
        }
    }

    private fun changeBadgeSize() {
        binding?.mainView?.badgeImage?.apply {
            val newWidth = context.resources.getDimensionPixelSize(R.dimen.fallback_badge_width)
            val newHeight = context.resources.getDimensionPixelSize(R.dimen.fallback_badge_height)
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
            scaleView(
                view = binding?.mainView?.spotlight,
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
            scaleView(
                view = binding?.mainView?.sunflare,
                duration = ANIMATION_DURATION,
                interpolatorType = EASE_IN,
                listener = listener
            )
        }
    }

    private fun animateRest() {
        binding?.mainView?.apply {
            context?.let {
                val dimen44 = it.resources.getDimension(R.dimen.dimen_44).toInt()
                fadeView(
                    view = celebrationHeading,
                    duration = ANIMATION_DURATION,
                    interpolatorType = EASE_IN
                )
                translateView(
                    view = badgeName,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, dimen44).toInt(),
                    interpolatorType = EASE_IN
                )
                translateView(
                    view = sponsorCard,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it, dimen44).toInt(),
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

    private fun showErrorView(error: Throwable) {
        context?.let {
            val defaultBg = ContextCompat.getColor(it, designR.color.white)
            binding?.mainFlipper?.backgroundTintList = ColorStateList.valueOf(defaultBg)
        }
        binding?.mainFlipper?.displayedChild = ERROR_STATE
        showCloseButton()
        setCloseBtnColor(false)
        if (error is UnknownHostException || error is SocketTimeoutException) {
            binding?.errorView?.setType(GlobalError.NO_CONNECTION)
        } else {
            binding?.errorView?.apply {
                errorSecondaryAction.show()
                if (errorSecondaryAction is UnifyButton) {
                    (errorSecondaryAction as UnifyButton).buttonVariant = UnifyButton.Variant.TEXT_ONLY
                }
                errorSecondaryAction.text = context?.getString(R.string.go_back_text)
                errorSecondaryAction.setTextColor(ContextCompat.getColor(context, R.color.dark_grey_nav_color))
                val buttonColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
                errorSecondaryAction.setBackgroundColor(buttonColor)
                setSecondaryActionClickListener {
                    activity?.finish()
                }
            }
        }
        binding?.errorView?.setActionClickListener {
            resetPage()
        }
    }

    private fun resetPage() {
        hideCloseButton()
        binding?.mainFlipper?.displayedChild = LOADING_STATE
        medalCelebrationViewModel.getRewards(medaliSlug, "medali_celebration_bottomsheet")
    }
    override fun onStop() {
        super.onStop()
        binding?.mainView?.apply {
            celebrationView.clearAnimation()
            lottieStars.clearAnimation()
        }
        audioManager?.releaseMediaPlayer()
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = false
        clearContentPadding = true
        showHeader = false
        setCloseClickListener {
            dismiss()
        }

        customPeekHeight = (getScreenHeight()).toDp()
    }

    private fun initBottomSheet() {
        setChild(binding?.root)
        setupViewModelObservers()
        medalCelebrationViewModel.getRewards(medaliSlug, "medali_celebration_bottomsheet")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    private fun hideCloseButton() {
        binding?.btnClose?.hide()
    }

    private fun showCloseButton() {
        binding?.btnClose?.show()
    }

    private fun showToastAndNavigateToLink(
        message: String?,
        appLink: String?,
        url: String?
    ) {
        Toaster.apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                toasterCustomBottomHeight = getNavigationBarHeight() - 8.toPx()
            }
        }
            .build(binding?.root!!, message.orEmpty())
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    requireContext().launchLink(appLink, url)
                    activity?.finish()
                }
            })
            .show()
    }

    private fun getNavigationBarHeight(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val imm =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
        val resources = context?.resources
        val resourceId: Int =
            resources?.getIdentifier("navigation_bar_height", "dimen", "android").toZeroIfNull()
        return if (resourceId > 0) {
            resources?.getDimensionPixelSize(resourceId) ?: 0
        } else {
            0
        }
    }

    private fun getBenefitCta(buttonType: String): ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage.CelebrationPage.BenefitButton? {
        return if (medalCelebrationViewModel.badgeLiveData.value is Success<*>) {
            (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.getBenefitCta(buttonType)
        } else {
            null
        }
    }

    companion object {

        private const val ANIMATION_DURATION = 300L
        private const val ROTATION_DURATION = 5000L
        private const val ANIMATION_INITIAL_DELAY = 400L
        private const val ASSET_TOTAL_COUNT = 6
        private const val MDPI_SCREEN_SIZE = 5.0

        // UI States
        const val LOADING_STATE = 0
        const val HAPPY_STATE = 1
        const val ERROR_STATE = 2

        const val TAG = "SCP_CELEBRATION_BOTTOM_SHEET"
        const val BUNDLE_SCP_MEDALI_SLUG = "bundleScpCelebrationBottomSheetSlug"

        fun show(
            childFragmentManager: FragmentManager,
            medaliSlug: String
        ) {
            val bundle = Bundle()
            bundle.putString(BUNDLE_SCP_MEDALI_SLUG, medaliSlug)
            val tokomemberIntroBottomsheet = MedalCelebrationBottomSheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }
    }
}
