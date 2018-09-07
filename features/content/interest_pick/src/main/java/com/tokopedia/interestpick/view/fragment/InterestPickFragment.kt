package com.tokopedia.interestpick.view.fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.interestpick.di.DaggerInterestPickComponent

/**
 * @author by milhamj on 03/09/18.
 */

class InterestPickFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance() = InterestPickFragment()
    }

    override fun getScreenName() = null

    override fun initInjector() {
        activity?.let {
            (it.applicationContext as BaseMainApplication).baseAppComponent
        }.let {
            DaggerInterestPickComponent.builder()
                    .baseAppComponent(it)
                    .build()
                    .inject(this)
        }
    }
}