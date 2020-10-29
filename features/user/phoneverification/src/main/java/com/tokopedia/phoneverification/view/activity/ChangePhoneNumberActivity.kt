package com.tokopedia.phoneverification.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.phoneverification.di.revamp.DaggerPhoneVerificationComponent
import com.tokopedia.phoneverification.di.revamp.PhoneVerificationComponent
import com.tokopedia.phoneverification.di.revamp.PhoneVerificationQueryModule
import com.tokopedia.phoneverification.view.fragment.ChangePhoneNumberFragment

/**
 * Created by nisie on 2/23/17.
 */
class ChangePhoneNumberActivity : BaseSimpleActivity(), HasComponent<PhoneVerificationComponent> {


    override fun getNewFragment(): Fragment? {
        return ChangePhoneNumberFragment.createInstance()
    }

    companion object {
        @JvmStatic
        fun getChangePhoneNumberIntent(context: Context?, phoneNumber: String?): Intent {
            val intent = Intent(context, ChangePhoneNumberActivity::class.java)
            intent.putExtra(ChangePhoneNumberFragment.EXTRA_PHONE_NUMBER, phoneNumber)
            return intent
        }
    }

    override fun getComponent(): PhoneVerificationComponent {
        return DaggerPhoneVerificationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .phoneVerificationQueryModule(PhoneVerificationQueryModule(this))
                .build()
    }
}