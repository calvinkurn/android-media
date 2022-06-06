package com.tokopedia.topads.auto.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.*
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.user.session.UserSessionInterface
import java.util.zip.ZipInputStream
import javax.inject.Inject

/**
 * Created by Pika on 27/5/20.
 */

class AutoAdsOnboardingFragScreen1 : BaseDaggerFragment() {

    private var lottieAnimationView: LottieAnimationView? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getScreenName(): String {
        return AutoAdsOnboardingFragScreen1::class.java.name
    }

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view =
            inflater.inflate(resources.getLayout(R.layout.topads_autoads_onboarding_fragment1_layout),
                container, false)
        lottieAnimationView = view.findViewById(R.id.lottie_animation_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lottieFileZipStream = ZipInputStream(context?.assets?.open(ANIMATION_JSON))
        val task = LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
        addLottieAnimationToView(task)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsOpenOnboardingScreenEvent(userSession.isLoggedIn,
            userSession.userId)
    }

    private fun addLottieAnimationToView(task: LottieTask<LottieComposition>) {
        task.addListener { result: LottieComposition? ->
            result?.let {
                lottieAnimationView?.setComposition(result)
                lottieAnimationView?.repeatMode = LottieDrawable.RESTART
                lottieAnimationView?.repeatCount = LottieDrawable.INFINITE
                lottieAnimationView?.playAnimation()
            }
        }
    }

    companion object {
        private const val ANIMATION_JSON = "animation_lottie1.zip"
    }

}