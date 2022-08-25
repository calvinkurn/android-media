package com.tokopedia.loginregister.redefine_register_email.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.redefine_register_email.di.DaggerRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.di.RegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.view.fragment.RedefineRegisterEmailFragment

class RedefineRegisterEmailActivity : BaseSimpleActivity(), HasComponent<RegisterEmailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
    }

    override fun getNewFragment(): Fragment = RedefineRegisterEmailFragment.newInstance()

    override fun getComponent(): RegisterEmailComponent {
        return DaggerRegisterEmailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}