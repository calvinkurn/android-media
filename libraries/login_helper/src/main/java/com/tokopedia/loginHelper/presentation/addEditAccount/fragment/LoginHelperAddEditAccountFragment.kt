package com.tokopedia.loginHelper.presentation.addEditAccount.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.di.component.DaggerLoginHelperComponent
import com.tokopedia.loginHelper.domain.entity.PageMode
import com.tokopedia.loginHelper.util.BundleConstants

class LoginHelperAddEditAccountFragment : BaseDaggerFragment() {

    private val pageMode by lazy {
        arguments?.getParcelable(BundleConstants.LOGIN_HELPER_ADD_EDIT_ACCOUNT_MODE) as? PageMode
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getScreenName(): String {
        return context?.resources?.getString(com.tokopedia.loginHelper.R.string.login_helper_add_edit_header_title)
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

    companion object {
        fun newInstance(pageMode: PageMode): LoginHelperAddEditAccountFragment {
            return LoginHelperAddEditAccountFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstants.LOGIN_HELPER_ADD_EDIT_ACCOUNT_MODE, pageMode)
                }
            }
        }
    }
}
