package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifyprinciples.Typography
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

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        setupContent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_broadcast_interaction, container, false)
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
    }

    private fun setupContent() {
        arguments?.getString(KEY_CHANNEL_ID)?.let {
            channelId -> parentViewModel.getChannel(channelId)
        }
        arguments?.getString(KEY_INGEST_URL)?.let {
            ingestUrl -> parentViewModel.startPushBroadcast(ingestUrl)
        }
    }

    private fun observeCountDownDuration() {

    }

    private fun observeChannelInfo() {
        parentViewModel.channelInfo.observe(viewLifecycleOwner, Observer {
            parentViewModel.startPushBroadcast(ingestUrl = it.ingestUrl)
        })
    }

    private fun observeTotalViews() {
        // TODO("observe total views")
    }

    private fun observeTotalLikes() {
        // TODO("observe total likes")
    }

    companion object {

        const val KEY_CHANNEL_ID = "channel_id"
        const val KEY_INGEST_URL = "ingest_url"
    }
}