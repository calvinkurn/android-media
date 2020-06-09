package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.util.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.ChatListPartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by mzennis on 25/05/20.
 */
class PlayBroadcastUserInteractionFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val fragmentFactory: FragmentFactory
): PlayBaseBroadcastFragment() {

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var tvTimeCounter: Typography
    private lateinit var tvTimeCounterEnd: AppCompatTextView
    private lateinit var tvTotalView: Typography
    private lateinit var tvTotalLike: Typography
    private lateinit var ivShareLink: AppCompatImageView

    private lateinit var chatListView: ChatListPartialView

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        setupContent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcast_user_interaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        observeChannelInfo() // TODO("double check")
        observeCountDownDuration()
        observeTotalViews()
        observeTotalLikes()
        observeChatList()
    }

    private fun initView(view: View) {
        tvTimeCounter = view.findViewById(R.id.tv_time_counter)
        tvTimeCounterEnd = view.findViewById(R.id.tv_time_counter_end)
        tvTotalView = view.findViewById(R.id.tv_total_views)
        tvTotalLike = view.findViewById(R.id.tv_total_likes)
        ivShareLink = view.findViewById(R.id.iv_share_link)

        chatListView = ChatListPartialView(view as ViewGroup, R.id.cl_chat_list)
    }

    private fun setupView() {
        broadcastCoordinator.setupTitle("")
        broadcastCoordinator.setupCloseButton(getString(R.string.play_action_bar_end))

        ivShareLink.setOnClickListener{ doCopyShareLink() }
    }

    private fun setupContent() {
        arguments?.getString(KEY_CHANNEL_ID)?.let {
            channelId -> parentViewModel.getChannel(channelId)
        }
        arguments?.getString(KEY_INGEST_URL)?.let {
            ingestUrl -> parentViewModel.startPushBroadcast(ingestUrl)
        }
    }

    override fun onBackPressed(): Boolean {
        showDialogWhenActionClose()
        return true
    }

    private fun setCountDownTimer(
            elapsedTime: String,
            minutesUntilFinished: Long,
            secondsUntilFinished: Long
    ) {
        if (shouldShowWhenFiveOrTwoMinutesLeft(minutesUntilFinished, secondsUntilFinished)) {
            tvTimeCounterEnd.show()
            tvTimeCounter.invisible()
        } else {
            tvTimeCounterEnd.invisible()
            tvTimeCounter.show()
        }
        tvTimeCounterEnd.text = getString(R.string.play_live_broadcast_time_left, minutesUntilFinished)
        tvTimeCounter.text = elapsedTime
    }

    private fun shouldShowWhenFiveOrTwoMinutesLeft(minutesUntilFinished: Long,
                                                   secondsUntilFinished: Long): Boolean =
            (minutesUntilFinished == 2L || minutesUntilFinished == 5L) && secondsUntilFinished in 0..3

    private fun setTotalView(totalView: TotalViewUiModel) {
        tvTotalView.text = totalView.totalView
    }

    private fun setTotalLike(totalLike: TotalLikeUiModel) {
        tvTotalLike.text = totalLike.totalLike
    }

    private fun setChatList(chatList: List<PlayChatUiModel>) {
        chatListView.setChatList(chatList)
    }

    private fun setNewChat(chat: PlayChatUiModel) {
        chatListView.showNewChat(chat)
    }

    private fun showDialogWhenActionClose() {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
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
            }.show()
        }
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

    private fun doCopyShareLink() {
        parentViewModel.channelInfo?.let { channelInfo ->
            PlayShareWrapper.doCopyShareLink(requireContext(), channelInfo) {
                Toaster.make(requireView(),
                        text = getString(R.string.play_live_broadcast_share_link_copied),
                        duration = Toaster.LENGTH_LONG,
                        actionText =  getString(R.string.play_live_broadcast_share_link_ok))
            }
        }
    }

    private fun doEndStreaming() {
        parentViewModel.stopPushBroadcast()
        navigateToSummary()
    }

    private fun navigateToSummary() {
        broadcastCoordinator.navigateToFragment(PlayBroadcastSummaryFragment::class.java)
    }

    //region observe
    /**
     * Observe
     */
    private fun observeCountDownDuration() {
        parentViewModel.observableLiveInfoState.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is PlayPusherInfoState.Active -> {
                    setCountDownTimer(it.elapsedTime, it.minutesUntilFinished, it.secondsUntilFinished)
                }
                is PlayPusherInfoState.Finish -> {
                    parentViewModel.stopPushBroadcast()
                    showDialogWhenTimeout()
                }
            }
        })
    }

//    private fun observeChannelInfo() {
//        parentViewModel.channelInfo.observe(viewLifecycleOwner, Observer {
//            parentViewModel.startPushBroadcast(ingestUrl = it.ingestUrl)
//        })
//    }

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
    //endregion

    companion object {

        const val KEY_CHANNEL_ID = "channel_id"
        const val KEY_INGEST_URL = "ingest_url"
    }
}