package com.tokopedia.interestpick.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.interestpick.view.fragment.InterestPickFragment

/**
 * @author by milhamj on 03/09/18.
 */
class InterestPickActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return InterestPickFragment.createInstance()
    }
}