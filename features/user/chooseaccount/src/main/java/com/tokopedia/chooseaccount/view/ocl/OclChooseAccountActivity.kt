package com.tokopedia.chooseaccount.view.ocl

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.chooseaccount.di.ChooseAccountComponent
import com.tokopedia.chooseaccount.di.DaggerChooseAccountComponent

class OclChooseAccountActivity : BaseSimpleActivity(), HasComponent<ChooseAccountComponent> {

    override fun getNewFragment(): Fragment {
        return OclChooseAccountFragment.createInstance()
    }

    override fun getComponent(): ChooseAccountComponent {
        val appComponent = (application as BaseMainApplication)
            .baseAppComponent
        return DaggerChooseAccountComponent.builder()
            .baseAppComponent(appComponent)
            .build()
    }
}
