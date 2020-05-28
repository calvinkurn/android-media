package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.di.PlayBroadcasterModule
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject


/**
 * Created by mzennis on 25/05/20.
 */
class PlayBroadcastInteractionFragment : PlayBaseBroadcastFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var parentViewModel: PlayBroadcastViewModel

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_broadcast_interaction, container, false)
        initView(view)
        return view
    }

    private fun initInjector() {
        DaggerPlayBroadcasterComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .playBroadcasterModule(PlayBroadcasterModule())
                .build()
                .inject(this)
    }

    private fun initView(view: View) {
        val tvTimeCounter = view.findViewById<Typography>(R.id.tv_time_counter)
    }

    companion object {

        const val EXTRA_CHANNEL_ID = "channel_id"
    }
}