package com.tokopedia.smartbills.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment

class SmartBillsActivity : BaseSimpleActivity(), HasComponent<SmartBillsComponent> {

    override fun getNewFragment(): Fragment? {
        return SmartBillsFragment()
    }

    override fun getComponent(): SmartBillsComponent {
        return DaggerSmartBillsComponent.builder()
                .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                .build()
    }
}