package com.tokopedia.videoplayer.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * @author by yfsx on 20/03/19.
 */
class MultipleVideoPlayerFragment: BaseDaggerFragment() {

    companion object {
        fun getInstance(bundle: Bundle): MultipleVideoPlayerFragment {
            val fragment = MultipleVideoPlayerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
    }
}