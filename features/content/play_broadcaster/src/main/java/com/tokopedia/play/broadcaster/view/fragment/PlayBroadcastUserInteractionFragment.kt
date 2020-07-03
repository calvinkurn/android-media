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
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherErrorType
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.PlayShareWrapper
import com.tokopedia.play.broadcaster.util.getDialog
import com.tokopedia.play.broadcaster.util.showToaster
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayProductLiveBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayMetricsView
import com.tokopedia.play.broadcaster.view.custom.PlayStatInfoView
import com.tokopedia.play.broadcaster.view.custom.PlayTimerCountDown
import com.tokopedia.play.broadcaster.view.custom.PlayTimerView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.ChatListPartialView
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
        private val viewModelFactory: ViewModelFactory
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

    private var toasterBottomMargin = 0

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
        setupContent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeChannelInfo()
        observeLiveInfo()
        observeLiveDuration()
        observeTotalViews()
        observeTotalLikes()
        observeChatList()
        observeMetrics()
        observeNetworkConnectionDuringLive()
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

        ivShareLink.setOnClickListener{ doCopyShareLink() }
        ivProductTag.setOnClickListener { doShowProductInfo() }
    }

    private fun setupInsets(view: View) {
        viewTimer.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.requestLayout()
            }
        }

        ivShareLink.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.requestLayout()
            }
        }
    }

    private fun setupContent() {
        val ingestUrl = arguments?.getString(KEY_INGEST_URL)
        if (ingestUrl != null && ingestUrl.isNotEmpty()) {
            startCountDown(ingestUrl)
        } else {
            parentViewModel.fetchChannelData()
        }
    }

    private fun startCountDown(ingestUrl: String) {
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
                startLiveStreaming(ingestUrl)
            }
        })
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
        return showDialogWhenActionClose()
    }

    private fun startLiveStreaming(ingestUrl: String) {
        parentViewModel.startPushStream(ingestUrl)
        // TODO remove mock
        parentViewModel.mockChatList()
        parentViewModel.mockMetrics()
    }

    private fun stopLiveStreaming() {
        parentViewModel.stopPushStream()
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

    private fun showDialogWhenActionClose(): Boolean {
        return if (parentViewModel.allPermissionGranted()) {
            getExitDialog().show()
            true
        }
        else false
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

    private fun showDialogContinueLiveStreaming(ingestUrl: String) {
        requireContext().getDialog(
                actionType = DialogUnify.HORIZONTAL_ACTION,
                title = getString(R.string.play_dialog_continue_live_title),
                desc = getString(R.string.play_dialog_continue_live_desc),
                primaryCta = getString(R.string.play_next),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    startLiveStreaming(ingestUrl)
                },
                secondaryCta = getString(R.string.play_broadcast_end),
                secondaryListener = { dialog ->
                    dialog.dismiss()
                    doEndStreaming()
                }
        ).show()
    }

    private fun showToaster(
            message: String,
            type: Int,
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
        parentViewModel.shareInfo?.let { shareInfo ->
            PlayShareWrapper.doCopyShareLink(requireContext(), shareInfo) {
                showToaster(message = getString(R.string.play_live_broadcast_share_link_copied),
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
        when (channelInfo.status) {
            PlayChannelStatus.Active, PlayChannelStatus.Live -> startLiveStreaming(channelInfo.ingestUrl)
            PlayChannelStatus.Pause -> showDialogContinueLiveStreaming(channelInfo.ingestUrl)
            PlayChannelStatus.Stop -> doEndStreaming()
            else -> {}
        }
    }

    private fun handleLiveNetworkInfo(pusherNetworkState: PlayPusherNetworkState) {
        when(pusherNetworkState) {
            is PlayPusherNetworkState.Recover -> {
                showToaster(message = getString(R.string.play_live_broadcast_network_recover),
                        type = Toaster.TYPE_NORMAL)
            }
            is PlayPusherNetworkState.Poor -> {
                showToaster(message = getString(R.string.play_live_broadcast_network_loss),
                        type = Toaster.TYPE_ERROR)
            }
            is PlayPusherNetworkState.Loss -> {
                showToaster(message = getString(R.string.play_live_broadcast_network_loss),
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
            is PlayPusherErrorType.UnSupportedDevice -> {
                // TODO("handle unsupported devices")
                // Perangkat tidak mendukung\n layanan siaran live streaming
                // Layanan live streaming tidak didukung pada perangkat Anda.
                // showDialogWhenUnSupportedDevices()
            }
            is PlayPusherErrorType.ReachMaximumPauseDuration -> doEndStreaming()
            is PlayPusherErrorType.Throwable -> if (GlobalConfig.DEBUG) showToaster(errorType.message, type = Toaster.TYPE_ERROR)
        }
    }

    //region observe
    /**
     * Observe
     */
    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Loading -> loadingView.show()
                is NetworkResult.Success -> {
                    loadingView.hide()
                    handleChannelInfo(it.data)
                }
                is NetworkResult.Fail -> {
                    loadingView.hide()
                    view?.showToaster(
                            message = it.error.localizedMessage,
                            type = Toaster.TYPE_ERROR,
                            duration = Toaster.LENGTH_INDEFINITE
                    )
                }
            }
        })
    }

    private fun observeLiveInfo() {
        parentViewModel.observableLiveInfoState.observe(viewLifecycleOwner, EventObserver{
           if (it is PlayPusherInfoState.Error) handleLiveError(it.errorType)
        })
    }

    private fun observeTotalViews() {
        parentViewModel.observableTotalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.observableTotalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }

    private fun observeLiveDuration() {
        parentViewModel.observableLiveDuration.observe(viewLifecycleOwner, Observer {
            it.handleLiveDuration(
                    onActive = { duration -> showCounterDuration(duration) },
                    onAlmostFinish = { remaining -> showTimeRemaining(remaining) },
                    onFinish = {
                        stopLiveStreaming()
                        showDialogWhenTimeout()
                    }
            )
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