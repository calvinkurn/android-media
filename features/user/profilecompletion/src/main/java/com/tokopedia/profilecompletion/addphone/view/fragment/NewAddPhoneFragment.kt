package com.tokopedia.profilecompletion.addphone.view.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent

class NewAddPhoneFragment: BaseDaggerFragment() {

    override fun getScreenName(): String =
        NewAddPhoneFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    companion object {
        fun newInstance() = NewAddPhoneFragment()
    }
}
