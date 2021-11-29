package com.tokopedia.common.topupbills.view.model.contact

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.view.typefactory.ContactListTypeFactory

class TopupBillsContactPermissionDataView: Visitable<ContactListTypeFactory> {

    override fun type(typeFactory: ContactListTypeFactory): Int {
        return typeFactory.type(this)
    }
}