package com.tokopedia.play.view.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setMargin
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
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.util.keyboard.KeyboardWatcher
import com.tokopedia.play.view.contract.PlayNewChannelInteractor
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_play.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment : BaseDaggerFragment() {

    companion object {
        const val ANIMATION_DURATION = 300L

        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"

        private val MARGIN_CHAT_VIDEO = 16f.dpToPx()

        private const val VIDEO_FRAGMENT_TAG = "FRAGMENT_VIDEO"
        private const val INTERACTION_FRAGMENT_TAG = "FRAGMENT_INTERACTION"
        private const val BOTTOM_SHEET_FRAGMENT_TAG = "FRAGMENT_INTERACTION"
        private const val ERROR_FRAGMENT_TAG = "FRAGMENT_ERROR"

        private const val FULL_SCALE_FACTOR = 1.0f

        fun newInstance(channelId: String?): PlayFragment {
            return PlayFragment().apply {
                val args = Bundle()
                args.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = args
            }
        }
    }

    private var channelId = ""

    @TrackingField
    private var bufferTrackingModel = BufferTrackingModel(
            isBuffering = false,
            bufferCount = 0,
            lastBufferMs = System.currentTimeMillis(),
            shouldTrackNext = false
    )

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val offset12 by lazy { resources.getDimensionPixelOffset(R.dimen.dp_12) }

    private val videoScaleAnimator = AnimatorSet()
    private val onBottomInsetsShownAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            flVideo.isClickable = true
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            flVideo.isClickable = false
        }
    }
    private val onBottomInsetsHiddenAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            flVideo.isClickable = false
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            flVideo.isClickable = false
        }
    }

    private lateinit var playViewModel: PlayViewModel

    private lateinit var ivClose: ImageView
    private lateinit var flVideo: FrameLayout
    private lateinit var flInteraction: FrameLayout
    private lateinit var flBottomSheet: FrameLayout
    private lateinit var flGlobalError: FrameLayout

    private val keyboardWatcher = KeyboardWatcher()

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
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupScreen(view)

        if (childFragmentManager.findFragmentByTag(VIDEO_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flVideo.id, PlayVideoFragment.newInstance(channelId), VIDEO_FRAGMENT_TAG)
                    .commit()
        }

        if (childFragmentManager.findFragmentByTag(INTERACTION_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(flInteraction.id, PlayInteractionFragment.newInstance(channelId), INTERACTION_FRAGMENT_TAG)
                    .commit()
        }

        if (childFragmentManager.findFragmentByTag(BOTTOM_SHEET_FRAGMENT_TAG) == null) {
            childFragmentManager.beginTransaction()
                    .replace(fl_bottom_sheet.id, PlayBottomSheetFragment.newInstance(channelId), BOTTOM_SHEET_FRAGMENT_TAG)
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
        playViewModel.resumeWithChannelId(channelId)
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

    fun onNewChannelId(channelId: String?) {
        if (this.channelId != channelId && activity is PlayNewChannelInteractor) {
            (activity as PlayNewChannelInteractor).onNewChannel(channelId)
        }
    }

    private fun initView(view: View) {
        with (view) {
            ivClose = findViewById(R.id.iv_close)
            flVideo = findViewById(R.id.fl_video)
            flInteraction = findViewById(R.id.fl_interaction)
            flBottomSheet = findViewById(R.id.fl_bottom_sheet)
            flGlobalError = findViewById(R.id.fl_global_error)
        }
    }

    private fun setupView(view: View) {
        ivClose.setOnClickListener {
            hideKeyboard()
        }
        flVideo.setOnClickListener {
            hideAllInsets()
        }
    }

    private fun setupScreen(view: View) {
        setFullScreen()
        setInsets(view)
    }

    private fun setFullScreen() {
        PlayFullScreenHelper.showSystemUi(requireActivity())
    }

    private fun setInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val closeLp = ivClose.layoutParams as ViewGroup.MarginLayoutParams
            ivClose.setMargin(closeLp.leftMargin, offset12 + insets.systemWindowInsetTop, closeLp.rightMargin, closeLp.bottomMargin)

            insets
        }
    }

    private fun observeGetChannelInfo() {
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success ->
                    PlayAnalytics.sendScreen(channelId, playViewModel.channelType)
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
            if (it.isFreeze)
                try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
            else if (it.isBanned)
                showEventDialog(it.bannedTitle, it.bannedMessage, it.bannedButtonTitle)
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
                            bufferDurationInSecond = ((System.currentTimeMillis() - bufferTrackingModel.lastBufferMs) / 1000).toInt()
                    )
                }

                bufferTrackingModel = bufferTrackingModel.copy(
                        isBuffering = false,
                        shouldTrackNext = true
                )

            }
        })
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, Observer {
            setWindowSoftInputMode(it.channelType.isLive)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        videoScaleAnimator.cancel()
    }

    fun onBottomInsetsViewShown(bottomMostBounds: Int) {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        videoScaleAnimator.cancel()

        val currentHeight = flVideo.height
        val destHeight = bottomMostBounds.toFloat() - (MARGIN_CHAT_VIDEO + offset12) //offset12 for the range between video and status bar
        val scaleFactor = destHeight / currentHeight
        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y,FULL_SCALE_FACTOR, scaleFactor)
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X,FULL_SCALE_FACTOR, scaleFactor)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        flVideo.pivotX = (flVideo.width / 2).toFloat()
        val marginTop = (ivClose.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        val marginTopXt = marginTop * scaleFactor
        flVideo.pivotY = ivClose.y + (ivClose.y * scaleFactor) + marginTopXt
        videoScaleAnimator.apply {
            removeAllListeners()
            addListener(onBottomInsetsShownAnimatorListener)
            playTogether(animatorX, animatorY)
        }.start()
    }

    fun onBottomInsetsViewHidden() {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        videoScaleAnimator.cancel()

        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y, flVideo.scaleY, FULL_SCALE_FACTOR)
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X, flVideo.scaleX, FULL_SCALE_FACTOR)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        videoScaleAnimator.apply {
            removeAllListeners()
            addListener(onBottomInsetsHiddenAnimatorListener)
            playTogether(animatorX, animatorY)
        }.start()
    }

    fun setResultBeforeFinish() {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            val totalView = playViewModel.totalView
            if (!totalView.isNullOrEmpty()) putExtra(EXTRA_TOTAL_VIEW, totalView)
        })
    }

    /**
     * @return true means the onBackPressed() has been handled by this fragment
     */
    fun onBackPressed(): Boolean {
        return playViewModel.onBackPressed()
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
}