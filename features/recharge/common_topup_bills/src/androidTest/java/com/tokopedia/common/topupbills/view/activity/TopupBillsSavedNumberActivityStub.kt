package com.tokopedia.common.topupbills.view.activity

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.di.StubCommonTopupBillsComponentInstance
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity

class TopupBillsSavedNumberActivityStub: TopupBillsPersoSavedNumberActivity() {

    override fun getComponent(): CommonTopupBillsComponent {
        return StubCommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application)
    }
}