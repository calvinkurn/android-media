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
        val view = inflater.inflate(R.layout.fragment_play_live_broadcast, container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun setupContent() {
        arguments?.getString(KEY_CHANNEL_ID)?.let {
            channelId -> parentViewModel.getChannel(channelId)
        }
        arguments?.getString(KEY_INGEST_URL)?.let {
            ingestUrl -> parentViewModel.startPushBroadcast(ingestUrl)
        }
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

    private fun setupDialog(
            title: String,
            description: String,
            primaryCta: String,
            primaryCtaClick: () -> Unit,
            secondaryCta: String = "",
            secondaryCtaClick: () -> Unit = {}
    ): DialogUnify? {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(title)
            dialog.setDescription(description)
            dialog.setPrimaryCTAText(primaryCta)
            dialog.setPrimaryCTAClickListener(primaryCtaClick)
            if (secondaryCta.isNotEmpty()) {
                dialog.setSecondaryCTAText(secondaryCta)
                dialog.setSecondaryCTAClickListener(secondaryCtaClick)
            }
            return dialog
        }
        return null
    }

    //region observe
    /**
     * Observe
     */
    private fun observeCountDownDuration() {
        parentViewModel.observableLiveInfoState.observe(viewLifecycleOwner, EventObserver{
            if (it is PlayPusherInfoState.Active) {
                setCountDownTimer(it.millisUntilFinished)
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