package com.tokopedia.notifcenter.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactory
import java.util.ArrayList

class NotificationFilterSectionWrapper(
        val filters: ArrayList<NotificationFilterSection>
): Visitable<NotificationTransactionFactory> {

    override fun type(typeFactory: NotificationTransactionFactory): Int {
        return typeFactory.type(this)
    }

}