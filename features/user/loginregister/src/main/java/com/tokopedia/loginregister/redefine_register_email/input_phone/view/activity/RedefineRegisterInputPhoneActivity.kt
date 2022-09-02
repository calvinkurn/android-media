package com.tokopedia.loginregister.redefine_register_email.input_phone.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.ActivityRedefineRegisterInputPhoneBinding
import com.tokopedia.loginregister.redefine_register_email.common.intentGoToHome
import com.tokopedia.loginregister.redefine_register_email.di.DaggerRedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.fragment.RedefineRegisterInputPhoneFragment
import com.tokopedia.utils.view.binding.viewBinding

class RedefineRegisterInputPhoneActivity : BaseSimpleActivity(),
    HasComponent<RedefineRegisterEmailComponent> {

    private val binding : ActivityRedefineRegisterInputPhoneBinding? by viewBinding()
    private var paramIsRequiresInputPhone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpToolBar()
    }

    private fun setUpToolBar() {
        binding?.unifyToolbar?.apply {
            isShowShadow = false
            if (paramIsRequiresInputPhone) {
                setNavigationOnClickListener {
                    onBackPressed()
                }
                isShowBackButton = true
            } else {
                isShowBackButton = false
                actionText = getString(R.string.register_email_input_phone_skip)

                actionTextView?.setOnClickListener {
                    goToHome()
                }
            }
        }
    }

    private fun goToHome() {
        val intent = intentGoToHome(this)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (paramIsRequiresInputPhone) {
            super.onBackPressed()
        }
    }

    override fun getNewFragment(): Fragment{
        paramIsRequiresInputPhone = intent.getBooleanExtra(
            ApplinkConstInternalUserPlatform.PARAM_IS_REGISTER_REQUIRED_INPUT_PHONE,
            false
        )

        val bundle = Bundle()
        bundle.putAll(intent.extras)
        return RedefineRegisterInputPhoneFragment.newInstance(bundle)
    }

    override fun getComponent(): RedefineRegisterEmailComponent {
        return DaggerRedefineRegisterEmailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getToolbarResourceID(): Int {
        return R.id.unify_toolbar
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_redefine_register_input_phone
    }
}