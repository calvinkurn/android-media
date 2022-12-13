package com.tokopedia.kyc_centralized.ui.tokoKyc.alacarte

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class UserIdentificationInfoSimpleActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return UserIdentificationInfoSimpleFragment()
    }
}
