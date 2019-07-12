package com.tokopedia.onboarding.fragment

import android.animation.AnimatorSet
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.util.getParamInt
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.onboarding.OnboardingActivity
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.animation.OnboardingAnimationHelper
import com.tokopedia.onboarding.di.DaggerOnboardingComponent
import com.tokopedia.onboarding.listener.CustomAnimationPageTransformerDelegate
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject


/**
 * @author by stevenfredian on 14/05/19.
 */
class
OnboardingFragment : BaseDaggerFragment(),
        CustomAnimationPageTransformerDelegate, OnboardingActivity.onBoardingFirsbaseCallBack {

    companion object {
        val VIEW_DEFAULT = 100
        val VIEW_ENDING = 101

        val ARG_TITLE = "title"
        val ARG_LOTTIE = "lottie"
        val ARG_DESC = "desc"
        val ARG_BG_COLOR = "bg_color"
        val ARG_VIEW_TYPE = "view_type"
        val ARG_POSITION = "position"
        val ARG_DESCKEY = "desc_key"
        val ARG_TTLKEY = "ttl_key"

        fun createInstance(title: String = "",
                           description: String = "",
                           lottieAsset: Int = 0,
                           bgColor: Int = 0,
                           position: Int = 0,
                           ttlKey: String = "",
                           descKey: String = ""): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putCharSequence(ARG_TITLE, title)
            args.putCharSequence(ARG_DESC, description)
            args.putInt(ARG_LOTTIE, lottieAsset)
            args.putInt(ARG_BG_COLOR, bgColor)
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_TTLKEY, ttlKey)
            args.putString(ARG_DESCKEY, descKey)
            fragment.arguments = args
            return fragment
        }
    }

    var title: String = ""
    var description: String = ""
    var lottieAsset: Int = 0
    var bgColor: Int = 0
    var position: Int = 0
    var isAnimationPlayed = false
    private var descKey: String = ""
    private var ttlKey: String = ""
    private var remoteConfig: RemoteConfig? = null

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
        lottieAsset = getParamInt(ARG_LOTTIE, arguments, savedInstanceState, 0)
        bgColor = getParamInt(ARG_BG_COLOR, arguments, savedInstanceState, 0)
        position = getParamInt(ARG_POSITION, arguments, savedInstanceState, 0)
        descKey = getParamString(ARG_DESCKEY, arguments, savedInstanceState, "")
        ttlKey = getParamString(ARG_TTLKEY, arguments, savedInstanceState, "")

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
        val main: View = defaultView.findViewById(R.id.main)
        main.setBackgroundColor(bgColor)

        setAnimation(defaultView)

        titleView = defaultView.findViewById(R.id.title)
        descView = defaultView.findViewById(R.id.description)

        titleView.text = MethodChecker.fromHtml(getTitleMsg())
        descView.text = MethodChecker.fromHtml(getDescMsg())

        return defaultView
    }

    private fun loadJSONFromAsset(asset: String): String {
        val json: String?
        try {
            val `is` = activity!!.assets.open(asset)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }

        return json
    }

    private fun setAnimation(defaultView: View) {
        try {
            lottieAnimationView = defaultView.findViewById(R.id.animation_view)
            if (lottieAsset != 0) {
                lottieAnimationView.setAnimation(lottieAsset)
            } else if (!GlobalConfig.DEBUG) {
                Crashlytics.log("Lottie Asset Is Blank")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun getTitleMsg(): String {
        val ttlMsg = getRemoteConfig().getString(ttlKey)
        return if (TextUtils.isEmpty(ttlMsg)) {
            title
        } else {
            ttlMsg
        }
    }

    private fun getDescMsg(): String {
        val descMsg = getRemoteConfig().getString(descKey)
        return if (TextUtils.isEmpty(descMsg)) {
            description
        } else {
            descMsg
        }
    }

    private fun getRemoteConfig(): RemoteConfig {
        if (remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(activity)
        }
        return remoteConfig as RemoteConfig
    }

    override fun onResponse(remoteConfig: RemoteConfig) {
        if (remoteConfig != null) {
            var msg = remoteConfig.getString(descKey)
            if (!TextUtils.isEmpty(msg)) {
                val descTxt = msg
                activity?.runOnUiThread(object : Runnable {
                    override fun run() {
                        descView.text = MethodChecker.fromHtml(descTxt)
                    }
                })
            }
            msg = remoteConfig.getString(ttlKey)
            if (!TextUtils.isEmpty(msg)) {
                val ttlTxt = msg
                activity?.runOnUiThread(object : Runnable {
                    override fun run() {
                        titleView.text = MethodChecker.fromHtml(ttlTxt)
                    }
                })
            }
        }
    }
}