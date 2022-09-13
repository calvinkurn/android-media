package com.tokopedia.loginregister.redefineregisteremail.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.redefineregisteremail.di.DaggerRedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefineregisteremail.di.RedefineRegisterEmailComponent
import javax.inject.Inject

class RedefineRegisterEmailActivity : BaseSimpleActivity(), HasComponent<RedefineRegisterEmailComponent> {

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
        supportActionBar?.elevation = 0f
    }

    override fun getLayoutRes(): Int = R.layout.activity_redefine_register_email

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): RedefineRegisterEmailComponent {
        return DaggerRedefineRegisterEmailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onBackPressed() {
        if (viewModel.isAllowBackPressed) super.onBackPressed()
    }
}