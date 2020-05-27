package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.di.PlayBroadcasterModule
import com.tokopedia.play.broadcaster.view.viewmodel.PlayPrepareBroadcastViewModel
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastSetupFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var parentViewModel: PlayPrepareBroadcastViewModel

    override fun getScreenName(): String = "Play Broadcast Setup Page"

    override fun initInjector() {
        DaggerPlayBroadcasterComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .playBroadcasterModule(PlayBroadcasterModule())
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayPrepareBroadcastViewModel::class.java)
    }
}