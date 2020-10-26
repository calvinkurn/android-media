package com.tokopedia.phoneverification.view.activity

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.phoneverification.R
import com.tokopedia.phoneverification.di.revamp.DaggerPhoneVerificationComponent
import com.tokopedia.phoneverification.di.revamp.PhoneVerificationComponent
import com.tokopedia.phoneverification.di.revamp.PhoneVerificationModule
import com.tokopedia.phoneverification.di.revamp.PhoneVerificationQueryModule
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment.Companion.createInstance
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment.PhoneVerificationFragmentListener
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationProfileFragment.Companion.createInstance

/**
 * Created by nisie on 2/23/17.
 * * For navigate: use [ApplinkConstInternalGlobal.SETTING_PROFILE_PHONE_VERIFICATION]
 */
class PhoneVerificationProfileActivity : BaseSimpleActivity(), HasComponent<PhoneVerificationComponent> {

    private lateinit var phoneVerificationComponent: PhoneVerificationComponent

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initView() {
        val fragmentHeader = createInstance()
        val fragment = createInstance(phoneVerificationListener, false)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentById(R.id.container_header) == null) {
            fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader.javaClass.simpleName)
        }
        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.javaClass.simpleName)
        }
        //        } else if (((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id.container)).listener == null) {
//            ((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id.container))
//                    .setPhoneVerificationListener(getPhoneVerificationListener());
//        }
        fragmentTransaction.commit()
    }

    private val phoneVerificationListener: PhoneVerificationFragmentListener
        private get() = object : PhoneVerificationFragmentListener {
            override fun onSkipVerification() {
                setIntentTarget(Activity.RESULT_CANCELED)
            }

            override fun onSuccessVerification() {
                setIntentTarget(Activity.RESULT_OK)
            }
        }

    private fun setIntentTarget(result: Int) {
        if (isTaskRoot) {
            goToManageProfile()
        } else {
            setResult(result)
            finish()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_phone_verification_activation
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun inflateFragment() {
        initView()
    }

    private fun goToManageProfile() {
        val intent = RouteManager.getIntent(applicationContext, ApplinkConstInternalGlobal.SETTING_PROFILE)
        startActivityForResult(intent, RESULT_CODE_MANAGE_PROFILE)
    }

    companion object {
        private const val RESULT_CODE_MANAGE_PROFILE = 123
    }

    override fun getComponent(): PhoneVerificationComponent {
        return DaggerPhoneVerificationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .phoneVerificationQueryModule(PhoneVerificationQueryModule(this))
                .build()
    }
}