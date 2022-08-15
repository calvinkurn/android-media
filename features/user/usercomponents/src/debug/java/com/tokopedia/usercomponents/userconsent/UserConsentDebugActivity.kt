package com.tokopedia.usercomponents.userconsent

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

open class UserConsentDebugActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return UserConsentDebugFragment()
    }
}