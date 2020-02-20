package com.tokopedia.notifcenter.data.viewbean

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.model.NotificationFilterSection
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactory
import java.util.ArrayList

class NotificationFilterSectionViewBean(
        val filters: ArrayList<NotificationFilterSection>
): Visitable<NotificationTransactionFactory> {

    override fun type(typeFactory: NotificationTransactionFactory): Int {
        return typeFactory.type(this)
    }

}