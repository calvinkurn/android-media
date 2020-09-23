package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherActiveStatus
import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherErrorStatus
import com.tokopedia.play.broadcaster.ui.model.PlayChannelStatus
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayProductLiveBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayMetricsView
import com.tokopedia.play.broadcaster.view.custom.PlayStatInfoView
import com.tokopedia.play.broadcaster.view.custom.PlayTimerCountDown
import com.tokopedia.play.broadcaster.view.custom.PlayTimerView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.play.broadcaster.view.partial.ChatListPartialView
import com.tokopedia.play.broadcaster.view.state.LivePusherState
import com.tokopedia.play.broadcaster.view.state.LivePusherTimerState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by mzennis on 25/05/20.
 */
class PlayBroadcastUserInteractionFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic
): PlayBaseBroadcastFragment() {

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var viewTimer: PlayTimerView
    private lateinit var viewStatInfo: PlayStatInfoView
    private lateinit var ivShareLink: AppCompatImageView
    private lateinit var ivProductTag: AppCompatImageView
    private lateinit var pmvMetrics: PlayMetricsView
    private lateinit var countdownTimer: PlayTimerCountDown
    private lateinit var loadingView: FrameLayout

    private lateinit var chatListView: ChatListPartialView
    private lateinit var productLiveBottomSheet: PlayProductLiveBottomSheet

    private lateinit var exitDialog: DialogUnify
    private lateinit var forceStopDialog: DialogUnify

    private var toasterBottomMargin = 0

    private lateinit var loadingFragment: LoadingDialogFragment

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcast_user_interaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView()
        setupInsets(view)

        if (arguments?.getBoolean(KEY_START_COUNTDOWN) == true) {
            startCountDown()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeLiveInfo()
        observeLiveDuration()
        observeTotalViews()
        observeTotalLikes()
        observeChatList()
        observeMetrics()
        observeBannedEvent()
    }

    override fun onStart() {
        super.onStart()
        ivShareLink.requestApplyInsetsWhenAttached()
        viewTimer.requestApplyInsetsWhenAttached()
    }

    private fun initView(view: View) {
        viewTimer = view.findViewById(R.id.view_timer)
        viewStatInfo = view.findViewById(R.id.view_stat_info)
        ivShareLink = view.findViewById(R.id.iv_share_link)
        ivProductTag = view.findViewById(R.id.iv_product_tag)
        pmvMetrics = view.findViewById(R.id.pmv_metrics)
        countdownTimer = view.findViewById(R.id.countdown_timer)
        loadingView = view.findViewById(R.id.loading_view)

        chatListView = ChatListPartialView(view as ViewGroup)
    }

    private fun setupView() {
        broadcastCoordinator.setupTitle("")
        broadcastCoordinator.setupCloseButton(getString(R.string.play_action_bar_end))

        ivShareLink.setOnClickListener{
            doCopyShareLink()
            analytic.clickShareIconOnLivePage(parentViewModel.channelId, parentViewModel.title)
        }
        ivProductTag.setOnClickListener {
            doShowProductInfo()
            analytic.clickProductTagOnLivePage(parentViewModel.channelId, parentViewModel.title)
        }
    }

    private fun setupInsets(view: View) {
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

    private fun startCountDown() {
        val animationProperty = PlayTimerCountDown.AnimationProperty.Builder()
                .setFullRotationInterval(3000)
                .setTextCountDownInterval(2000)
                .setTotalCount(3)
                .build()

        countdownTimer.visible()
        countdownTimer.startCountDown(animationProperty, object : PlayTimerCountDown.Listener {
            override fun onTick(milisUntilFinished: Long) {}

            override fun onFinish() {
                countdownTimer.gone()
            }
        })
    }

    override fun onDestroy() {
        try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        return showDialogWhenActionClose()
    }

    /**
     * render to ui
     */
    private fun showCounterDuration(timeLeft: String) {
        viewTimer.showCounterDuration(timeLeft)
    }

    private fun showTimeRemaining(minutesUntilFinished: Long) {
        viewTimer.showTimeRemaining(minutesUntilFinished)
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
        analytic.viewDialogExitOnLivePage(parentViewModel.channelId, parentViewModel.title)
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
                       parentViewModel.stopPushStream(shouldNavigate = true)
                       analytic.clickDialogExitOnLivePage(parentViewModel.channelId, parentViewModel.title)
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
                    analytic.clickDialogSeeReportOnLivePage(parentViewModel.channelId, parentViewModel.title)
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
        forceStopDialog.show()
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

    private fun handleLivePushInfo(state: LivePusherState) {
        if (!isVisible) return
        when (state) {
            is LivePusherState.Connecting -> showLoading(true)
            is LivePusherState.Started -> {
                showLoading(false)
                if (state.activeStatus == ApsaraLivePusherActiveStatus.Recover) {
                    showToaster(message = getString(R.string.play_live_broadcast_network_recover),
                            type = Toaster.TYPE_NORMAL)
                }
            }
            is LivePusherState.Stopped -> {
                showLoading(false)
                 if (state.shouldNavigate) navigateToSummary()
            }
            is LivePusherState.Error -> {
                showLoading(false)
                handleLivePushError(state.errorStatus)
            }
        }
    }

    private fun handleLivePushError(errorStatus: ApsaraLivePusherErrorStatus) {
        when(errorStatus) {
            ApsaraLivePusherErrorStatus.NetworkPoor -> {
                val errorMessage = getString(R.string.play_live_broadcast_network_poor)
                showToaster(message = errorMessage,
                        type = Toaster.TYPE_ERROR)
                analytic.viewErrorOnLivePage(parentViewModel.channelId, parentViewModel.title, errorMessage)
            }
            ApsaraLivePusherErrorStatus.NetworkLoss -> {
                val errorMessage = getString(R.string.play_live_broadcast_network_loss)
                showToaster(message = getString(R.string.play_live_broadcast_network_loss),
                        type = Toaster.TYPE_ERROR)
                analytic.viewErrorOnLivePage(parentViewModel.channelId, parentViewModel.title, errorMessage)
            }
            ApsaraLivePusherErrorStatus.ConnectFailed, ApsaraLivePusherErrorStatus.ReconnectFailed -> {
                val errorMessage = getString(R.string.play_live_broadcast_connect_fail)
                showToaster(message = getString(R.string.play_live_broadcast_connect_fail),
                        type = Toaster.TYPE_ERROR)
                analytic.viewErrorOnLivePage(parentViewModel.channelId, parentViewModel.title, errorMessage)
            }
            ApsaraLivePusherErrorStatus.SystemError -> {
                showToaster(message = getString(R.string.play_live_broadcast_connect_fail),
                        type = Toaster.TYPE_ERROR,duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_broadcast_try_again),
                        actionListener = View.OnClickListener {
                            parentViewModel.restartPushStream()
                        })
            }
        }
    }

    private fun getLoadingFragment(): LoadingDialogFragment {
        if (!::loadingFragment.isInitialized) {
            val setupClass = LoadingDialogFragment::class.java
            val fragmentFactory = childFragmentManager.fragmentFactory
            loadingFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as LoadingDialogFragment
        }
        return loadingFragment
    }

    private fun showLoading(isLoading: Boolean) {
        if (!getLoadingFragment().isAdded) return
        if (isLoading && !getLoadingFragment().isVisible)  getLoadingFragment().show(childFragmentManager)
        else getLoadingFragment().dismiss()
    }

    //region observe
    /**
     * Observe
     */
    private fun observeLiveInfo() {
        parentViewModel.observableLiveInfoState.observe(viewLifecycleOwner, Observer(::handleLivePushInfo))
    }

    private fun observeTotalViews() {
        parentViewModel.observableTotalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.observableTotalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }

    private fun observeLiveDuration() {
        parentViewModel.observableLiveDuration.observe(viewLifecycleOwner, EventObserver {
            when(it)  {
                is LivePusherTimerState.Active -> showCounterDuration(it.remainingTime)
                is LivePusherTimerState.AlmostFinish -> showTimeRemaining(it.minutesLeft)
                is LivePusherTimerState.Finish -> {
                    showDialogWhenTimeout()
                    analytic.viewDialogSeeReportOnLivePage(parentViewModel.channelId, parentViewModel.title)
                }
            }
        })
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

    private fun observeBannedEvent() {
        parentViewModel.observableBannedEvent.observe(viewLifecycleOwner,  EventObserver{ event ->
            showForceStopDialog(
                    title = event.title,
                    message = event.message,
                    buttonTitle = event.buttonTitle
            )
        })
    }
    //endregion

    companion object {
        const val KEY_START_COUNTDOWN = "start_count_down"
    }
}