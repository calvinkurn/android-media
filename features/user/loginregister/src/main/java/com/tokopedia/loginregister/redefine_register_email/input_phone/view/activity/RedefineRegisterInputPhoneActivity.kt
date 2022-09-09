package com.tokopedia.loginregister.redefine_register_email.input_phone.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.loginregister.redefine_register_email.di.DaggerRedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.fragment.RedefineRegisterInputPhoneFragment
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel.RedefineRegisterViewModel
import javax.inject.Inject

class RedefineRegisterInputPhoneActivity : BaseSimpleActivity(),
    HasComponent<RedefineRegisterEmailComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            RedefineRegisterViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment{
        val bundle = Bundle()
        bundle.putAll(intent.extras)
        return RedefineRegisterInputPhoneFragment.newInstance(bundle)
    }

    override fun getComponent(): RedefineRegisterEmailComponent {
        return DaggerRedefineRegisterEmailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onBackPressed() {
        if (viewModel.isAllowBackPressed) super.onBackPressed()
    }
}