package com.tokopedia.smartbills.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.smartbills.di.DaggerSmartBillsComponent
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment

class SmartBillsActivity : BaseSimpleActivity(), HasComponent<SmartBillsComponent> {

    override fun getNewFragment(): Fragment? {
        val bundle = intent.extras
        val sourceType = bundle?.getString(PARAM_SOURCE_TYPE) ?: ""
        return SmartBillsFragment.newInstance(sourceType)
    }

    override fun getComponent(): SmartBillsComponent {
        return DaggerSmartBillsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        const val PARAM_SOURCE_TYPE = "source"
    }
}