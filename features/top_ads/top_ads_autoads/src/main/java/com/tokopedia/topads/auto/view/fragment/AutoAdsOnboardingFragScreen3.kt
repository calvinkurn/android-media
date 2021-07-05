package com.tokopedia.topads.auto.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import kotlinx.android.synthetic.main.topads_autoads_onboarding_fragment3_layout.*
import java.util.zip.ZipInputStream

/**
 * Created by Pika on 27/5/20.
 */

class AutoAdsOnboardingFragScreen3 : BaseDaggerFragment() {

    override fun getScreenName(): String {
        return AutoAdsOnboardingFragScreen3::class.java.name
    }

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_autoads_onboarding_fragment3_layout), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lottieFileZipStream = ZipInputStream(context?.assets?.open(ANIMATION_JSON))
        val task = LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
        addLottieAnimationToView(task)

    }

    private fun addLottieAnimationToView(task: LottieTask<LottieComposition>) {
        task.addListener { result: LottieComposition? ->
            result?.let {
                lottie_animation_view?.setComposition(result)
                lottie_animation_view?.repeatMode = LottieDrawable.RESTART
                lottie_animation_view?.repeatCount = LottieDrawable.INFINITE
                lottie_animation_view?.playAnimation()
            }
        }
    }
    companion object {
        private const val ANIMATION_JSON = "animation_lottie3.zip"
    }
}