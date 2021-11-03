package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.pusher.view.PlayLivePusherDebugView
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveInitState
import com.tokopedia.play.broadcaster.ui.model.interactive.BroadcastInteractiveState
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherErrorType
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayInteractiveLeaderBoardBottomSheet
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayProductLiveBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayMetricsView
import com.tokopedia.play.broadcaster.view.custom.PlayStatInfoView
import com.tokopedia.play.broadcaster.view.custom.PlayTimerCountDown
import com.tokopedia.play.broadcaster.view.custom.PlayTimerView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.ActionBarViewComponent
import com.tokopedia.play.broadcaster.view.partial.BroadcastInteractiveSetupViewComponent
import com.tokopedia.play.broadcaster.view.partial.BroadcastInteractiveViewComponent
import com.tokopedia.play.broadcaster.view.partial.ChatListViewComponent
import com.tokopedia.play.broadcaster.view.state.PlayLiveCountDownTimerState
import com.tokopedia.play.broadcaster.view.state.PlayLiveViewState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.detachableview.detachableView
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by mzennis on 25/05/20.
 */
class PlayBroadcastUserInteractionFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic
): PlayBaseBroadcastFragment(), FragmentWithDetachableView {

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private val viewTimer: PlayTimerView by detachableView(R.id.view_timer)
    private val viewStatInfo: PlayStatInfoView by detachableView(R.id.view_stat_info)
    private val ivShareLink: AppCompatImageView by detachableView(R.id.iv_share_link)
    private val flProductTag: FrameLayout by detachableView(R.id.fl_product_tag)
    private val pmvMetrics: PlayMetricsView by detachableView(R.id.pmv_metrics)
    private val countdownTimer: PlayTimerCountDown by detachableView(R.id.countdown_timer)
    private val loadingView: FrameLayout by detachableView(R.id.loading_view)
    private val errorLiveNetworkLossView: View by detachableView(R.id.error_live_view)
    private val debugView: PlayLivePusherDebugView by detachableView(R.id.live_debug_view)

    private val actionBarView by viewComponent {
        ActionBarViewComponent(it, object : ActionBarViewComponent.Listener {
            override fun onCameraIconClicked() {
                parentViewModel.switchCamera()
                analytic.clickSwitchCameraOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
            }

            override fun onCloseIconClicked() {
                activity?.onBackPressed()
            }
        })
    }
    private val chatListView by viewComponent { ChatListViewComponent(it) }
    private val interactiveView by viewComponent {
        BroadcastInteractiveViewComponent(it, object : BroadcastInteractiveViewComponent.Listener {
            override fun onNewGameClicked(view: BroadcastInteractiveViewComponent) {
                if (allowSetupInteractive()) {
                    interactiveSetupView.setActiveTitle(parentViewModel.interactiveTitle)
                    interactiveSetupView.setAvailableDurations(parentViewModel.interactiveDurations)
                    interactiveSetupView.setSelectedDuration(parentViewModel.selectedInteractiveDuration)
                    interactiveSetupView.show()
                    analytic.onClickInteractiveTool(parentViewModel.channelId)
                } else {
                    showToaster(
                        message = getString(R.string.play_interactive_broadcast_not_allowed),
                        duration = Toaster.LENGTH_SHORT
                    )
                }
            }

            override fun onSeeWinnerClicked(view: BroadcastInteractiveViewComponent) {
                openInteractiveLeaderboardSheet()
                analytic.onClickWinnerIcon(
                    parentViewModel.channelId,
                    parentViewModel.interactiveId,
                    parentViewModel.interactiveTitle
                )
            }
        })
    }

    private val interactiveSetupView by viewComponent {
        BroadcastInteractiveSetupViewComponent(it, object : BroadcastInteractiveSetupViewComponent.Listener {
            override fun onTitleInputChanged(
                view: BroadcastInteractiveSetupViewComponent,
                title: String
            ) {
                parentViewModel.setInteractiveTitle(title)
            }

            override fun onPickerValueChanged(
                view: BroadcastInteractiveSetupViewComponent,
                durationInMs: Long
            ) {
                parentViewModel.setSelectedInteractiveDuration(durationInMs)
            }

            override fun onApplyButtonClicked(
                view: BroadcastInteractiveSetupViewComponent,
                title: String,
                durationInMs: Long
            ) {
                parentViewModel.createInteractiveSession(title, durationInMs)
            }
        })
    }

    private lateinit var productLiveBottomSheet: PlayProductLiveBottomSheet

    private lateinit var exitDialog: DialogUnify
    private lateinit var forceStopDialog: DialogUnify

    private val fragmentViewContainer = FragmentViewContainer()

    private var toasterBottomMargin = 0

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcast_user_interaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupInsets()
        setupObserve()

        if (arguments?.getBoolean(KEY_START_COUNTDOWN) == true) {
            startCountDown()
        }

        if (GlobalConfig.DEBUG) setupDebugView(view)
    }

    override fun onStart() {
        super.onStart()
        actionBarView.rootView.requestApplyInsetsWhenAttached()
        ivShareLink.requestApplyInsetsWhenAttached()
        viewTimer.requestApplyInsetsWhenAttached()
    }

    override fun getViewContainer(): FragmentViewContainer = fragmentViewContainer

    private fun setupView() {
        actionBarView.setActionTitle(getString(R.string.play_action_bar_end))
        ivShareLink.setOnClickListener{
            doCopyShareLink()
            analytic.clickShareIconOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        }
        flProductTag.setOnClickListener {
            doShowProductInfo()
            analytic.clickProductTagOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        }
    }

    private fun setupInsets() {
        actionBarView.rootView.doOnApplyWindowInsets { v, insets, _, _ ->
            v.updatePadding(top = insets.systemWindowInsetTop)
        }

        viewTimer.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }

        ivShareLink.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun setupDebugView(view: View) {
        val ivSetting = view.findViewById<AppCompatImageView>(R.id.iv_setting)

        ivSetting.show()
        ivSetting.setOnClickListener {
            debugView.show()
        }

        observeLiveInfo()
        observeLiveStats()
    }

    private fun observeLiveInfo() {
        parentViewModel.observableLivePusherInfo.observe(viewLifecycleOwner) {
            debugView.setLiveInfo(it)
        }
    }

    private fun observeLiveStats() {
        parentViewModel.observableLivePusherStatistic.observe(viewLifecycleOwner) {
            debugView.updateStats(it)
        }
    }

    private fun setupObserve() {
        observeLiveState()
        observeLiveDuration()
        observeTotalViews()
        observeTotalLikes()
        observeChatList()
        observeMetrics()
        observeEvent()
        observeInteractiveConfig()
        observeCreateInteractiveSession()
    }

    private fun startCountDown() {
        val animationProperty = PlayTimerCountDown.AnimationProperty.Builder()
                .setFullRotationInterval(TIMER_ROTATION_INTERVAL)
                .setTextCountDownInterval(TIMER_TEXT_COUNTDOWN_INTERVAL)
                .setTotalCount(TIMER_ANIMATION_TOTAL_COUNT)
                .build()

        countdownTimer.visible()
        countdownTimer.startCountDown(animationProperty, object : PlayTimerCountDown.Listener {
            override fun onTick(milisUntilFinished: Long) {}

            override fun onFinish() {
                countdownTimer.gone()
                parentViewModel.startLiveCountDownTimer()
            }
        })
    }

    override fun onDestroy() {
        try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        return if (interactiveSetupView.isShown()) interactiveSetupView.interceptBackPressed()
        else showDialogWhenActionClose()
    }

    /**
     * render to ui
     */
    private fun showCounterDuration(remainingInMs: Long) {
        viewTimer.showCounter(remainingInMs)
    }

    private fun setTotalView(totalView: TotalViewUiModel) {
        viewStatInfo.setTotalView(totalView)
    }

    private fun setTotalLike(totalLike: TotalLikeUiModel) {
        viewStatInfo.setTotalLike(totalLike)
    }

    private fun setChatList(chatList: List<PlayChatUiModel>) {
        chatListView.setChatList(chatList)
    }

    private fun setNewChat(chat: PlayChatUiModel) {
        chatListView.showNewChat(chat)
    }

    private fun setNewMetrics(metrics: List<PlayMetricUiModel>) {
        pmvMetrics.addMetricsToQueue(metrics)
    }

    private fun getProductLiveBottomSheet(): PlayProductLiveBottomSheet {
        if (!::productLiveBottomSheet.isInitialized) {
            val setupClass = PlayProductLiveBottomSheet::class.java
            val fragmentFactory = childFragmentManager.fragmentFactory
            productLiveBottomSheet = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayProductLiveBottomSheet
        }
        return productLiveBottomSheet
    }

    private fun showDialogWhenActionClose(): Boolean {
        getExitDialog().show()
        analytic.viewDialogExitOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
        return true
    }

    private fun getExitDialog(): DialogUnify {
        if (!::exitDialog.isInitialized) {
           exitDialog = requireContext().getDialog(
                   actionType = DialogUnify.HORIZONTAL_ACTION,
                   title = getString(R.string.play_live_broadcast_dialog_end_title),
                   desc = getString(R.string.play_live_broadcast_dialog_end_desc),
                   primaryCta = getString(R.string.play_live_broadcast_dialog_end_primary),
                   primaryListener = { dialog -> dialog.dismiss() },
                   secondaryCta = getString(R.string.play_broadcast_exit),
                   secondaryListener = { dialog ->
                       dialog.dismiss()
                       parentViewModel.stopLiveStream(shouldNavigate = true)
                       analytic.clickDialogExitOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
                   }
           )
        }
        return exitDialog
    }

    private fun showDialogWhenTimeout() {
        showForceStopDialog(
                title = getString(R.string.play_live_broadcast_dialog_end_timeout_title),
                message = getString(R.string.play_live_broadcast_dialog_end_timeout_desc),
                buttonTitle = getString(R.string.play_live_broadcast_dialog_end_timeout_primary),
                onClickListener = {
                    analytic.clickDialogSeeReportOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
                }
        )
    }

    private fun showForceStopDialog(
            title: String,
            message: String,
            buttonTitle: String,
            onClickListener: () -> Unit = { }) {
        if (!::forceStopDialog.isInitialized) {
            forceStopDialog = requireContext().getDialog(
                    title = title,
                    desc = message,
                    primaryCta = buttonTitle,
                    primaryListener = { dialog ->
                        dialog.dismiss()
                        navigateToSummary()
                        onClickListener()
                    }
            )
        }
        if (!forceStopDialog.isShowing) forceStopDialog.show()
    }

    private fun showDialogContinueLiveStreaming() {
        if (!isVisible || !isAdded) return
        (requireActivity() as? PlayBroadcastActivity)?.showDialogContinueLiveStreaming()
    }

    private fun showErrorToaster(
        err: Throwable,
        customErrMessage: String? = null,
        duration: Int = Toaster.LENGTH_LONG,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener {  }
    ) {
        val errMessage = if (customErrMessage == null) {
            ErrorHandler.getErrorMessage(
                context, err, ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
        } else {
            val (_, errCode) = ErrorHandler.getErrorMessagePair(
                context, err, ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            getString(
                commonR.string.play_custom_error_handler_msg,
                customErrMessage,
                errCode
            )
        }
        showToaster(errMessage, Toaster.TYPE_ERROR, duration, actionLabel, actionListener)
    }

    private fun showToaster(
            message: String,
            type: Int = Toaster.TYPE_NORMAL,
            duration: Int = Toaster.LENGTH_LONG,
            actionLabel: String = "",
            actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        if (toasterBottomMargin == 0) {
            val offset24 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
            toasterBottomMargin = ivShareLink.height + offset24
        }
        view?.showToaster(
                message = message,
                duration = duration,
                type = type,
                actionLabel = actionLabel,
                actionListener = actionListener,
                bottomMargin = toasterBottomMargin
        )
    }

    private fun doCopyShareLink() {
        PlayShareWrapper.copyToClipboard(requireContext(), parentViewModel.shareContents) {
            showToaster(
                    message = getString(R.string.play_live_broadcast_share_link_copied),
                    actionLabel = getString(R.string.play_ok))
        }
    }

    private fun doShowProductInfo() {
        getProductLiveBottomSheet().show(childFragmentManager)
    }

    private fun navigateToSummary() {
        broadcastCoordinator.navigateToFragment(PlayBroadcastSummaryFragment::class.java)
        analytic.openReportScreen(parentViewModel.channelId)
    }

    private fun handleLivePushInfo(state: PlayLiveViewState) {
        if (!isVisible) return
        errorLiveNetworkLossView.hide()
        when (state) {
            is PlayLiveViewState.Connecting -> showLoading(true)
            is PlayLiveViewState.Started -> showLoading(false)
            is PlayLiveViewState.Stopped -> {
                showLoading(false)
                 if (state.shouldNavigate) navigateToSummary()
            }
            is PlayLiveViewState.Error -> {
                showLoading(false)
                handleLivePushError(state)
            }
            is PlayLiveViewState.Resume -> {
                showLoading(false)
                if (!state.isResumed) showDialogContinueLiveStreaming()
            }
            is PlayLiveViewState.Recovered -> {
                showLoading(false)
                showToaster(
                        message = getString(R.string.play_live_broadcast_network_recover),
                        type = Toaster.TYPE_NORMAL
                )
            }
        }
        debugView.updateState(state)
    }

    private fun handleLivePushError(state: PlayLiveViewState.Error) {
        when(state.error.type) {
            PlayLivePusherErrorType.NetworkPoor -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_live_broadcast_network_poor),
            )
            PlayLivePusherErrorType.NetworkLoss -> errorLiveNetworkLossView.show()
            PlayLivePusherErrorType.ConnectFailed -> {
                showErrorToaster(
                    err = state.error,
                    customErrMessage = getString(R.string.play_live_broadcast_connect_fail),
                    duration = Toaster.LENGTH_INDEFINITE,
                    actionLabel = getString(R.string.play_broadcast_try_again),
                    actionListener = { parentViewModel.reconnectLiveStream() }
                )
            }
            PlayLivePusherErrorType.SystemError -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_dialog_unsupported_device_desc),
                duration = Toaster.LENGTH_INDEFINITE,
                actionLabel = getString(R.string.play_ok),
                actionListener = { parentViewModel.stopLiveStream(shouldNavigate = true) }
            )
        }
        analytic.viewErrorOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle, state.error.reason)
    }

    private fun showLoading(isLoading: Boolean) {
        loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    //region observe
    /**
     * Observe
     */
    private fun observeLiveState() {
        parentViewModel.observableLiveViewState.observe(viewLifecycleOwner, Observer(::handleLivePushInfo))
    }

    private fun observeTotalViews() {
        parentViewModel.observableTotalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.observableTotalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }

    private fun observeLiveDuration() {
        parentViewModel.observableLiveCountDownTimerState.observe(viewLifecycleOwner) {
            when(it)  {
                is PlayLiveCountDownTimerState.Active -> showCounterDuration(it.remainingInMs)
                is PlayLiveCountDownTimerState.Finish -> {
                    analytic.viewDialogSeeReportOnLivePage(parentViewModel.channelId, parentViewModel.channelTitle)
                    showDialogWhenTimeout()
                }
            }
        }
    }

    private fun observeChatList() {
        parentViewModel.observableChatList.observe(viewLifecycleOwner, object : Observer<List<PlayChatUiModel>> {
            override fun onChanged(chatList: List<PlayChatUiModel>) {
                setChatList(chatList)
                parentViewModel.observableChatList.removeObserver(this)
            }
        })

        parentViewModel.observableNewChat.observe(viewLifecycleOwner, EventObserver(::setNewChat))
    }

    private fun observeMetrics() {
        parentViewModel.observableNewMetrics.observe(viewLifecycleOwner, EventObserver(::setNewMetrics))
    }

    private fun observeEvent() {
        parentViewModel.observableEvent.observe(viewLifecycleOwner) {
            when {
                it.freeze -> {
                    showForceStopDialog(
                            title = getString(R.string.play_live_broadcast_dialog_end_timeout_title),
                            message = getString(R.string.play_live_broadcast_dialog_end_timeout_desc),
                            buttonTitle = getString(R.string.play_live_broadcast_dialog_end_timeout_primary)
                    )
                }
                it.banned -> {
                    showForceStopDialog(
                            title = it.title,
                            message = it.message,
                            buttonTitle = it.buttonTitle
                    )
                }
            }
        }
    }

    private fun observeInteractiveConfig() {
        parentViewModel.observableInteractiveConfig.observe(viewLifecycleOwner) { config ->
            interactiveSetupView.setConfig(config)
        }
        parentViewModel.observableInteractiveState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BroadcastInteractiveState.Forbidden -> {
                    interactiveView.hide()
                }
                is BroadcastInteractiveState.Allowed -> {
                    handleHasInteractiveState(state)
                    interactiveView.show()
                }
            }
        }
    }

    private fun observeCreateInteractiveSession() {
        parentViewModel.observableCreateInteractiveSession.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkResult.Loading -> interactiveSetupView.setLoading(true)
                is NetworkResult.Success -> {
                    analytic.onStartInteractive(
                        channelId = parentViewModel.channelId,
                        interactiveId = parentViewModel.interactiveId,
                        interactiveTitle = parentViewModel.interactiveTitle,
                        durationInMs = state.data.durationInMs
                    )
                    interactiveSetupView.setLoading(false)
                    interactiveSetupView.reset()
                }
                is NetworkResult.Fail -> {
                    interactiveSetupView.setLoading(false)
                    showErrorToaster(
                        err = state.error,
                        customErrMessage = getString(R.string.play_interactive_broadcast_create_fail),
                        duration = Toaster.LENGTH_SHORT
                    )
                }
            }
        }
    }
    //endregion

    private fun allowSetupInteractive(): Boolean {
        return parentViewModel.interactiveDurations.isNotEmpty()
    }

    private fun handleHasInteractiveState(state: BroadcastInteractiveState.Allowed) {

        fun setLive(timeInMs: Long) {
            interactiveView.setLive(timeInMs) {
                parentViewModel.onInteractiveLiveEnded()
            }
        }

        when (state) {
            is BroadcastInteractiveState.Allowed.Init -> handleInitInteractiveState(state.state)
            is BroadcastInteractiveState.Allowed.Schedule -> {
                interactiveView.setSchedule(state.title, state.timeToStartInMs) {
                    setLive(state.durationInMs)
                }
            }
            is BroadcastInteractiveState.Allowed.Live -> {
                setLive(state.remainingTimeInMs)
            }
        }
    }

    private fun handleInitInteractiveState(state: BroadcastInteractiveInitState) {
        when (state) {
            is BroadcastInteractiveInitState.NoPrevious -> {
                analytic.onImpressInteractiveTool(parentViewModel.channelId)
                interactiveView.setInit(state.showOnBoarding)
            }
            BroadcastInteractiveInitState.Loading -> interactiveView.setLoading()
            is BroadcastInteractiveInitState.HasPrevious -> {
                analytic.onImpressWinnerIcon(parentViewModel.channelId, parentViewModel.interactiveId, parentViewModel.interactiveTitle)
                interactiveView.setFinish(state.coachMark)
            }
        }
    }

    private fun openInteractiveLeaderboardSheet() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val leaderBoardBottomSheet = fragmentFactory.instantiate(
            requireContext().classLoader,
            PlayInteractiveLeaderBoardBottomSheet::class.java.name) as PlayInteractiveLeaderBoardBottomSheet
        leaderBoardBottomSheet.show(childFragmentManager)
    }

    companion object {
        const val KEY_START_COUNTDOWN = "start_count_down"

        private const val TIMER_ROTATION_INTERVAL = 3000L
        private const val TIMER_TEXT_COUNTDOWN_INTERVAL = 2000L
        private const val TIMER_ANIMATION_TOTAL_COUNT = 3
    }
}