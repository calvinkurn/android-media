package com.tokopedia.home_account.explicitprofile.fakes

import androidx.fragment.app.Fragment
import com.tokopedia.home_account.explicitprofile.features.ExplicitProfileActivity

class FakeExplicitProfileActivity: ExplicitProfileActivity() {

    override fun getNewFragment(): Fragment {
        return FakeExplicitProfileFragment()
    }
}