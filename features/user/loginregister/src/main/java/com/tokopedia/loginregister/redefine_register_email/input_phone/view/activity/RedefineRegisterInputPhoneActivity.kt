package com.tokopedia.loginregister.redefine_register_email.input_phone.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.ActivityRedefineRegisterInputPhoneBinding
import com.tokopedia.loginregister.redefine_register_email.di.DaggerRedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.fragment.RedefineRegisterInputPhoneFragment
import com.tokopedia.utils.view.binding.viewBinding

class RedefineRegisterInputPhoneActivity : BaseSimpleActivity(),
    HasComponent<RedefineRegisterEmailComponent> {

    private val binding : ActivityRedefineRegisterInputPhoneBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpToolBar()
    }

    private fun setUpToolBar() {
        binding?.unifyToolbar?.apply {
            isShowShadow = false
            if (isUsingMandatoryRollence()) {
                setNavigationOnClickListener {
                    onBackPressed()
                }
                isShowBackButton = true
            } else {
                isShowBackButton = false
                actionText = getString(R.string.register_email_input_phone_skip)

                actionTextView?.setOnClickListener {
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isUsingMandatoryRollence()) {
            super.onBackPressed()
        } else {
            //show toaster
        }
    }

    private fun isUsingMandatoryRollence(): Boolean = false

    override fun getNewFragment(): Fragment = RedefineRegisterInputPhoneFragment.newInstance()

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