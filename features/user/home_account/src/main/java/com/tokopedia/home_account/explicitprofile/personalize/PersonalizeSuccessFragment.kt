package com.tokopedia.home_account.explicitprofile.personalize

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment

class PersonalizeSuccessFragment: TkpdBaseV4Fragment() {
    override fun getScreenName(): String = ""

    companion object {
        fun createInstance(): Fragment = PersonalizeSuccessFragment()
    }
}
