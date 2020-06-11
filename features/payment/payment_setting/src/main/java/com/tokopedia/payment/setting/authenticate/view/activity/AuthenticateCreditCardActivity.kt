package com.tokopedia.payment.setting.authenticate.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCreditCardComponent
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCreditCardModule
import com.tokopedia.payment.setting.authenticate.di.DaggerAuthenticateCreditCardComponent
import com.tokopedia.payment.setting.authenticate.view.fragment.AuthenticateCreditCardFragment

class AuthenticateCreditCardActivity : BaseSimpleActivity(), HasComponent<AuthenticateCreditCardComponent> {

    override fun getNewFragment(): Fragment {
        return AuthenticateCreditCardFragment.createInstance()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AuthenticateCreditCardActivity::class.java)
        }
    }

    override fun getComponent(): AuthenticateCreditCardComponent {
        return DaggerAuthenticateCreditCardComponent.builder()
                .baseAppComponent((this.application as BaseMainApplication).baseAppComponent)
                .authenticateCreditCardModule(AuthenticateCreditCardModule(this))
                .build()
    }
}