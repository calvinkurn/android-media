package com.tokopedia.common.topupbills.view.activity

import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.di.StubCommonTopupBillsComponentInstance
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType

class TopupBillsSavedNumberActivityStub: TopupBillsSavedNumberActivity() {

    override fun getComponent(): CommonTopupBillsComponent {
        return StubCommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application)
    }
}