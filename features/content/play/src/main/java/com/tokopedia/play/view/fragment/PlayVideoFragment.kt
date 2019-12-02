package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.R

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
}