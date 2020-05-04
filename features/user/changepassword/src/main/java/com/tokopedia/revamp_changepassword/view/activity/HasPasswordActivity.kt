package com.tokopedia.revamp_changepassword.view.activity

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.changepassword.R
import com.tokopedia.revamp_changepassword.di.ChangePasswordComponent
import com.tokopedia.revamp_changepassword.di.DaggerChangePasswordComponent
import com.tokopedia.revamp_changepassword.di.module.ChangePasswordModule
import com.tokopedia.revamp_changepassword.view.viewmode.HasPasswordViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @team: @minion-kevin
 *
 * For navigate :
 * External : [com.tokopedia.applink.ApplinkConst.HAS_PASSWORD]
 * Internal : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.HAS_PASSWORD]
 *
 * */

class HasPasswordActivity : BaseSimpleActivity(), HasComponent<ChangePasswordComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(HasPasswordViewModel::class.java) }

    override fun getComponent(): ChangePasswordComponent {
        return DaggerChangePasswordComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .changePasswordModule(ChangePasswordModule(this))
                .build()
    }

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_has_password)
        component.inject(this)
        initObserver()

        if (!userSession.isLoggedIn) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            viewModel.checkPassword()
        }
    }

    private fun initObserver() {
        viewModel.profileData.observe(this, Observer {
            when(it) {
                is Success -> { isPasswordAvailable(it.data.userProfileData.isCreatedPassword) }
                is Fail -> { onFailedCheck() }
            }
        })
    }

    private fun onFailedCheck() {
        /* check from cache */
        val hasPassword = userSession.hasPassword()

        /* check is have or not */
        isPasswordAvailable(hasPassword)
    }

    private fun isPasswordAvailable(hasPassword: Boolean) {
        if (hasPassword) {
            /* Goto change password*/
            RouteManager.route(this, ApplinkConstInternalGlobal.CHANGE_PASSWORD)
        } else {
            /* Goto add password */
            RouteManager.route(this, ApplinkConstInternalGlobal.ADD_PASSWORD)
        }
        finish()
    }
}
