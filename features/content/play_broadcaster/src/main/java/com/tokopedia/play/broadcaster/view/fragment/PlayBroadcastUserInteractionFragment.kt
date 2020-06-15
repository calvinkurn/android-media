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
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.util.PlayShareWrapper
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

        observeCountDownDuration()
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
//        arguments?.getString(KEY_CHANNEL_ID)?.let {
//            channelId -> parentViewModel.getChannel(channelId)
//        }
//        arguments?.getString(KEY_INGEST_URL)?.let {
//            ingestUrl -> parentViewModel.startPushBroadcast(ingestUrl)
//        }
        parentViewModel.startPushBroadcast("rtmp://192.168.0.110:1935/stream/")
    }

    override fun onDestroy() {
        try { Toaster.snackBar.dismiss() } catch (e: Exception) {}
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        showDialogWhenActionClose()
        return true
    }

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
           exitDialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
               setTitle(getString(R.string.play_live_broadcast_dialog_end_title))
               setDescription(getString(R.string.play_live_broadcast_dialog_end_desc))
               setPrimaryCTAText(getString(R.string.play_live_broadcast_dialog_end_primary))
               setSecondaryCTAText(getString(R.string.play_live_broadcast_dialog_end_secondary))
               setPrimaryCTAClickListener { dismiss() }
               setSecondaryCTAClickListener {
                   dismiss()
                   doEndStreaming()
               }
               setCancelable(false)
               setOverlayClose(false)
           }
        }
        return exitDialog
    }

    private fun showDialogWhenTimeout() {
        context?.let {
            val dialog =  DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.play_live_broadcast_dialog_end_timeout_title))
            dialog.setDescription(getString(R.string.play_live_broadcast_dialog_end_timeout_desc))
            dialog.setPrimaryCTAText(getString(R.string.play_live_broadcast_dialog_end_timeout_primary))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                navigateToSummary()
            }
            dialog.setCancelable(false)
            dialog.setOverlayClose(false)
            dialog.show()
        }
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
        parentViewModel.stopPushBroadcast()
        navigateToSummary()
    }

    private fun navigateToSummary() {
        broadcastCoordinator.navigateToFragment(PlayBroadcastSummaryFragment::class.java, Bundle().apply {
//            putString(PlayBroadcastSummaryFragment.KEY_LIVE_DURATION, tvTimeCounter.text.toString())
        })
    }

    //region observe
    /**
     * Observe
     */
    private fun observeCountDownDuration() {
        parentViewModel.observableLiveInfoState.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is PlayPusherInfoState.Active -> {
                    showTimeLeft(it.timeLeft)
                }
                is PlayPusherInfoState.AlmostFinish -> {
                    showTimeRunOut(it.minutesUntilFinished)
                }
                is PlayPusherInfoState.Finish -> {
                    parentViewModel.stopPushBroadcast()
                    showDialogWhenTimeout()
                }
            }
        })
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
        parentViewModel.observableLiveNetworkState.observe(viewLifecycleOwner, EventObserver{
            when(it) {
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
        })
    }
    //endregion

    companion object {

        const val KEY_CHANNEL_ID = "channel_id"
        const val KEY_INGEST_URL = "ingest_url"
    }
}