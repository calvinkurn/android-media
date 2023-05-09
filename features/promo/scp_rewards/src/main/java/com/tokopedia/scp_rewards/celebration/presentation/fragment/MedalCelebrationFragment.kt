package com.tokopedia.scp_rewards.celebration.presentation.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.PathInterpolator
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
import com.tokopedia.scp_rewards.celebration.di.CelebrationComponent
import com.tokopedia.scp_rewards.celebration.domain.model.ScpRewardsCelebrationModel
import com.tokopedia.scp_rewards.celebration.presentation.viewmodel.MedalCelebrationViewModel
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.AudioFactory
import com.tokopedia.scp_rewards.common.utils.EASE_IN
import com.tokopedia.scp_rewards.common.utils.dpToPx
import com.tokopedia.scp_rewards.databinding.CelebrationFragmentLayoutBinding
import com.tokopedia.unifycomponents.ImageUnify
import javax.inject.Inject

@SuppressLint("DeprecatedMethod")
class MedalCelebrationFragment : BaseDaggerFragment() {

    companion object{
        private const val ANIMATION_DURATION = 300L
        private const val ROTATION_DURATION = 5000L
        private const val ANIMATION_INITIAL_DELAY = 400L
        private const val ANIMATION_REDIRECT_DELAY = 2000L
        private const val CONFETTI_URL = "https://json.extendsclass.com/bin/8c8a50287360"
        private const val STARS_URL = "https://json.extendsclass.com/bin/8124418c86cf"
        private const val ASSET_TOTAL_COUNT = 5
        private const val SOUND_EFFECT_URL = "https://res.cloudinary.com/dv9upuqs7/video/upload/v1683464110/samples/people/GoTo_Medali_-_SFX_FINAL_lgskby.mp3"
    }

    //Assets
    private var badgeImage:Drawable? = null
    private var spotlightImage:Drawable? = null
    private var sunburstImage:Drawable? = null
    private var backgroundImage:Drawable? = null
    private var celebrationLottieComposition:LottieComposition? = null
    private var starsLottieComposition:LottieComposition? = null
    private var isBackgroundImageAvailable = false
    private var isBackgroundLoading = false
    private var isSoundAvailable = false
    private var isSoundLoading = false
    private val audioManager = AudioFactory()

    //urls
    private var badgeUrl = ""
    private var sunflareUrl = ""
    private var spotlightUrl = ""
    private var celebrationLottieUrl = ""
    private var starsLottieUrl = ""
    private var soundaEffectUrl = ""
    private var backgroundImageUrl = ""



    private var binding: CelebrationFragmentLayoutBinding?=null
    private val handler = Handler(Looper.getMainLooper())

    private var assetCount = 0

    @Inject
    @JvmField
    var viewModelFactory:ViewModelFactory? = null

    private val medalCelebrationViewModel by lazy {
        ViewModelProvider(this,viewModelFactory!!).get(MedalCelebrationViewModel::class.java)
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
        binding = CelebrationFragmentLayoutBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding?.mainFlipper?.displayedChild = 0
//        downloadAssets()
        setupViewModelObservers()
        medalCelebrationViewModel.getRewards()
    }

