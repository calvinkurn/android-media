package com.tokopedia.onboarding.fragment

import android.animation.AnimatorSet
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.util.getParamInt
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.animation.OnboardingAnimationHelper
import com.tokopedia.onboarding.di.DaggerOnboardingComponent
import com.tokopedia.onboarding.listener.CustomAnimationPageTransformerDelegate
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by stevenfredian on 14/05/19.
 */
class OnboardingFragment : BaseDaggerFragment(),
        CustomAnimationPageTransformerDelegate {

    companion object {
        val VIEW_DEFAULT = 100
        val VIEW_ENDING = 101

        val ARG_TITLE = "title"
        val ARG_LOTTIE = "lottie"
        val ARG_DESC = "desc"
        val ARG_BG_COLOR = "bg_color"
        val ARG_VIEW_TYPE = "view_type"
        val ARG_POSITION = "position"

        fun createInstance(title: String = "",
                           description: String = "",
                           lottieAsset: String = "",
                           bgColor: Int = 0,
                           position: Int = 0): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putCharSequence(ARG_TITLE, title)
            args.putCharSequence(ARG_DESC, description)
            args.putString(ARG_LOTTIE, lottieAsset)
            args.putInt(ARG_BG_COLOR, bgColor)
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    var title: String = ""
    var description: String = ""
    var lottieAsset: String = ""
    var bgColor: Int = 0
    var position: Int = 0
    var isAnimationPlayed = false

    @Inject
    lateinit var userSession: UserSessionInterface

    var animatorSet = AnimatorSet()

    lateinit var lottieAnimationView: LottieAnimationView
    lateinit var titleView: TextView
    lateinit var descView: TextView
    lateinit var main: View

    override fun getScreenName(): String {
       return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getParamString(ARG_TITLE, arguments, savedInstanceState, "")
        description = getParamString(ARG_DESC, arguments, savedInstanceState, "")
        lottieAsset = getParamString(ARG_LOTTIE, arguments, savedInstanceState, "")
        bgColor = getParamInt(ARG_BG_COLOR, arguments, savedInstanceState, 0)
        position = getParamInt(ARG_POSITION, arguments, savedInstanceState, 0)
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val onboardingComponent = DaggerOnboardingComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()

            onboardingComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? {
        val defaultView: View = inflater.inflate(R.layout.base_onboarding_fragment, container,
                false)
        val main :View = defaultView.findViewById(R.id.main)
        main.setBackgroundColor(bgColor)

        lottieAnimationView = defaultView.findViewById(R.id.animation_view)
        lottieAnimationView.setAnimation(lottieAsset, LottieAnimationView.CacheStrategy.Strong)

        titleView = defaultView.findViewById(R.id.title)
        descView = defaultView.findViewById(R.id.description)

        titleView.text = MethodChecker.fromHtml(title)
        descView.text = MethodChecker.fromHtml(description)

        return defaultView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        view.tag = this
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPageSelected() {
        if (!isAnimationPlayed) {
            isAnimationPlayed = true
            lottieAnimationView.playAnimation()
            clearAnimation()
            lottieAnimationView.visibility = View.VISIBLE
            playAnimation()
        }
    }

    fun clearAnimation() {
        if (animatorSet.isRunning) {
            animatorSet.cancel()
        }
    }

    fun playAnimation() {
        titleView.visibility = View.VISIBLE
        descView.visibility = View.VISIBLE

        val slideTitle = OnboardingAnimationHelper.slideReverseX(titleView)
        val slideDesc = OnboardingAnimationHelper.slideReverseX(descView)

        val fadeTitle = OnboardingAnimationHelper.appearText(titleView)
        val fadeDesc = OnboardingAnimationHelper.appearText(descView)
        slideDesc.startDelay = 100L
        val set = AnimatorSet()
        set.playTogether(slideTitle, slideDesc, fadeTitle, fadeDesc)
        set.duration = 1000L
        set.start()
    }

    override fun onPageScrolled(page: View, position: Float) {
        if (position < -1) {
            main.alpha = 0f

        } else if (position > 1) {
            main.alpha = 0f

            titleView.translationX = titleView.width * -position
            descView.translationX = descView.width * -position
            titleView.translationY = page.height * -position
            descView.translationY = page.height * -position
        }
    }

    override fun onPageInvisible(position: Float) {
        clearAnimation()
        lottieAnimationView.visibility = View.INVISIBLE
        titleView.visibility = View.INVISIBLE
        descView.visibility = View.INVISIBLE
        isAnimationPlayed = false
    }


}