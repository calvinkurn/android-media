package com.tokopedia.usercomponents.userconsent.fakes

import androidx.fragment.app.Fragment
import com.tokopedia.usercomponents.userconsent.UserConsentDebugActivity

class FakeUserConsentDebugActivity: UserConsentDebugActivity() {

    override fun getFragment(): Fragment {
        return FakeUserConsentDebugFragment()
    }
}