    private fun setupViewModelObservers(){
        medalCelebrationViewModel.badgeLiveData.observe(viewLifecycleOwner){
            when(it){
                is Success<*> -> {
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

    private fun initViewSetup(){
        binding?.mainView?.apply {
            celebrationHeading.alpha = 0f
            badgeName.alpha = 0f
            badgeDescription.alpha = 0f
            sunflare.alpha = 0f
            sponsorCard.alpha = 0f
            spotlight.alpha = 0f
            celebrationView.setImageDrawable(null)
            backgroundImage.setImageDrawable(null)
            setAllText()
            configureBackground()
        }
    }


    private fun setAllText(){
       binding?.mainView?.apply {
           (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
               celebrationHeading.text = scpRewardsCelebrationPage?.celebrationPage?.title
               badgeName.text = scpRewardsCelebrationPage?.celebrationPage?.medaliName
               brandTag.text = scpRewardsCelebrationPage?.celebrationPage?.medaliSourceText
               badgeDescription.text = scpRewardsCelebrationPage?.celebrationPage?.medaliDescription
           }
       }
    }

    private fun configureBackground(){
        (medalCelebrationViewModel.badgeLiveData.value as Success<ScpRewardsCelebrationModel>).data.apply {
            binding?.mainView?.apply {
                val color = scpRewardsCelebrationPage?.celebrationPage?.backgroundColor
                if(!color.isNullOrBlank()){
                    container.setBackgroundColor(Color.parseColor(color))
                }
                if(isBackgroundImageAvailable){
                    backgroundImage.setImageDrawable(this@MedalCelebrationFragment.backgroundImage)
                }
            }
        }
    }

    private fun setupHeader(){
        binding?.mainView?.closeBtn?.setOnClickListener {
            activity?.finish()
        }
    }


//    private fun animateBadge(){
//        val badgeDrawable = ResourcesCompat.getDrawable(resources,R.drawable.category_medali,null)
//        binding?.badgeImage?.setImageDrawable(badgeDrawable)
//        scaleView(
//          view = binding?.badgeImage,
//            duration = ANIMATION_DURATION,
//            interpolatorType = EASE_IN
//        )
//        loadSpotLight()
//        loadSunflare()
//        animateRest()
//        handler.postDelayed({
//            showCelebrationConfetti()
//        }, ANIMATION_DURATION)
//    }

    private fun animateRest(){
        binding?.mainView?.apply {
            context?.let{
                translateView(
                    view = celebrationHeading,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it,-53).toInt(),
                    interpolatorType = EASE_IN
                )
                translateView(
                    view = badgeName,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it,40).toInt(),
                    interpolatorType = EASE_IN
                )
                translateView(
                    view = sponsorCard,
                    duration  = ANIMATION_DURATION,
                    from = dpToPx(it,44).toInt(),
                    interpolatorType = EASE_IN
                )
                translateView(
                    view = badgeDescription,
                    duration = ANIMATION_DURATION,
                    from = dpToPx(it,40).toInt(),
                    interpolatorType = EASE_IN
                )
            }
        }
    }

    private fun scaleView(view:View?,duration:Long,from:Int = 0,to:Int = 1,interpolatorType:Int,listener:Animator.AnimatorListener?=null){
        view?.let { _ ->
            val scaleXPvh = getScaleXPropertyValueHolder()
            val scaleYPvh = getScaleYPropertyValueHolder()
            val opacityPvh = getOpacityPropertyValueHolder()
            ObjectAnimator.ofPropertyValuesHolder(view,scaleXPvh,scaleYPvh,opacityPvh).apply {
                this.duration = duration
                interpolator = when(interpolatorType){
                    EASE_IN -> AccelerateDecelerateInterpolator()
                    else -> PathInterpolator(0.63f,0.01f,0.29f,1f)
                }
                addListener()
                listener?.let {
                    addListener(it)
                }
                start()
            }
        }
    }

    private fun translateView(view:View?,duration:Long,from:Int,to:Int = 0,interpolatorType:Int){
        view?.let { _ ->
            val translatePvh = getTranslationPropertyValueHolder(from,to)
            val opacityPvh = getOpacityPropertyValueHolder()
            ObjectAnimator.ofPropertyValuesHolder(view,translatePvh,opacityPvh).apply {
//                   addUpdateListener {
//                       val opacity = getAnimatedValue("opacity") as Float
//                       val translationY = getAnimatedValue("translationY") as Float
//                       animatedView.alpha = opacity
//                       animatedView.translationY = translationY
//                   }
                   this.duration = duration
                   interpolator = when(interpolatorType){
                       EASE_IN -> AccelerateDecelerateInterpolator()
                       else -> PathInterpolator(0.63f,0.01f,0.29f,1f)
                   }
                   start()
            }
        }
    }

    private fun rotateSunflare(){
        binding?.mainView?.sunflare?.apply {
            ObjectAnimator.ofFloat(this,"rotation",0f,360f).apply {
                duration = ROTATION_DURATION
                interpolator = null
                repeatCount = ValueAnimator.INFINITE
                start()
            }
        }
    }

    private fun getTranslationPropertyValueHolder(from:Int,to:Int = 0) = PropertyValuesHolder.ofFloat(
        "translationY",
        from.toFloat(),
        to.toFloat()
    )

    private fun getOpacityPropertyValueHolder(from:Int = 0,to:Int = 255) = PropertyValuesHolder.ofFloat(
        "alpha",
        from.toFloat(),
        to.toFloat()
    )

    private fun getScaleXPropertyValueHolder(from: Int = 0 ,to:Int = 1) = PropertyValuesHolder.ofFloat(
        "scaleX",
        from.toFloat(),
        to.toFloat()
    )

    private fun getScaleYPropertyValueHolder(from: Int = 0 ,to:Int = 1) = PropertyValuesHolder.ofFloat(
        "scaleY",
        from.toFloat(),
        to.toFloat()
    )

    private fun loadLottieFromUrl(view:LottieAnimationView?,url:String,success:(composition:LottieComposition) -> Unit,error:(() -> Unit)? = null){
        view?.let {
            context?.let{ it2 ->
                val lottieCompositionTask = LottieCompositionFactory.fromUrl(it2,url)
                lottieCompositionTask.addListener { composition ->
                    success.invoke(composition)
                }
                lottieCompositionTask.addFailureListener {
                    error?.invoke()
                }
            }
        }
    }

    private fun loadImageFromUrl(view:ImageUnify?,url:String,success:(drawable:Drawable) -> Unit,error:(() -> Unit)? = null){
        context?.let {
            Glide.with(it)
                .load(url)
                .into(object : CustomTarget<Drawable>(){
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

//    private fun animateSunflare(){
//        val drawable = ResourcesCompat.getDrawable(resources,R.drawable.sunburst,null)
//        binding?.sunflare?.setImageDrawable(drawable)
//        val listener = object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {}
//            override fun onAnimationEnd(animation: Animator) {
//              rotateSunflare()
//            }
//            override fun onAnimationCancel(animation: Animator) {}
//            override fun onAnimationRepeat(animation: Animator) {}
//        }
//        scaleView(
//            view = binding?.sunflare,
//            duration = ANIMATION_DURATION,
//            interpolatorType = EASE_IN,
//            listener = listener
//        )
//    }

    private fun downloadAssets(){
        setAssetUrls()
        loadSound()
        loadBadge()
        loadSunflare()
        loadSpotlight()
        loadBackgroundImage()
        loadCelebrationLottie()
        loadStarsLottie()
    }

    private fun setAssetUrls(){
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

    private fun loadBadge(){
        loadImageFromUrl(binding?.mainView?.badgeImage,badgeUrl ,{
            badgeImage = it
            incrementAssetCount()
        }){
            redirectToMedaliDetail()
        }
    }

    private fun loadSpotlight(){
        loadImageFromUrl(binding?.mainView?.spotlight, spotlightUrl,{
            spotlightImage = it
            incrementAssetCount()
        }){
            redirectToMedaliDetail()
        }
    }

    private fun loadSunflare(){
        loadImageFromUrl(binding?.mainView?.sunflare, sunflareUrl,{
            sunburstImage = it
            incrementAssetCount()
        }){
            redirectToMedaliDetail()
        }
    }

    private fun loadCelebrationLottie(){
        loadLottieFromUrl(binding?.mainView?.celebrationView,celebrationLottieUrl,{
            celebrationLottieComposition = it
            incrementAssetCount()
        }){
            redirectToMedaliDetail()
        }
    }

    private fun loadStarsLottie(){
        loadLottieFromUrl(binding?.mainView?.lottieStars, starsLottieUrl,{
            starsLottieComposition = it
            incrementAssetCount()
        }){
            redirectToMedaliDetail()
        }
    }

    private fun loadSound(){
        isSoundLoading = true
        audioManager.createAudioFromUrl(
            url = SOUND_EFFECT_URL,
            loop = true,
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
        )
    }

    private fun loadBackgroundImage(){
        isBackgroundLoading = true
        loadImageFromUrl(binding?.mainView?.backgroundImage,backgroundImageUrl,{
            isBackgroundLoading = false
            isBackgroundImageAvailable = true
            backgroundImage = it
            incrementAssetCount(mandatory = false)
        },{
            isBackgroundLoading = false
            isBackgroundImageAvailable = false
            incrementAssetCount(false)
        })
    }

    private fun incrementAssetCount(mandatory:Boolean = true){
        if(mandatory) assetCount++
        if(assetCount == ASSET_TOTAL_COUNT && !isSoundLoading && !isBackgroundLoading){
            showMainView()
        }
    }

    private fun startPageAnimation(){
        animateBadge()
        animateSunflare()
        animateSpotlight()
        animateRest()
        playSound()
        handler.postDelayed(
            {
                showCelebrationConfetti()
                showStarsConfetti()
                handler.postDelayed({
                    fadeOutConfetti()
                }, ANIMATION_REDIRECT_DELAY)
            },
            ANIMATION_DURATION
        )
    }

    private fun animateSpotlight(){
        binding?.mainView?.spotlight?.setImageDrawable(spotlightImage)
        scaleView(
            view = binding?.mainView?.spotlight,
            duration = ANIMATION_DURATION,
            interpolatorType = EASE_IN
        )
    }

    private fun animateSunflare(){
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

    private fun animateBadge(){
        binding?.mainView?.badgeImage?.setImageDrawable(badgeImage)
        scaleView(
            view = binding?.mainView?.badgeImage,
            duration = ANIMATION_DURATION,
            interpolatorType = EASE_IN
        )
    }

    private fun showCelebrationConfetti(){
        celebrationLottieComposition?.let {
            binding?.mainView?.celebrationView?.apply {
                setComposition(it)
                playAnimation()
                loop(true)
            }
        }
    }

    private fun showStarsConfetti(){
        starsLottieComposition?.let {
            binding?.mainView?.lottieStars?.apply {
                setComposition(it)
                playAnimation()
                loop(true)
            }
        }
    }

    private fun playSound(){
        if(isSoundAvailable)
          audioManager.playAudio()
    }

    private fun showMainView(){
        binding?.mainFlipper?.displayedChild = 1
        configureStatusBar()
        setupHeader()
        initViewSetup()
        handler.postDelayed({
            startPageAnimation()
        }, ANIMATION_INITIAL_DELAY
        )
    }

//    private fun showLoader(){
//        binding?.loader?.visibility = View.VISIBLE
//        hideMainView()
//    }
//
//    private fun hideLoader(){
//        binding?.loader?.visibility = View.GONE
//    }
//
//    private fun showMainView(){
//        hideLoader()
//        binding?.mainView?.visibility = View.VISIBLE
//    }
//
//    private fun hideMainView(){
//        binding?.mainView?.visibility = View.GONE
//    }


    private fun configureStatusBar(){
        activity?.let {
            it.window.apply {
                binding?.mainView?.statusBarBg?.layoutParams?.height = getStatusBarHeight()
                setStatusBarColor()
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    private fun getStatusBarHeight() : Int{
        var height = 0
        context?.let {
            val resId = it.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resId > 0) {
                height = it.resources.getDimensionPixelSize(resId)
            }
        }
        return height
    }

    private fun setStatusBarColor(){
        context?.let {
            binding?.mainView?.statusBarBg?.setBackgroundColor(ContextCompat.getColor(it,com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        }
    }

    private fun showErrorView(){
        binding?.mainFlipper?.displayedChild = 2
        binding?.errorView?.apply {
            errorSecondaryAction.text = context?.getString(R.string.go_back_text)
            errorSecondaryAction.setTextColor(ContextCompat.getColor(context,R.color.dark_grey_nav_color))
            setActionClickListener { resetPage() }
            setSecondaryActionClickListener { activity?.finish() }
        }
    }

    private fun resetPage(){
        binding?.mainFlipper?.displayedChild = 0
        medalCelebrationViewModel.getRewards()
    }

    private fun fadeOutConfetti(){
        binding?.mainView?.celebrationView?.apply {
            val opacityPvh = getOpacityPropertyValueHolder(from = 255,to = 0)
            ObjectAnimator.ofPropertyValuesHolder(this,opacityPvh).apply {
                duration = ANIMATION_DURATION
                interpolator = LinearInterpolator()
                addListener(object : Animator.AnimatorListener{
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

    private fun redirectToMedaliDetail(){
        Toast.makeText(context,"redirecting to medali detail",Toast.LENGTH_LONG).show()
        activity?.finish()
//        handler.postDelayed({
//            activity?.finish()
//        },300)
    }

    override fun onStop() {
        super.onStop()
        binding?.mainView?.apply {
            celebrationView.clearAnimation()
            lottieStars.clearAnimation()
        }
        audioManager.releaseMediaPlayer()
    }


    private fun getStringResourceByName(aString: String): Int? {
        return resources.getIdentifier(aString, "color", context?.packageName)
    }

}
