package com.tokopedia.loginHelper.presentation.addEditAccount.fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.di.component.DaggerLoginHelperComponent

class LoginHelperSearchAccountFragment: BaseDaggerFragment() {
    override fun getScreenName(): String {
        return context?.resources?.getString(com.tokopedia.loginHelper.R.string.login_helper_accounts_header_title)
            .toBlankOrString()
    }

    override fun initInjector() {
        DaggerLoginHelperComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }
}
