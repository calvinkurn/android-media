package com.tokopedia.interestpick.view.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * @author by milhamj on 03/09/18.
 */
class InterestPickFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance() = InterestPickFragment()
    }

    override fun getScreenName() = null

    override fun initInjector() {

    }
}