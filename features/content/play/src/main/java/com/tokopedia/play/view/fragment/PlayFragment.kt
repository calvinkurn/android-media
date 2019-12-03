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
class PlayFragment : BaseDaggerFragment() {

    companion object {

        fun newInstance(): PlayFragment {
            return PlayFragment()
        }
    }

    override fun getScreenName(): String = "Play"

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireFragmentManager().beginTransaction()
                .replace(R.id.fl_video, PlayVideoFragment.newInstance())
                .commit()

        requireFragmentManager().beginTransaction()
                .replace(R.id.fl_interaction, PlayInteractionFragment.newInstance())
                .commit()
    }
}