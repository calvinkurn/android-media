package com.tokopedia.common.topupbills.favoritecommon.view.model.contact

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.favoritecommon.view.typefactory.ContactListTypeFactory

class TopupBillsContactDataView(
    var name: String,
    var number: String
): Visitable<ContactListTypeFactory> {

    override fun type(typeFactory: ContactListTypeFactory): Int {
        return typeFactory.type(this)
    }
}