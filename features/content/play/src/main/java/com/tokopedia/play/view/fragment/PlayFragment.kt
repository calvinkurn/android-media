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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.play.ERR_STATE_SOCKET
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.isAnyBottomSheetsShown
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.ui.closebutton.CloseButtonComponent
import com.tokopedia.play.ui.closebutton.interaction.CloseButtonInteractionEvent
import com.tokopedia.play.ui.fragment.bottomsheet.FragmentBottomSheetComponent
import com.tokopedia.play.ui.fragment.error.FragmentErrorComponent
import com.tokopedia.play.ui.fragment.miniinteraction.FragmentMiniInteractionComponent
import com.tokopedia.play.ui.fragment.userinteraction.FragmentUserInteractionComponent
import com.tokopedia.play.ui.fragment.video.FragmentVideoComponent
import com.tokopedia.play.ui.fragment.video.interaction.FragmentVideoInteractionEvent
import com.tokopedia.play.ui.fragment.youtube.FragmentYouTubeComponent
import com.tokopedia.play.ui.fragment.youtube.interaction.FragmentYouTubeInteractionEvent
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.util.PlaySensorOrientationManager
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.keyboard.KeyboardWatcher
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayNewChannelInteractor
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.layout.parent.PlayParentLayoutManager
import com.tokopedia.play.view.layout.parent.PlayParentLayoutManagerImpl
import com.tokopedia.play.view.layout.parent.PlayParentViewInitializer
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.EventUiModel
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.GlobalErrorCodeWrapper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayFragment : BaseDaggerFragment(), PlayOrientationListener, PlayFragmentContract, PlayParentViewInitializer {

    companion object {
        private const val TOP_BOUNDS_LANDSCAPE_VIDEO = "top_bounds_landscape_video"

        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"

        fun newInstance(channelId: String?): PlayFragment {
            return PlayFragment().apply {
                val args = Bundle()
                args.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = args
            }
        }
    }

    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = job + dispatchers.main
    }
    private val job: Job = SupervisorJob()

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var channelId = ""
    private var topBounds: Int? = null

    private lateinit var pageMonitoring: PageLoadTimePerformanceInterface
    private lateinit var playViewModel: PlayViewModel

    /**
     * Manager
     */
    private lateinit var layoutManager: PlayParentLayoutManager
    private lateinit var orientationManager: PlaySensorOrientationManager

    private val keyboardWatcher = KeyboardWatcher()

    private var requestedOrientation: Int
        get() = requireActivity().requestedOrientation
        set(value) {
            requireActivity().requestedOrientation = value
        }

    private var systemUiVisibility: Int
        get() = requireActivity().window.decorView.systemUiVisibility
        set(value) {
            requireActivity().window.decorView.systemUiVisibility = value
        }

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

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
        setOrientation()
        setupPageMonitoring()
        playViewModel = ViewModelProvider(this, viewModelFactory).get(PlayViewModel::class.java)
        channelId = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedInstanceState?.containsKey(TOP_BOUNDS_LANDSCAPE_VIDEO) == true) {
            topBounds = savedInstanceState.getInt(TOP_BOUNDS_LANDSCAPE_VIDEO, 0)
        }

        val view = inflater.inflate(R.layout.fragment_play, container, false)
        initComponents(view as ViewGroup)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupScreen(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeGetChannelInfo()
        observeSocketInfo()
        observeEventUserInfo()
        observeVideoProperty()
        observeVideoStream()
        observeVideoPlayer()
        observeBottomInsetsState()
    }

    override fun onResume() {
        super.onResume()
        orientationManager.enable()
        stopPrepareMonitoring()
        startNetworkMonitoring()
        playViewModel.getChannelInfo(channelId)
        onInterceptSystemUiVisibilityChanged()
        scope.launch {
            delay(200)
            registerKeyboardListener(requireView())
        }
    }

    override fun onPause() {
        unregisterKeyboardListener(requireView())
        playViewModel.stopJob()
        super.onPause()
        if (::orientationManager.isInitialized) orientationManager.disable()
    }

    override fun onDestroyView() {
        destroyInsets(requireView())
        super.onDestroyView()
        if (::layoutManager.isInitialized) layoutManager.onDestroy()
        job.cancelChildren()
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

    override fun onSaveInstanceState(outState: Bundle) {
        topBounds?.let { outState.putInt(TOP_BOUNDS_LANDSCAPE_VIDEO, it) }
        super.onSaveInstanceState(outState)
    }

    override fun onOrientationChanged(screenOrientation: ScreenOrientation, isTilting: Boolean) {
        if (requestedOrientation != screenOrientation.requestedOrientation && !onInterceptOrientationChangedEvent(screenOrientation))
            requestedOrientation = screenOrientation.requestedOrientation

        sendTrackerWhenRotateScreen(screenOrientation, isTilting)
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        val isIntercepted = childFragmentManager.fragments.asSequence()
                .filterIsInstance<PlayFragmentContract>()
                .any { it.onInterceptOrientationChangedEvent(newOrientation) }
        val videoOrientation = playViewModel.videoOrientation
        return isIntercepted || !videoOrientation.isHorizontal
    }

    override fun onInterceptSystemUiVisibilityChanged(): Boolean {
        val isIntercepted = childFragmentManager.fragments.asSequence()
                .filterIsInstance<PlayFragmentContract>()
                .any { it.onInterceptSystemUiVisibilityChanged() }

        if (!isIntercepted) {
            if (orientation.isLandscape) systemUiVisibility = PlayFullScreenHelper.getHideSystemUiVisibility()
        }

        return isIntercepted
    }

    fun onBottomInsetsViewShown(bottomMostBounds: Int) {
        if (orientation.isLandscape) return
        layoutManager.onBottomInsetsShown(requireView(), bottomMostBounds, playViewModel.videoPlayer, playViewModel.videoOrientation)
    }

    fun onBottomInsetsViewHidden() {
        layoutManager.onBottomInsetsHidden(requireView(), playViewModel.videoPlayer)
    }

    fun setResultBeforeFinish() {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            val totalView = playViewModel.totalView
            if (!totalView.isNullOrEmpty()) putExtra(EXTRA_TOTAL_VIEW, totalView)
            if (!channelId.isNullOrEmpty()) putExtra(EXTRA_CHANNEL_ID, channelId)
        })
    }

    fun setVideoTopBounds(videoPlayer: VideoPlayerUiModel, videoOrientation: VideoOrientation, topBounds: Int) {
        if (this.topBounds == null && topBounds > 0) {
            this.topBounds = topBounds
        }
        this.topBounds?.let { layoutManager.onVideoTopBoundsChanged(requireView(), videoPlayer, orientation, videoOrientation, it) }
    }

    /**
     * @return true means the onBackPressed() has been handled by this fragment
     */
    fun onBackPressed(): Boolean {
        val isHandled = playViewModel.onBackPressed()
        return when {
            isHandled -> isHandled
            else -> false
        }
    }

    fun onNewChannelId(channelId: String?) {
        if (this.channelId != channelId && activity is PlayNewChannelInteractor) {
            (activity as PlayNewChannelInteractor).onNewChannel(channelId)
        }
    }

    //region init components
    private fun initComponents(container: ViewGroup) {
        layoutManager = PlayParentLayoutManagerImpl(
                container = container,
                viewInitializer = this
        )

        sendInitState()
        layoutManager.layoutView(container)
    }

    override fun onInitCloseButton(container: ViewGroup): Int {
        val closeButtonComponent = CloseButtonComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
            closeButtonComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            CloseButtonInteractionEvent.OnClicked -> hideKeyboard()
                        }
                    }
        }

        return closeButtonComponent.getContainerId()
    }

    override fun onInitVideoFragment(container: ViewGroup): Int {
        val fragmentVideoComponent = FragmentVideoComponent(channelId, container, childFragmentManager, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
            fragmentVideoComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            FragmentVideoInteractionEvent.OnClicked -> {
                                if (playViewModel.bottomInsets.isKeyboardShown) hideKeyboard()
                                else hideAllInsets()
                            }
                        }
                    }
        }

        return fragmentVideoComponent.getContainerId()
    }

    override fun onInitUserInteractionFragment(container: ViewGroup): Int {
        return FragmentUserInteractionComponent(channelId, container, childFragmentManager, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }

    override fun onInitMiniInteractionFragment(container: ViewGroup): Int {
        return FragmentMiniInteractionComponent(channelId, container, childFragmentManager, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }

    override fun onInitBottomSheetFragment(container: ViewGroup): Int {
        return FragmentBottomSheetComponent(channelId, container, childFragmentManager, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }

    override fun onInitYouTubeFragment(container: ViewGroup): Int {
        val fragmentYouTubeComponent = FragmentYouTubeComponent(channelId, container, childFragmentManager, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
            fragmentYouTubeComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is FragmentYouTubeInteractionEvent.OnClicked -> {
                                if (!it.isScaling) return@collect

                                if (playViewModel.bottomInsets.isKeyboardShown) hideKeyboard()
                                else hideAllInsets()
                            }
                        }
                    }
        }

        return fragmentYouTubeComponent.getContainerId()
    }

    override fun onInitErrorFragment(container: ViewGroup): Int {
        return FragmentErrorComponent(channelId, container, childFragmentManager, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }
    //endregion

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newOrientation = ScreenOrientation.getByInt(newConfig.orientation)
        if (newOrientation.isLandscape) hideAllInsets()
        layoutManager.onOrientationChanged(requireView(), newOrientation, playViewModel.videoOrientation, playViewModel.videoPlayer)
        sendOrientationChangedEvent(newOrientation)
        onInterceptSystemUiVisibilityChanged()
    }

    private fun sendInitState() {
        scope.launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init(orientation, playViewModel.getStateHelper(orientation))
            )
        }
    }

    private fun sendEventBanned(event: EventUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.OnNewPlayRoomEvent(
                                    PlayRoomEvent.Banned(
                                            title = event.bannedTitle,
                                            message = event.bannedMessage,
                                            btnTitle = event.bannedButtonTitle
                                    )
                            )
                    )
        }
    }

    private fun sendEventFreeze(event: EventUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.OnNewPlayRoomEvent(
                                    PlayRoomEvent.Freeze(
                                            title = event.freezeTitle,
                                            message = event.freezeMessage,
                                            btnTitle = event.freezeButtonTitle,
                                            btnUrl = event.freezeButtonUrl
                                    )
                            )
                    )
        }
    }

    private fun sendOrientationChangedEvent(orientation: ScreenOrientation) {
        scope.launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.OrientationChanged(orientation, playViewModel.getStateHelper(orientation))
            )
        }
    }

    private fun destroyInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(requireView(), null)
    }

    private fun setOrientation() {
        orientationManager = PlaySensorOrientationManager(requireContext(), this)
    }

    private fun initView(view: View) {
        topBounds?.let { setVideoTopBounds(playViewModel.videoPlayer, playViewModel.videoOrientation, it) }
    }

    private fun setupView(view: View) {
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
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, DistinctObserver { result ->
            when (result) {
                is Success -> PlayAnalytics.sendScreen(channelId, playViewModel.channelType)
                is Fail -> result.throwable.message?.let {
                    if (GlobalErrorCodeWrapper.wrap(it) != GlobalErrorCodeWrapper.Unknown) showGlobalError()
                }
            }
        })
    }

    private fun observeSocketInfo() {
        playViewModel.observableSocketInfo.observe(viewLifecycleOwner, DistinctObserver {
            when(it) {
                is PlaySocketInfo.Reconnect ->
                    PlayAnalytics.errorState(channelId, "$ERR_STATE_SOCKET: ${getString(R.string.play_message_socket_reconnect)}", playViewModel.channelType)
                is PlaySocketInfo.Error ->
                    PlayAnalytics.errorState(channelId, "$ERR_STATE_SOCKET: ${it.throwable.localizedMessage}", playViewModel.channelType)
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, DistinctObserver {
            if (it.isFreeze) {
                sendEventFreeze(it)
                try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
            } else if (it.isBanned) {
                sendEventBanned(it)
                showEventDialog(it.bannedTitle, it.bannedMessage, it.bannedButtonTitle)
            }
            if (it.isFreeze || it.isBanned) {
                unregisterKeyboardListener(requireView())
                onBottomInsetsViewHidden()
            }
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, DistinctObserver {
            layoutManager.onVideoStateChanged(requireView(), it.state, playViewModel.videoOrientation)
        })
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, DistinctObserver {
            setWindowSoftInputMode(it.channelType.isLive)
            setBackground(it.coverUrl)
        })
    }

    private fun observeVideoPlayer() {
        playViewModel.observableVideoPlayer.observe(viewLifecycleOwner, Observer {
            scope.launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVideo(it)
                        )
            }
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver {
            scope.launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.BottomInsetsChanged(it, it.isAnyShown, it.isAnyHidden, playViewModel.getStateHelper(orientation))
                        )
            }
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

    private fun showGlobalError() {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(ScreenStateEvent::class.java, ScreenStateEvent.ShowGlobalError)
        }
    }

    /**
     * Performance Monitoring
     */
    private fun setupPageMonitoring() {
        if (activity != null && activity is PlayActivity) {
            pageMonitoring = (activity as PlayActivity).getPageMonitoring()
        }
    }

    private fun stopPrepareMonitoring() {
        pageMonitoring.stopPreparePagePerformanceMonitoring()
    }

    private fun startNetworkMonitoring() {
        pageMonitoring.startNetworkRequestPerformanceMonitoring()
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

    private fun stopPageMonitoring() {
        pageMonitoring.stopMonitoring()
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
            }

            override fun onKeyboardHidden() {
                playViewModel.onKeyboardHidden()
                if (!playViewModel.getStateHelper(orientation).bottomInsets.isAnyBottomSheetsShown) this@PlayFragment.onBottomInsetsViewHidden()
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

    private fun sendTrackerWhenRotateScreen(screenOrientation: ScreenOrientation, isTilting: Boolean) {
        if (screenOrientation.isLandscape && isTilting) {
            PlayAnalytics.userTiltFromPortraitToLandscape(
                    userId = playViewModel.userId,
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