package com.tokopedia.topads.dashboard.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.topads.dashboard.data.model.credit_history.CreditHistory

class TopAdsCreditHistoryTypeFactory: BaseAdapterTypeFactory() {
    fun type(creditHistory: CreditHistory): Int  = 1
}