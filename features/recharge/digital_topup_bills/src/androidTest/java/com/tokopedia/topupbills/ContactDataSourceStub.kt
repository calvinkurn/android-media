package com.tokopedia.topupbills

import com.tokopedia.common.topupbills.data.TopupBillsContact
import com.tokopedia.common.topupbills.data.source.ContactDataSource

class ContactDataSourceStub: ContactDataSource {
    override fun getContactList(): MutableList<TopupBillsContact> {
        return mutableListOf(
            TopupBillsContact("Misael", "081234567890"),
            TopupBillsContact("Jessica", "085600001111"),
            TopupBillsContact("Nabilla", "081288383091"),
            TopupBillsContact("Furqan", "088193930918"),
            TopupBillsContact("Firman", "081211111111"),
            TopupBillsContact("Asti", "087839080981"),
            TopupBillsContact("Meyta", "081208120812"),
        )
    }
}
