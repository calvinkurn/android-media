package com.tokopedia.loginHelper.presentation.addEditAccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.loginHelper.R
import com.tokopedia.loginHelper.domain.entity.PageMode
import com.tokopedia.loginHelper.presentation.addEditAccount.fragment.LoginHelperAddEditAccountFragment
import com.tokopedia.loginHelper.util.BundleConstants

class LoginHelperAddEditAccountActivity : BaseSimpleActivity() {
    override fun getNewFragment() = LoginHelperAddEditAccountFragment.newInstance(
        pageMode ?: PageMode.ADD,
        email?.toBlankOrString(),
        password?.toBlankOrString(),
        tribe?.toBlankOrString(),
        userId ?: 0
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

        fun buildEditAccountModeIntent(
            context: Context,
            email: String?,
            password: String?,
            tribe: String?,
            id: Long = 0
        ) {
            val bundle = Bundle().apply {
                putParcelable(BundleConstants.LOGIN_HELPER_ADD_EDIT_ACCOUNT_MODE, PageMode.EDIT)
                putString(BundleConstants.LOGIN_HELPER_EMAIL, email)
                putString(BundleConstants.LOGIN_HELPER_PASSWORD, password)
                putString(BundleConstants.LOGIN_HELPER_TRIBE, tribe)
                putLong(BundleConstants.LOGIN_HELPER_USER_ID, id)
            }

            val intent = Intent(context, LoginHelperAddEditAccountActivity::class.java)
            intent.putExtras(bundle)

            context.startActivity(intent)
        }
    }

    private val pageMode by lazy {
        intent?.extras?.getParcelable(BundleConstants.LOGIN_HELPER_ADD_EDIT_ACCOUNT_MODE) as? PageMode
    }

    private val email by lazy {
        intent?.extras?.getString(BundleConstants.LOGIN_HELPER_EMAIL)
    }

    private val password by lazy {
        intent?.extras?.getString(BundleConstants.LOGIN_HELPER_PASSWORD)
    }

    private val tribe by lazy {
        intent?.extras?.getString(BundleConstants.LOGIN_HELPER_TRIBE)
    }

    private val userId by lazy {
        intent?.extras?.getLong(BundleConstants.LOGIN_HELPER_USER_ID)
    }
}
