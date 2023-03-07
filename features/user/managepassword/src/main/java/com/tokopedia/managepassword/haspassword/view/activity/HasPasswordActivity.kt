package com.tokopedia.managepassword.haspassword.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.di.DaggerManagePasswordComponent
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.managepassword.di.module.ManagePasswordModule
import com.tokopedia.managepassword.haspassword.view.viewmodel.HasPasswordViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author rival
 * @team: @minion-kevin
 *
 * For navigate :
 * External : [com.tokopedia.applink.ApplinkConst.HAS_PASSWORD]
 * Internal : [com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.HAS_PASSWORD]
 *
 */

class HasPasswordActivity : BaseSimpleActivity(), HasComponent<ManagePasswordComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(HasPasswordViewModel::class.java) }


    override fun getComponent(): ManagePasswordComponent {
        return DaggerManagePasswordComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .managePasswordModule(ManagePasswordModule(this))
            .build()
    }

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_has_password)
        component.inject(this)
        initObserver()

        if (!userSession.isLoggedIn) {
            gotoLogin()
        } else {
            viewModel.checkPassword()
        }
    }

    private fun initObserver() {
        viewModel.profileDataModel.observe(this, {
            when (it) {
                is Success -> {
                    isPasswordAvailable(it.data.profileData.isCreatedPassword)
                }
                is Fail -> {
                    checkFromCache()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOGIN -> {
                if (resultCode == Activity.RESULT_OK && userSession.isLoggedIn) {
                    viewModel.checkPassword()
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        }
    }

    private fun checkFromCache() {
        val hasPassword = userSession.hasPassword()
        isPasswordAvailable(hasPassword)
    }

    private fun isPasswordAvailable(hasPassword: Boolean) {
        userSession.setHasPassword(hasPassword)
        val intent: Intent = if (hasPassword) {
            RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.FORGOT_PASSWORD)
        } else {
            RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.ADD_PASSWORD)
        }
        startActivity(intent)
        finish()
    }

    private fun gotoLogin() {
        val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_LOGIN)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.profileDataModel.removeObservers(this)
    }

    companion object {
        const val REQUEST_LOGIN = 1000
    }
}
