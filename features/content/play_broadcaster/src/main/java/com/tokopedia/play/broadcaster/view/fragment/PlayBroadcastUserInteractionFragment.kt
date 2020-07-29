package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherErrorType
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.util.PlayShareWrapper
import com.tokopedia.play.broadcaster.util.getDialog
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayProductLiveBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayMetricsView
import com.tokopedia.play.broadcaster.view.custom.PlayStatInfoView
import com.tokopedia.play.broadcaster.view.custom.PlayTimerView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.ChatListPartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by mzennis on 25/05/20.
 */
class PlayBroadcastUserInteractionFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
): PlayBaseBroadcastFragment() {

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var viewTimer: PlayTimerView
    private lateinit var viewStatInfo: PlayStatInfoView
    private lateinit var ivShareLink: AppCompatImageView
    private lateinit var ivProductTag: AppCompatImageView
    private lateinit var pmvMetrics: PlayMetricsView

    private lateinit var chatListView: ChatListPartialView
    private lateinit var productLiveBottomSheet: PlayProductLiveBottomSheet

    private lateinit var exitDialog: DialogUnify

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
        setupContent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeChannelInfo()
        observeLiveInfo()
        observeTotalViews()
        observeTotalLikes()
        observeChatList()
        observeMetrics()
        observeNetworkConnectionDuringLive()
    }

    private fun initView(view: View) {
        viewTimer = view.findViewById(R.id.view_timer)
        viewStatInfo = view.findViewById(R.id.view_stat_info)
        ivShareLink = view.findViewById(R.id.iv_share_link)
        ivProductTag = view.findViewById(R.id.iv_product_tag)
        pmvMetrics = view.findViewById(R.id.pmv_metrics)

        chatListView = ChatListPartialView(view as ViewGroup)
    }

    private fun setupView() {
        broadcastCoordinator.setupTitle("")
        broadcastCoordinator.setupCloseButton(getString(R.string.play_action_bar_end))

        ivShareLink.setOnClickListener{ doCopyShareLink() }
        ivProductTag.setOnClickListener { doShowProductInfo() }
    }

    private fun setupContent() {
        arguments?.getString(KEY_INGEST_URL)?.run {
            if (this.isNotEmpty()) startLiveStreaming(this)
        }
    }

    override fun onResume() {
        super.onResume()
        parentViewModel.resumePushStream()
    }

    override fun onPause() {
        super.onPause()
        parentViewModel.pausePushStream()
    }

    override fun onDestroy() {
        parentViewModel.getPlayPusher().destroy()
        try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        showDialogWhenActionClose()
        return true
    }

    private fun startLiveStreaming(ingestUrl: String) {
        parentViewModel.startPushBroadcast(ingestUrl)
    }

    private fun stopLiveStreaming() {
        parentViewModel.stopPushBroadcast()
    }

    /**
     * render to ui
     */
    private fun showTimeLeft(timeLeft: String) {
        viewTimer.showTimeLeft(timeLeft)
    }

    private fun showTimeRunOut(minutesUntilFinished: Long) {
        viewTimer.showTimeRunOut(minutesUntilFinished)
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

    private fun setNewMetric(metric: PlayMetricUiModel) {
        pmvMetrics.show(metric)
    }

    private fun getProductLiveBottomSheet(): PlayProductLiveBottomSheet {
        if (!::productLiveBottomSheet.isInitialized) {
            val setupClass = PlayProductLiveBottomSheet::class.java
            val fragmentFactory = childFragmentManager.fragmentFactory
            productLiveBottomSheet = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayProductLiveBottomSheet
        }
        return productLiveBottomSheet
    }

    private fun showDialogWhenActionClose() {
        getExitDialog().show()
    }

    private fun getExitDialog(): DialogUnify {
        if (!::exitDialog.isInitialized) {
           exitDialog =  requireContext().getDialog(
                   actionType = DialogUnify.HORIZONTAL_ACTION,
                   title = getString(R.string.play_live_broadcast_dialog_end_title),
                   desc = getString(R.string.play_live_broadcast_dialog_end_desc),
                   primaryCta = getString(R.string.play_live_broadcast_dialog_end_primary),
                   primaryListener = { dialog -> dialog.dismiss() },
                   secondaryCta = getString(R.string.play_broadcast_exit),
                   secondaryListener = { dialog ->
                       dialog.dismiss()
                       doEndStreaming()
                   }
           )
        }
        return exitDialog
    }

    private fun showDialogWhenTimeout() {
        requireContext().getDialog(
                title = getString(R.string.play_live_broadcast_dialog_end_timeout_title),
                desc = getString(R.string.play_live_broadcast_dialog_end_timeout_desc),
                primaryCta = getString(R.string.play_live_broadcast_dialog_end_timeout_primary),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    navigateToSummary()
                }
        ).show()
    }

    private fun showDialogContinueLiveStreaming(channelId: String) {
        requireContext().getDialog(
                actionType = DialogUnify.HORIZONTAL_ACTION,
                title = getString(R.string.play_dialog_continue_live_title),
                desc = getString(R.string.play_dialog_continue_live_desc),
                primaryCta = getString(R.string.play_next),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    startLiveStreaming(channelId)
                },
                secondaryCta = getString(R.string.play_broadcast_end),
                secondaryListener = { dialog ->
                    dialog.dismiss()
                    doEndStreaming()
                }
        ).show()
    }

    private fun showDialogWhenActiveOnOtherDevices() {
        requireContext().getDialog(
                title = getString(R.string.play_dialog_error_active_other_devices_title),
                desc = getString(R.string.play_dialog_error_active_other_devices_desc),
                primaryCta = getString(R.string.play_broadcast_exit),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    activity?.finish()
                }
        ).show()
    }

    private fun showToast(
            message: String,
            type: Int,
            duration: Int = Toaster.LENGTH_LONG,
            actionLabel: String = "",
            actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        view?.let { view ->
            if (actionLabel.isNotEmpty()) Toaster.toasterCustomCtaWidth = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl8)
            Toaster.toasterCustomBottomHeight =  ivShareLink.height + resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
            Toaster.make(view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionLabel,
                    clickListener = actionListener)
        }
    }

    private fun doCopyShareLink() {
        parentViewModel.shareInfo?.let { shareInfo ->
            PlayShareWrapper.doCopyShareLink(requireContext(), shareInfo) {
                showToast(message = getString(R.string.play_live_broadcast_share_link_copied),
                        type = Toaster.TYPE_NORMAL,
                        actionLabel = getString(R.string.play_ok))
            }
        }
    }

    private fun doShowProductInfo() {
        getProductLiveBottomSheet().show(childFragmentManager)
    }

    private fun doEndStreaming() {
        stopLiveStreaming()
        navigateToSummary()
    }

    private fun navigateToSummary() {
        broadcastCoordinator.navigateToFragment(PlayBroadcastSummaryFragment::class.java)
    }

    private fun handleChannelInfo(channelInfo: ChannelInfoUiModel) {
        when(channelInfo.status) {
            PlayChannelStatus.UnStarted -> startLiveStreaming(channelInfo.ingestUrl)
            PlayChannelStatus.Active -> showDialogWhenActiveOnOtherDevices()
            PlayChannelStatus.Pause -> showDialogContinueLiveStreaming(channelInfo.channelId)
            PlayChannelStatus.Finish -> navigateToSummary()
        }
    }

    private fun handleLiveInfo(pusherInfoState: PlayPusherInfoState) {
        when (pusherInfoState) {
            is PlayPusherInfoState.Active -> showTimeLeft(pusherInfoState.timeLeft)
            is PlayPusherInfoState.AlmostFinish -> showTimeRunOut(pusherInfoState.minutesUntilFinished)
            is PlayPusherInfoState.Finish -> {
                stopLiveStreaming()
                showDialogWhenTimeout()
            }
            is PlayPusherInfoState.Error -> handleLiveError(pusherInfoState.errorType)
        }
    }

    private fun handleLiveNetworkInfo(pusherNetworkState: PlayPusherNetworkState) {
        when(pusherNetworkState) {
            is PlayPusherNetworkState.Recover -> {
                showToast(message = getString(R.string.play_live_broadcast_network_recover),
                        type = Toaster.TYPE_NORMAL)
            }
            is PlayPusherNetworkState.Poor -> {
                showToast(message = getString(R.string.play_live_broadcast_network_loss),
                        type = Toaster.TYPE_ERROR)
            }
            is PlayPusherNetworkState.Loss -> {
                showToast(message = getString(R.string.play_live_broadcast_network_loss),
                        type = Toaster.TYPE_ERROR,
                        duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_broadcast_try_again),
                        actionListener = View.OnClickListener {
                            parentViewModel.getPlayPusher().restartPush()
                        })
            }
        }
    }

    private fun handleLiveError(errorType: PlayPusherErrorType) {
        when(errorType) {
            PlayPusherErrorType.UnSupportedDevice -> {
                // TODO("handle unsupported devices")
                // Perangkat tidak mendukung\n layanan siaran live streaming
                // Layanan live streaming tidak didukung pada perangkat Anda.
                // showDialogWhenUnSupportedDevices()
            }
            PlayPusherErrorType.ReachMaximumDuration -> doEndStreaming()
        }
    }

    //region observe
    /**
     * Observe
     */
    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner, Observer(::handleChannelInfo))
    }

    private fun observeLiveInfo() {
        parentViewModel.observableLiveInfoState.observe(viewLifecycleOwner, Observer(::handleLiveInfo))
    }

    private fun observeTotalViews() {
        parentViewModel.observableTotalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.observableTotalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
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
        parentViewModel.observableNewMetric.observe(viewLifecycleOwner, EventObserver(::setNewMetric))
    }

    private fun observeNetworkConnectionDuringLive() {
        parentViewModel.observableLiveNetworkState.observe(viewLifecycleOwner, EventObserver(::handleLiveNetworkInfo))
    }
    //endregion

    companion object {
        const val KEY_INGEST_URL = "ingest_url"
    }
}