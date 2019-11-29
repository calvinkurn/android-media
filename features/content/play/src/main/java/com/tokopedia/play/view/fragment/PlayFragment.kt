package com.tokopedia.play.view.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

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


}