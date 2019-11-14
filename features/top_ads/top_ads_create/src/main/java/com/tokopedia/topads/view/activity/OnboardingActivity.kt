package com.tokopedia.topads.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.view.fragment.OboardingFragment

/**
 * Author errysuprayogi on 29,October,2019
 */
class OnboardingActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return OboardingFragment.newInstance()
    }
}
