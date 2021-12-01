package com.tokopedia.common.topupbills.view.activity

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.di.StubCommonTopupBillsComponentInstance

class TopupBillsSavedNumberActivityStub: TopupBillsSavedNumberActivity() {

    override fun getComponent(): CommonTopupBillsComponent {
        return StubCommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application)
    }
}