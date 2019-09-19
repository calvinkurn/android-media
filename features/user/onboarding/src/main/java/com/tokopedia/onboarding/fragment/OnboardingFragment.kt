package com.tokopedia.onboarding.fragment

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
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
import com.tokopedia.onboarding.di.DaggerOnboardingComponent
import com.tokopedia.onboarding.listener.OnboardingVideoListener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by stevenfredian on 14/05/19.
 */
class OnboardingFragment : BaseDaggerFragment(),
        OnboardingActivity.onBoardingFirsbaseCallBack,
        OnboardingVideoListener {

    companion object {

        val ARG_TITLE = "title"
        val ARG_VIDEO_PATH = "video_path"
        val ARG_DESC = "desc"
        val ARG_BG_COLOR = "bg_color"
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

    private var isVideoPrepared: Boolean = false

    @Inject
    lateinit var userSession: UserSessionInterface

    var videoView: VideoView? = null
    var titleView: TextView? = null
    var descView: TextView? = null
    var main: View? = null

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
        val defaultView: View = inflater.inflate(R.layout.base_onboarding_video_fragment, container,
                false)

        videoView = defaultView.findViewById(R.id.video_view)
        titleView = defaultView.findViewById(R.id.title)
        descView = defaultView.findViewById(R.id.description)

        titleView?.text = MethodChecker.fromHtml(getTitleMsg())
        descView?.text = MethodChecker.fromHtml(getDescMsg())

        videoView?.setZOrderOnTop(true)
        videoView?.setVideoURI(Uri.parse(videoPath))
        videoView?.setOnErrorListener { _, _, _ -> true }

        videoView?.setOnPreparedListener(onPreparedListener)

        videoView?.requestFocus();

        return defaultView
    }

    var onPreparedListener: MediaPlayer.OnPreparedListener = MediaPlayer.OnPreparedListener { m ->
        var mediaPlayer = m
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
                mediaPlayer = MediaPlayer()
            }
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            isVideoPrepared = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        videoView?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.stopPlayback()
        videoView = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        view.tag = this
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPageSelected(position: Int) {
        try {
            videoView?.let {
                if (!it.isPlaying && isVideoPrepared) {
                    videoView?.start()
                    isVideoPrepared = false
                }
            }
        } catch (e: Exception) {}
    }

    override fun onPageUnSelected() {
        try {
            videoView?.let {
                if (it.isPlaying && isVideoPrepared) {
                    it.pause()
                    isVideoPrepared = false
                }
            }
        } catch (e: Exception) {}
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
            activity?.runOnUiThread { descView?.text = MethodChecker.fromHtml(descTxt) }
        }
        msg = remoteConfig.getString(ttlKey)
        if (!TextUtils.isEmpty(msg)) {
            val ttlTxt = msg
            activity?.runOnUiThread { titleView?.text = MethodChecker.fromHtml(ttlTxt) }
        }
    }
}