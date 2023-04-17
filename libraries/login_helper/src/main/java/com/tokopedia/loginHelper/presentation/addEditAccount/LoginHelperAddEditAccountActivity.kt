package com.tokopedia.loginHelper.presentation.addEditAccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.domain.entity.PageMode
import com.tokopedia.loginHelper.presentation.addEditAccount.fragment.LoginHelperAddEditAccountFragment
import com.tokopedia.loginHelper.util.BundleConstants

class LoginHelperAddEditAccountActivity : BaseSimpleActivity() {
    override fun getNewFragment() = LoginHelperAddEditAccountFragment.newInstance(
        pageMode ?: PageMode.ADD
    )
    override fun getLayoutRes() = R.layout.activity_login_helper
    override fun getParentViewResourceID() = R.id.container

    companion object {
        @JvmStatic
        fun buildAddAccountModeIntent(
            context: Context
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstants.LOGIN_HELPER_ADD_EDIT_ACCOUNT_MODE, PageMode.ADD)
            }

            val intent = Intent(context, LoginHelperAddEditAccountActivity::class.java)
            intent.putExtras(bundle)

            context.startActivity(intent)
        }
    }

    private val pageMode by lazy {
        intent?.extras?.getParcelable(BundleConstants.LOGIN_HELPER_ADD_EDIT_ACCOUNT_MODE) as? PageMode
    }
}
