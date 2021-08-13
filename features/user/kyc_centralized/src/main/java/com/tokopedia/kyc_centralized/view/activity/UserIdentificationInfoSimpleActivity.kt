package com.tokopedia.kyc_centralized.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kyc_centralized.view.fragment.UserIdentificationInfoSimpleFragment

class UserIdentificationInfoSimpleActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return UserIdentificationInfoSimpleFragment()
    }
}