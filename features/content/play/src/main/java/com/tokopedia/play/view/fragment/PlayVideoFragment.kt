package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.ui.video.VideoComponent

/**
 * Created by jegul on 29/11/19
 */
class PlayVideoFragment : BaseDaggerFragment() {

    companion object {

        fun newInstance(): PlayVideoFragment {
            return PlayVideoFragment()
        }
    }

    override fun getScreenName(): String = "Play Video"

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents(view as ViewGroup)
        initVideo()
    }

    private fun initComponents(container: ViewGroup) {
        VideoComponent(container, EventBusFactory.get(viewLifecycleOwner))
    }

    private fun initVideo() {
        
    }
}