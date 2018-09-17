package com.tokopedia.profile.view.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * @author by milhamj on 9/17/18.
 */

class ProfileFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance() = ProfileFragment()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {

    }
}