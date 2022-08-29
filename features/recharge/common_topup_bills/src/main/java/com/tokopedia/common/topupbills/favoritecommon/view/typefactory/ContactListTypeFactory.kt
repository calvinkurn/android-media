package com.tokopedia.common.topupbills.favoritecommon.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactDataView
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactEmptyDataView
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactNotFoundDataView
import com.tokopedia.common.topupbills.favoritecommon.view.model.contact.TopupBillsContactPermissionDataView

interface ContactListTypeFactory {

    fun type(contactDataView: TopupBillsContactDataView): Int
    fun type(permissionDataView: TopupBillsContactPermissionDataView): Int
    fun type(emptyStateDataView: TopupBillsContactEmptyDataView): Int
    fun type(notFoundStateDataView: TopupBillsContactNotFoundDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>>
}