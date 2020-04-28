package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.ERR_STATE_SOCKET
import com.tokopedia.play.ERR_STATE_VIDEO
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.BufferTrackingModel
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.analytic.TrackingField
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.isAnyBottomSheetsShown
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.util.PlaySensorOrientationManager
import com.tokopedia.play.util.keyboard.KeyboardWatcher
import com.tokopedia.play.view.contract.PlayNewChannelInteractor
import com.tokopedia.play.view.layout.parent.PlayParentLayoutManager
import com.tokopedia.play.view.layout.parent.PlayParentLayoutManagerImpl
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.GlobalErrorCodeWrapper
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_play.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment : BaseDaggerFragment(), PlaySensorOrientationManager.OrientationListener {

    companion object {
        private const val TOP_BOUNDS_LANDSCAPE_VIDEO = "top_bounds_landscape_video"

        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"

        private const val VIDEO_FRAGMENT_TAG = "FRAGMENT_VIDEO"
        private const val INTERACTION_FRAGMENT_TAG = "FRAGMENT_INTERACTION"
        private const val BOTTOM_SHEET_FRAGMENT_TAG = "FRAGMENT_BOTTOM_SHEET"
        private const val YOUTUBE_FRAGMENT_TAG = "FRAGMENT_YOUTUBE"
        private const val ERROR_FRAGMENT_TAG = "FRAGMENT_ERROR"

        fun newInstance(channelId: String?): PlayFragment {
            return PlayFragment().apply {
                val args = Bundle()
                args.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = args
            }
        }
    }

    private var channelId = ""

    private var topBounds: Int? = null

    @TrackingField
    private var bufferTrackingModel = BufferTrackingModel(
            isBuffering = false,
            bufferCount = 0,
            lastBufferMs = System.currentTimeMillis(),
            shouldTrackNext = false
    )

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var playViewModel: PlayViewModel

    private lateinit var ivClose: ImageView
    private lateinit var flVideo: FrameLayout
    private lateinit var flInteraction: FrameLayout
    private lateinit var flBottomSheet: FrameLayout
    private lateinit var flYouTube: FrameLayout
    private lateinit var flGlobalError: FrameLayout

    private lateinit var layoutManager: PlayParentLayoutManager

    private lateinit var videoFragment: PlayVideoFragment

    private val keyboardWatcher = KeyboardWatcher()

    private lateinit var orientationManager: PlaySensorOrientationManager

    private var isChangingOrientation = false

    private var requestedOrientation: Int
        get() = requireActivity().requestedOrientation
        set(value) {
            requireActivity().requestedOrientation = value
        }

    override fun getScreenName(): String = "Play"

    override fun initInjector() {
        DaggerPlayComponent
                .builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .playModule(PlayModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(this, viewModelFactory).get(PlayViewModel::class.java)
        channelId = arguments?.getString(PLAY_KEY_CHANNEL_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedInstanceState?.containsKey(TOP_BOUNDS_LANDSCAPE_VIDEO) == true) {
            topBounds = savedInstanceState.getInt(TOP_BOUNDS_LANDSCAPE_VIDEO, 0)
        }

        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOrientation()
        initView(view)
        setupView(view)
        setupScreen(view)

        val fragmentVideo = childFragmentManager.findFragmentByTag(VIDEO_FRAGMENT_TAG)
        videoFragment = if (fragmentVideo == null) {
            val videoFragment = PlayVideoFragment.newInstance(channelId)
            childFragmentManager.beginTransaction()
                    .replace(flVideo.id, videoFragment, VIDEO_FRAGMENT_TAG)
                    .commit()

            videoFragment
        } else {
            fragmentVideo as PlayVideoFragment
        }

        if (childFragmentManager.findFragmentByTag(INTERACTION_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flInteraction.id, PlayInteractionFragment.newInstance(channelId), INTERACTION_FRAGMENT_TAG)
                    .commit()
        }

        if (childFragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flBottomSheet.id, PlayBottomSheetFragment.newInstance(channelId), BOTTOM_SHEET_FRAGMENT_TAG)
                    .commit()
        }

        if (childFragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flYouTube.id, PlayYouTubeFragment.newInstance(), YOUTUBE_FRAGMENT_TAG)
                    .commit()
        }

        if (childFragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flGlobalError.id, PlayErrorFragment.newInstance(channelId), ERROR_FRAGMENT_TAG)
                    .commit()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeGetChannelInfo()
        observeSocketInfo()
        observeEventUserInfo()
        observeVideoProperty()
        observeVideoStream()
    }

    override fun onResume() {
        super.onResume()
        if (!isChangingOrientation) playViewModel.resumeWithChannelId(channelId)
        requireView().post {
            registerKeyboardListener(requireView())
        }
    }

    override fun onPause() {
        unregisterKeyboardListener(requireView())
        super.onPause()

        bufferTrackingModel = bufferTrackingModel.copy(
                isBuffering = false,
                bufferCount = if (bufferTrackingModel.isBuffering) bufferTrackingModel.bufferCount - 1 else bufferTrackingModel.bufferCount,
                shouldTrackNext = false
        )
    }

    override fun onDestroyView() {
        destroyInsets(requireView())
        super.onDestroyView()
        if (::orientationManager.isInitialized) orientationManager.disable()
        layoutManager.onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::layoutManager.isInitialized) layoutManager.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        topBounds?.let { outState.putInt(TOP_BOUNDS_LANDSCAPE_VIDEO, it) }
        super.onSaveInstanceState(outState)
    }

    override fun onOrientationChanged(screenOrientation: ScreenOrientation) {
        val event = playViewModel.observableEvent.value
        val videoOrientation = playViewModel.videoOrientation
        val isFreezeOrBanned = (event?.isFreeze ?: false) || (event?.isBanned ?: false)
        if (requestedOrientation != screenOrientation.requestedOrientation && !isFreezeOrBanned && videoOrientation.isHorizontal)
            requestedOrientation = screenOrientation.requestedOrientation
        sendTrackerWhenRotateScreen(screenOrientation)
    }

    fun onBottomInsetsViewShown(bottomMostBounds: Int) {
        layoutManager.onBottomInsetsShown(requireView(), bottomMostBounds, playViewModel.videoPlayer, playViewModel.videoOrientation)
    }

    fun onBottomInsetsViewHidden() {
        layoutManager.onBottomInsetsHidden(requireView(), playViewModel.videoPlayer)
    }

    fun setResultBeforeFinish() {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            val totalView = playViewModel.totalView
            if (!totalView.isNullOrEmpty()) putExtra(EXTRA_TOTAL_VIEW, totalView)
        })
    }

    fun setVideoTopBounds(videoPlayer: VideoPlayerUiModel, videoOrientation: VideoOrientation, topBounds: Int) {
        if (this.topBounds == null && topBounds > 0) {
            this.topBounds = topBounds
        }
        this.topBounds?.let { layoutManager.onVideoTopBoundsChanged(requireView(), videoPlayer, videoOrientation, it) }
    }

    /**
     * @return true means the onBackPressed() has been handled by this fragment
     */
    fun onBackPressed(): Boolean {
        val isHandled = playViewModel.onBackPressed()
        return when {
            isHandled -> isHandled
            playViewModel.screenOrientation.isLandscape -> {
                requestedOrientation = ScreenOrientation.Portrait.requestedOrientation
                true
            }
            else -> false
        }
    }

    fun onNewChannelId(channelId: String?) {
        if (this.channelId != channelId && activity is PlayNewChannelInteractor) {
            (activity as PlayNewChannelInteractor).onNewChannel(channelId)
        }
    }

    private fun destroyInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(requireView(), null)
    }

    private fun setOrientation() {
        orientationManager = PlaySensorOrientationManager(requireContext(), this)
        orientationManager.enable()

        val screenOrientation = ScreenOrientation.getByInt(resources.configuration.orientation)
        isChangingOrientation = (playViewModel.screenOrientation != ScreenOrientation.Unknown) && (playViewModel.screenOrientation != screenOrientation)
        playViewModel.setScreenOrientation(screenOrientation)
    }

    private fun initView(view: View) {
        with (view) {
            ivClose = findViewById(R.id.iv_close)
            flVideo = findViewById(R.id.fl_video)
            flInteraction = findViewById(R.id.fl_interaction)
            flBottomSheet = findViewById(R.id.fl_bottom_sheet)
            flYouTube = findViewById(R.id.fl_youtube)
            flGlobalError = findViewById(R.id.fl_global_error)
        }

        layoutManager = PlayParentLayoutManagerImpl(
                context = requireContext(),
                screenOrientation = playViewModel.screenOrientation,
                ivClose = ivClose,
                flVideo = flVideo,
                flInteraction = flInteraction,
                flBottomSheet = flBottomSheet,
                flYouTube = flYouTube,
                flGlobalError = flGlobalError
        )

        topBounds?.let { setVideoTopBounds(playViewModel.videoPlayer, playViewModel.videoOrientation, it) }
    }

    private fun setupView(view: View) {
        ivClose.setOnClickListener {
            hideKeyboard()
        }
        flVideo.setOnClickListener {
            //TODO("Figure out a better way")
            if (playViewModel.bottomInsets.isKeyboardShown) hideKeyboard()
            else hideAllInsets()
        }

        hideAllInsets()
    }

    private fun setupScreen(view: View) {
        setInsets(view)
    }

    private fun setInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->

            layoutManager.setupInsets(view, insets)
            insets
        }
    }

    private fun observeGetChannelInfo() {
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> PlayAnalytics.sendScreen(channelId, playViewModel.channelType)
                is Fail -> result.throwable.message?.let {
                    if (GlobalErrorCodeWrapper.wrap(it) != GlobalErrorCodeWrapper.Unknown) flGlobalError.show()
                }
            }
        })
    }

    private fun observeSocketInfo() {
        playViewModel.observableSocketInfo.observe(viewLifecycleOwner, Observer {
            when(it) {
                is PlaySocketInfo.Reconnect ->
                    PlayAnalytics.errorState(channelId, "$ERR_STATE_SOCKET: ${getString(R.string.play_message_socket_reconnect)}", playViewModel.channelType)
                is PlaySocketInfo.Error ->
                    PlayAnalytics.errorState(channelId, "$ERR_STATE_SOCKET: ${it.throwable.localizedMessage}", playViewModel.channelType)
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, Observer {
            if (playViewModel.screenOrientation.isLandscape && (it.isFreeze || it.isBanned)) {
                requestedOrientation = ScreenOrientation.Portrait.requestedOrientation
            } else {
                if (it.isFreeze)
                    try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
                else if (it.isBanned)
                    showEventDialog(it.bannedTitle, it.bannedMessage, it.bannedButtonTitle)
            }
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, Observer {
            if (it.state is PlayVideoState.Error) {
                PlayAnalytics.errorState(channelId,
                        "$ERR_STATE_VIDEO: ${it.state.error.message?:getString(com.tokopedia.play_common.R.string.play_common_video_error_message)}",
                        playViewModel.channelType)

            } else if (it.state is PlayVideoState.Buffering && !bufferTrackingModel.isBuffering) {
                val nextBufferCount = if (bufferTrackingModel.shouldTrackNext) bufferTrackingModel.bufferCount + 1 else bufferTrackingModel.bufferCount

                bufferTrackingModel = BufferTrackingModel(
                        isBuffering = true,
                        bufferCount = nextBufferCount,
                        lastBufferMs = System.currentTimeMillis(),
                        shouldTrackNext = bufferTrackingModel.shouldTrackNext
                )

            } else if ((it.state is PlayVideoState.Playing || it.state is PlayVideoState.Pause) && bufferTrackingModel.isBuffering) {
                if (bufferTrackingModel.shouldTrackNext) {
                    PlayAnalytics.trackVideoBuffering(
                            bufferCount = bufferTrackingModel.bufferCount,
                            bufferDurationInSecond = ((System.currentTimeMillis() - bufferTrackingModel.lastBufferMs) / 1000).toInt(),
                            userId = userSession.userId,
                            channelId = channelId,
                            channelType = playViewModel.channelType
                    )
                }

                bufferTrackingModel = bufferTrackingModel.copy(
                        isBuffering = false,
                        shouldTrackNext = true
                )

            }

            layoutManager.onVideoStateChanged(requireView(), it.state, playViewModel.videoOrientation)
        })
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, Observer {
            setWindowSoftInputMode(it.channelType.isLive)
            setBackground(it.backgroundUrl)

            layoutManager.onVideoStreamChanged(requireView())
        })
    }

    private fun showEventDialog(title: String, message: String, buttonTitle: String, buttonUrl: String = "") {
        activity?.let {
            val dialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(title)
            dialog.setDescription(message)
            dialog.setPrimaryCTAText(buttonTitle)
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                it.finish()
                if (buttonUrl.isNotEmpty()) RouteManager.route(it, buttonUrl)
            }
            dialog.setOverlayClose(false)
            dialog.show()
        }
    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        view?.let { v ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun setWindowSoftInputMode(isLive: Boolean) {
        requireActivity().window.setSoftInputMode(
                if (!isLive) WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
                else WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    private fun registerKeyboardListener(view: View) {
        keyboardWatcher.listen(view, object : KeyboardWatcher.Listener {
            override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                playViewModel.onKeyboardShown(estimatedKeyboardHeight)
                ivClose.visible()
            }

            override fun onKeyboardHidden() {
                playViewModel.onKeyboardHidden()
                ivClose.invisible()
                if (!playViewModel.stateHelper.bottomInsets.isAnyBottomSheetsShown) this@PlayFragment.onBottomInsetsViewHidden()
            }
        })
    }

    private fun unregisterKeyboardListener(view: View) {
        keyboardWatcher.unlisten(view)
    }

    private fun hideAllInsets() {
        hideKeyboard()
        playViewModel.hideInsets(isKeyboardHandled = true)
    }

    private fun sendTrackerWhenRotateScreen(screenOrientation: ScreenOrientation) {
        if (screenOrientation.isLandscape) {
            PlayAnalytics.userTiltFromPortraitToLandscape(
                    userId = userSession.userId,
                    channelId = channelId,
                    channelType = playViewModel.channelType)
        }
    }

    private fun setBackground(backgroundUrl: String) {
        Glide.with(requireContext()).load(backgroundUrl).into(object : CustomTarget<Drawable?>() {
            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                view?.background = resource
            }
        })
    }
}