package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.extensions.*
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.measurement.bounds.BoundsKey
import com.tokopedia.play.view.measurement.bounds.manager.videobounds.PlayVideoBoundsManager
import com.tokopedia.play.view.measurement.bounds.manager.videobounds.VideoBoundsManager
import com.tokopedia.play.view.measurement.scaling.PlayVideoScalingManager
import com.tokopedia.play.view.measurement.scaling.VideoScalingManager
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.isYouTube
import com.tokopedia.play.view.viewcomponent.*
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.util.KeyboardWatcher
import com.tokopedia.play.view.uimodel.action.SetChannelActiveAction
import com.tokopedia.play.view.uimodel.recom.PlayStatusSource
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.util.extension.awaitResume
import com.tokopedia.play_common.util.extension.dismissToaster
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.viewcomponent.viewComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment @Inject constructor(
    viewModelFactory: PlayViewModel.Factory,
    private val pageMonitoring: PlayPltPerformanceCallback,
    private val analytic: PlayAnalytic
) :
        TkpdBaseV4Fragment(),
        PlayFragmentContract,
        FragmentVideoViewComponent.Listener,
        FragmentYouTubeViewComponent.Listener,
        PlayVideoScalingManager.Listener {

    private lateinit var ivClose: View
    private val fragmentVideoView by viewComponent {
        FragmentVideoViewComponent(channelId, it, R.id.fl_video, childFragmentManager, this)
    }
    private val fragmentUserInteractionView by viewComponent {
        FragmentUserInteractionViewComponent(channelId, it, R.id.fl_user_interaction, childFragmentManager)
    }
    private val fragmentBottomSheetView by viewComponent {
        FragmentBottomSheetViewComponent(channelId, it, R.id.fl_bottom_sheet, childFragmentManager)
    }
    private val fragmentYouTubeView by viewComponent {
        FragmentYouTubeViewComponent(channelId, it, R.id.fl_youtube, childFragmentManager, this)
    }

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    val viewModelProviderFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return viewModelFactory.create(channelId) as T
        }
    }

    private lateinit var playParentViewModel: PlayParentViewModel
    private lateinit var playViewModel: PlayViewModel

    private val keyboardWatcher = KeyboardWatcher()

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private val playNavigation: PlayNavigation
        get() = requireActivity() as PlayNavigation

    private var videoScalingManager: VideoScalingManager? = null
    private var videoBoundsManager: VideoBoundsManager? = null
    private val boundsMap = BoundsKey.values.associate { Pair(it, 0) }.toMutableMap()

    private var isFirstTopBoundsCalculated = false

    override fun getScreenName(): String = "Play"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(PlayViewModel::class.java)

        val theActivity = requireActivity()
        if (theActivity is PlayActivity) {
            playParentViewModel = ViewModelProvider(theActivity, theActivity.getViewModelFactory()).get(PlayParentViewModel::class.java)
            processChannelInfo()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupInsets(view)
        setupObserve()
    }

    override fun onStart() {
        super.onStart()
        ivClose.requestApplyInsetsWhenAttached()
    }

    override fun onResume() {
        super.onResume()
        stopPrepareMonitoring()
        onPageFocused()
        view?.postDelayed({
            view?.let { registerKeyboardListener(it) }
        }, KEYBOARD_REGISTER_DELAY)
    }

    override fun onPause() {
        unregisterKeyboardListener(requireView())
        onPageDefocused()
        playParentViewModel.setLatestChannelStorageData(
                channelId,
                playViewModel.latestCompleteChannelData
        )
        super.onPause()
    }

    override fun onDestroyView() {
        if (view != null) getVideoScalingManager().onDestroy()
        videoScalingManager = null

        destroyInsets(requireView())
        super.onDestroyView()
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

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        val isIntercepted = childFragmentManager.fragments.asSequence()
                .filterIsInstance<PlayFragmentContract>()
                .any { it.onInterceptOrientationChangedEvent(newOrientation) }
        val videoOrientation = playViewModel.videoOrientation
        return isIntercepted || !videoOrientation.isHorizontal
    }

    override fun onEnterPiPState(pipState: PiPState) {
        if (playViewModel.isPiPAllowed) {
            childFragmentManager.fragments
                    .forEach {
                        if (it is PlayFragmentContract) it.onEnterPiPState(pipState)
                    }
        }
    }

    /**
     * FragmentVideo View Component Listener
     */
    override fun onFragmentClicked(view: FragmentVideoViewComponent) {
        if (playViewModel.bottomInsets.isKeyboardShown) hideKeyboard()
        else hideAllInsets()
    }

    /**
     * FragmentYouTube View Component Listener
     */
    override fun onFragmentClicked(view: FragmentYouTubeViewComponent, isScaling: Boolean) {
        if (!isScaling) return

        if (playViewModel.bottomInsets.isKeyboardShown) hideKeyboard()
        else hideAllInsets()
    }

    /**
     * Video Scaling Manager Listener
     */
    override fun onFinalBottomMostBoundsScalingCalculated(bottomMostBounds: Int) {
        fragmentUserInteractionView.setScaledVideoBottomBounds(bottomMostBounds)
    }

    override fun onAnimationStart(isHidingInsets: Boolean) {
        fragmentUserInteractionView.startAnimateInsets(isHidingInsets)
    }

    override fun onAnimationFinish(isHidingInsets: Boolean) {
        fragmentUserInteractionView.finishAnimateInsets(isHidingInsets)
    }

    fun onFirstTopBoundsCalculated() {
        isFirstTopBoundsCalculated = true
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            when {
                playViewModel.videoPlayer.isYouTube -> {
                    fragmentYouTubeView.safeInit()
                    fragmentYouTubeView.show()
                }
                else -> {
                    fragmentVideoView.safeInit()
                    fragmentVideoView.show()
                }
            }
        }
    }

    fun onBottomInsetsViewShown(bottomMostBounds: Int) {
        if (orientation.isLandscape || view == null) return
        getVideoScalingManager().onBottomInsetsShown(bottomMostBounds, playViewModel.videoPlayer, playViewModel.videoOrientation)
    }

    fun onBottomInsetsViewHidden() {
        if (view == null) return
        getVideoScalingManager().onBottomInsetsHidden(playViewModel.videoPlayer)
    }

    fun setResultBeforeFinish() {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            val totalView = playViewModel.totalView
            if (totalView.isNotEmpty()) putExtra(EXTRA_TOTAL_VIEW, totalView)
            if (channelId.isNotEmpty()) putExtra(EXTRA_CHANNEL_ID, channelId)
        })
    }

    fun setCurrentVideoTopBounds(videoOrientation: VideoOrientation, topBounds: Int) {
        val key = BoundsKey.getByOrientation(orientation, videoOrientation)
        boundsMap[key] = topBounds

        invalidateVideoTopBounds(videoOrientation)
    }

    /**
     * @return true means the onBackPressed() has been handled by this fragment
     */
    fun onBackPressed(): Boolean {
        val isHandled = playViewModel.goBack()
        return when {
            isHandled -> isHandled
            else -> false
        }
    }

    /**
     * When viewpager is swiped to this fragment
     */
    fun setFragmentActive(position: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            /**
             * Workaround because the first UI load is slower,
             * TODO("Maybe find a way to have this logic from viewModel")
             */
            if (position == 0) delay(FIRST_FRAGMENT_ACTIVE_DELAY)
            viewLifecycleOwner.awaitResume()
            playViewModel.submitAction(SetChannelActiveAction)
        }
    }

    private fun processChannelInfo() {
        try {
            playViewModel.createPage(playParentViewModel.getLatestChannelStorageData(channelId))
        } catch (e: Throwable) {}
    }

    //TODO("Somehow when clearing viewpager, onResume is called, and when it happens, channel id is already empty so this might cause crash")
    private fun onPageFocused() {
        try {
            val channelData = playParentViewModel.getLatestChannelStorageData(channelId)
            playViewModel.focusPage(channelData)
            analytic.sendScreen(channelId, playViewModel.channelType, playParentViewModel.sourceType, channelName = channelData.channelDetail.channelInfo.title)
            sendSwipeRoomAnalytic()
        } catch (e: Throwable) {}
    }

    private fun onPageDefocused() {
        playViewModel.defocusPage(shouldPauseVideo = !playViewModel.pipState.isInPiP)
    }

    private fun invalidateVideoTopBounds(
            videoOrientation: VideoOrientation = playViewModel.videoOrientation
    ) {
        val key = BoundsKey.getByOrientation(orientation, playViewModel.videoOrientation)
        val topBounds = boundsMap[key] ?: 0
        getVideoBoundsManager().invalidateVideoBounds(videoOrientation, playViewModel.videoPlayer, topBounds)
    }

    private fun getVideoScalingManager(): VideoScalingManager = synchronized(this) {
        if (videoScalingManager == null) {
            videoScalingManager = PlayVideoScalingManager(requireView() as ViewGroup, this)
        }
        return videoScalingManager!!
    }

    private fun getVideoBoundsManager(): VideoBoundsManager = synchronized(this) {
        if (videoBoundsManager == null) {
            videoBoundsManager = PlayVideoBoundsManager(requireView() as ViewGroup, object : ScreenOrientationDataSource {
                override fun getScreenOrientation(): ScreenOrientation {
                    return orientation
                }
            })
        }
        return videoBoundsManager!!
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newOrientation = ScreenOrientation.getByInt(newConfig.orientation)
        if (newOrientation.isLandscape) hideAllInsets()

        invalidateVideoTopBounds()
    }

    private fun destroyInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(requireView(), null)
    }

    private fun initView(view: View) {
        with (view) {
            ivClose = findViewById(R.id.iv_close)
        }
    }

    private fun setupView(view: View) {
        ivClose.setOnClickListener { hideKeyboard() }
        fragmentVideoView.safeInit()
        fragmentUserInteractionView.safeInit()
        fragmentBottomSheetView.safeInit()

        invalidateVideoTopBounds()
        hideAllInsets()
    }

    private fun setupInsets(view: View) {
        ivClose.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams

            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun setupObserve() {
        observeVideoMeta()
        observeChannelInfo()
        observeBottomInsetsState()
        observePiPEvent()

        observeUiState()
    }

    //region observe
    /**
     * Observe
     */
    private fun handleStatus(status: PlayStatusUiModel) {
        if (status.channelStatus.statusType.isFreeze) {
            dismissToaster()

            if (!playViewModel.bottomInsets.isAnyBottomSheetsShown && status.channelStatus.statusSource == PlayStatusSource.Socket) doAutoSwipe()
            else if (!playViewModel.bottomInsets.isAnyBottomSheetsShown) onBottomInsetsViewHidden()

        } else if (status.channelStatus.statusType.isBanned) {
            showEventDialog(status.config.bannedModel.title, status.config.bannedModel.message, status.config.bannedModel.btnTitle)
        }
        if (status.channelStatus.statusType.isFreeze || status.channelStatus.statusType.isBanned) {
            unregisterKeyboardListener(requireView())
        }

        fragmentVideoViewOnStateChanged(isFreezeOrBanned = status.channelStatus.statusType.isFreeze || status.channelStatus.statusType.isBanned)
        fragmentBottomSheetViewOnStateChanged(isFreezeOrBanned = status.channelStatus.statusType.isFreeze || status.channelStatus.statusType.isBanned)
        fragmentYouTubeViewOnStateChanged(isFreezeOrBanned = status.channelStatus.statusType.isFreeze || status.channelStatus.statusType.isBanned)
    }

    private fun observeVideoMeta() {
        playViewModel.observableVideoMeta.observe(viewLifecycleOwner) { meta ->
            fragmentVideoViewOnStateChanged(videoPlayer = meta.videoPlayer)
            fragmentYouTubeViewOnStateChanged(videoPlayer = meta.videoPlayer)
        }
    }

    private fun observeChannelInfo() {
        playViewModel.observableChannelInfo.observe(viewLifecycleOwner, DistinctObserver {
            setWindowSoftInputMode(it.channelType.isLive)
            setBackground(it.backgroundUrl)
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver {
            buttonCloseViewOnStateChanged(bottomInsets = it)
            fragmentBottomSheetViewOnStateChanged(bottomInsets = it)

            /**
             * We have to change the translationZ for now, because in some cases, the interaction view
             * cover part of the video and prevents the click on the video
             */
            if (it.isAnyShown) {
                playNavigation.requestDisableNavigation()
                fragmentUserInteractionView.rootView.translationZ = -1f
            }
            else {
                playNavigation.requestEnableNavigation()
                fragmentUserInteractionView.rootView.translationZ = 0f
            }
        })
    }

    private fun observePiPEvent() {
        playViewModel.observableEventPiPState.observe(viewLifecycleOwner, EventObserver {
            if (it is PiPState.Requesting) onEnterPiPState(it)
        })
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                val prevState = cachedState.prevValue

                handleStatus(state.status)
            }
        }
    }
    //endregion

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

    /**
     * Performance Monitoring
     */
    private fun stopPrepareMonitoring() {
        pageMonitoring.stopPreparePagePerformanceMonitoring()
    }

    private fun stopNetworkMonitoring() {
        pageMonitoring.stopNetworkRequestPerformanceMonitoring()
    }

    fun startRenderMonitoring() {
        stopNetworkMonitoring()
        pageMonitoring.startRenderPerformanceMonitoring()
    }

    fun stopRenderMonitoring() {
        pageMonitoring.stopRenderPerformanceMonitoring()
        stopPageMonitoring()
    }

    fun getVideoLatency() = playViewModel.videoLatency

    private fun stopPageMonitoring() {
        pageMonitoring.stopMonitoring()
    }

    fun hideKeyboard() {
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

    fun registerKeyboardListener(view: View) {
        keyboardWatcher.listen(view, object : KeyboardWatcher.Listener {
            override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                playViewModel.onKeyboardShown(estimatedKeyboardHeight)
            }

            override fun onKeyboardHidden() {
                playViewModel.onKeyboardHidden()
                if (!playViewModel.bottomInsets.isAnyBottomSheetsShown) this@PlayFragment.onBottomInsetsViewHidden()
            }
        })
    }

    fun unregisterKeyboardListener(view: View) {
        keyboardWatcher.unlisten(view)
    }

    private fun hideAllInsets() {
        hideKeyboard()
        playViewModel.hideInsets(isKeyboardHandled = true)
    }

    fun sendTrackerWhenRotateFullScreen() {
        analytic.userTiltFromPortraitToLandscape()
    }

    private fun setBackground(backgroundUrl: String) {
        Glide.with(requireContext()).load(backgroundUrl).into(object : CustomTarget<Drawable?>() {
            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                view?.background = resource
            }
        })
    }

    private fun doAutoSwipe() {
        if (playNavigation.canNavigateNextPage()) playNavigation.navigateToNextPage()
    }

    private fun sendSwipeRoomAnalytic() {
        if (playParentViewModel.startingChannelId != channelId) analytic.swipeRoom()
    }

    //region onStateChanged
    /**
     * OnStateChanged
     */
    private fun buttonCloseViewOnStateChanged(bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets) {
        if (bottomInsets.isKeyboardShown) ivClose.show()
        else ivClose.invisible()
    }

    private fun fragmentVideoViewOnStateChanged(
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (videoPlayer.isYouTube || (isFreezeOrBanned && !bottomInsets.isAnyBottomSheetsShown)) {
            fragmentVideoView.safeRelease()
            fragmentVideoView.hide()
        }
    }

    private fun fragmentBottomSheetViewOnStateChanged(
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (isFreezeOrBanned && !bottomInsets.isAnyBottomSheetsShown) {
            fragmentBottomSheetView.safeRelease()
            fragmentBottomSheetView.hide()
        }

        if(bottomInsets.isAnyUserReportBottomSheetShown && playViewModel.videoPlayer.isYouTube){
            fragmentBottomSheetView.rootView.translationZ = 1.0f
        }else if(playViewModel.videoPlayer.isYouTube){
            fragmentBottomSheetView.rootView.translationZ = 0.0f
        }
    }

    private fun fragmentYouTubeViewOnStateChanged(
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            if (isFreezeOrBanned) {
                fragmentYouTubeView.safeRelease()
                fragmentYouTubeView.hide()
                return@launchWhenResumed
            }

            if (videoPlayer.isYouTube && isFirstTopBoundsCalculated) {
                fragmentYouTubeView.safeInit()
                fragmentYouTubeView.show()
            }
        }
    }
    //endregion

    companion object {
        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"

        const val KEYBOARD_REGISTER_DELAY = 200L
        private const val FIRST_FRAGMENT_ACTIVE_DELAY = 500L
    }
}