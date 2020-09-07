package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_create_onboarding_fragment_layout.*
import java.util.zip.ZipInputStream
import javax.inject.Inject

/**
 * Created by Pika on 27/8/20.
 */

class CreationOnboardingFragScreen2 : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getScreenName(): String {
        return CreationOnboardingFragScreen2::class.java.name
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_create_onboarding_fragment_layout), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lottieFileZipStream = ZipInputStream(context?.assets?.open(ANIMATION_JSON))
        title?.text = getString(R.string.topads_create_onboarding_screen_title2)
        desc?.text = getString(R.string.topads_create_onboarding_screen_desc2)
        dotImage?.setImageDrawable(context?.getResDrawable(R.drawable.topads_indi_2))
        val task = LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
        addLottieAnimationToView(task)
    }



    private fun addLottieAnimationToView(task: LottieTask<LottieComposition>) {
        task.addListener { result: LottieComposition? ->
            result?.let {
                lottie_animation_view?.setComposition(result)
                lottie_animation_view?.repeatMode = LottieDrawable.RESTART
                lottie_animation_view?.repeatCount = LottieDrawable.INFINITE
                lottie_animation_view.playAnimation()
            }
        }
    }

    companion object{
        private const val ANIMATION_JSON = "animation_lottie1.zip"
        private const val ANIMATION_JSON2 = "topads_ani2.zip"
    }

}