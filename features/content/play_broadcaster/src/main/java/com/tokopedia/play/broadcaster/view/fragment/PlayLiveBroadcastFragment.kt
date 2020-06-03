package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel
import com.tokopedia.play.broadcaster.util.event.EventObserver
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by mzennis on 25/05/20.
 */
class PlayLiveBroadcastFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val fragmentFactory: FragmentFactory
): PlayBaseBroadcastFragment() {

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var tvTimeCounter: Typography
    private lateinit var tvTotalView: Typography
    private lateinit var tvTotalLike: Typography

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        setupContent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_live_broadcast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeChannelInfo()
        observeCountDownDuration()
        observeTotalViews()
        observeTotalLikes()
    }

    private fun initView(view: View) {
        tvTimeCounter = view.findViewById(R.id.tv_time_counter)
        tvTotalView = view.findViewById(R.id.tv_total_views)
        tvTotalLike = view.findViewById(R.id.tv_total_likes)
    }

    private fun setupView() {
        broadcastCoordinator.setupTitle("")
        broadcastCoordinator.setupCloseButton(getString(R.string.play_action_bar_end))
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

    private fun setCountDownTimer(millisUntilFinish: Long) {
        tvTimeCounter.text = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinish),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinish) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinish))
        )
    }

    private fun setTotalView(totalView: TotalViewUiModel) {
        tvTotalView.text = totalView.totalView
    }

    private fun setTotalLike(totalLike: TotalLikeUiModel) {
        tvTotalLike.text = totalLike.totalLike
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
        parentViewModel.observableLiveInfoState.observe(viewLifecycleOwner, EventObserver{
            when (it) {
                is PlayPusherInfoState.Active -> {
                    setCountDownTimer(it.millisUntilFinished)
                }
                is PlayPusherInfoState.Finish -> {
                    parentViewModel.stopPushBroadcast()
                    showDialogWhenTimeout()
                }
            }
        })
    }

    private fun observeChannelInfo() {
        parentViewModel.channelInfo.observe(viewLifecycleOwner, Observer {
            parentViewModel.startPushBroadcast(ingestUrl = it.ingestUrl)
        })
    }

    private fun observeTotalViews() {
        parentViewModel.totalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.totalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }
    //endregion

    companion object {

        const val KEY_CHANNEL_ID = "channel_id"
        const val KEY_INGEST_URL = "ingest_url"
    }
}