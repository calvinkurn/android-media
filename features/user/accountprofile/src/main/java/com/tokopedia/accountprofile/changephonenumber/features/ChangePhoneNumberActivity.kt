package com.tokopedia.accountprofile.changephonenumber.features

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.changephonenumber.data.GetWarningDataModel
import com.tokopedia.accountprofile.changephonenumber.di.ChangePhoneNumberComponent
import com.tokopedia.accountprofile.changephonenumber.di.DaggerChangePhoneNumberComponent
import com.tokopedia.accountprofile.changephonenumber.di.module.ChangePhoneNumberModule
import com.tokopedia.accountprofile.changephonenumber.features.webview.ChangePhoneNumberWebViewActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChangePhoneNumberActivity : BaseSimpleActivity(), HasComponent<ChangePhoneNumberComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ChangePhoneNumberViewModel::class.java) }

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): ChangePhoneNumberComponent {
        return DaggerChangePhoneNumberComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .changePhoneNumberModule(ChangePhoneNumberModule(this))
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_phone_number)
        component.inject(this)

        initObserver()
        if (userSession.isLoggedIn) {
            viewModel.getWarning()
        } else {
            gotoLogin()
        }
    }

    private fun initObserver() {
        viewModel.getWarning.observe(this, {
            when(it) {
                is Success -> {
                    onSuccessGetWarning(it.data)
                }

                is Fail -> {
                    openPage(DEFAULT_URL)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode) {
            REQUEST_LOGIN -> {
                if (resultCode == Activity.RESULT_OK && userSession.isLoggedIn) {
                    viewModel.getWarning()
                } else {
                    onFailedOrCanceled()
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun onSuccessGetWarning(getWarningDataModel: GetWarningDataModel) {
        if (getWarningDataModel.redirectUrl.isNotEmpty()) {
            openPage(getWarningDataModel.redirectUrl)
        } else {
            openPage(DEFAULT_URL)
        }
    }

    private fun openPage(url: String) {
        val intent = ChangePhoneNumberWebViewActivity.createIntent(this, url, true)
        startActivity(intent)
        finish()
    }

    private fun onFailedOrCanceled() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun gotoLogin() {
        val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_LOGIN)
    }

    companion object {
        private const val REQUEST_LOGIN = 1000
        private val DEFAULT_URL =  "${TokopediaUrl.getInstance().MOBILEWEB}user/profile/edit/phone"
    }
}
