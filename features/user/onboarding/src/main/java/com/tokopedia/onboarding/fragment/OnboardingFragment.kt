package com.tokopedia.onboarding.fragment

import android.animation.AnimatorSet
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
        val ARG_VIDEO_PATH = "video_path"
        val ARG_DESC = "desc"
        val ARG_BG_COLOR = "bg_color"
        val ARG_VIEW_TYPE = "view_type"
        val ARG_POSITION = "position"
        val ARG_DESCKEY = "desc_key"
        val ARG_TTLKEY = "ttl_key"

        fun createInstance(title: String = "",
                           description: String = "",
                           videoPath: String = "",
                           position: Int = 0,
                           ttlKey: String = "",
                           descKey: String = ""): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putCharSequence(ARG_TITLE, title)
            args.putCharSequence(ARG_DESC, description)
            args.putCharSequence(ARG_VIDEO_PATH, videoPath)
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_TTLKEY, ttlKey)
            args.putString(ARG_DESCKEY, descKey)
            fragment.arguments = args
            return fragment
        }
    }

    var title: String = ""
    var description: String = ""
    var bgColor: Int = 0
    var position: Int = 0
    var videoPath = ""

    private var descKey: String = ""
    private var ttlKey: String = ""
    private var remoteConfig: RemoteConfig? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    lateinit var videoView: VideoView
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
        videoPath = getParamString(ARG_VIDEO_PATH, arguments, savedInstanceState, "")
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

        videoView = defaultView.findViewById(R.id.video_view)
        titleView = defaultView.findViewById(R.id.title)
        descView = defaultView.findViewById(R.id.description)

        titleView.text = MethodChecker.fromHtml(getTitleMsg())
        descView.text = MethodChecker.fromHtml(getDescMsg())

        videoView.setVideoURI(Uri.parse(videoPath))

        return defaultView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        view.tag = this
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPageSelected() {
        playAnimationTitleDesc()
    }

    private fun playAnimationTitleDesc() {
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

        videoView.start()
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            videoView.start()
        }
    }

    override fun onPageScrolled(page: View, position: Float) {}

    override fun onPageInvisible(position: Float) {
        titleView.visibility = View.INVISIBLE
        descView.visibility = View.INVISIBLE
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
        var msg = remoteConfig.getString(descKey)
        if (!TextUtils.isEmpty(msg)) {
            val descTxt = msg
            activity?.runOnUiThread { descView.text = MethodChecker.fromHtml(descTxt) }
        }
        msg = remoteConfig.getString(ttlKey)
        if (!TextUtils.isEmpty(msg)) {
            val ttlTxt = msg
            activity?.runOnUiThread { titleView.text = MethodChecker.fromHtml(ttlTxt) }
        }
    }
}