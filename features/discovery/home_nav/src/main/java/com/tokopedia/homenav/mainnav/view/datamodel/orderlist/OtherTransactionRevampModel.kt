package com.tokopedia.homenav.mainnav.view.datamodel.orderlist

import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.OrderListTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

/**
 * Created by dhaba
 */

@MePage(MePage.Widget.TRANSACTION)
class OtherTransactionRevampModel : OrderNavVisitable, ImpressHolder() {
    override fun type(factory: OrderListTypeFactory): Int {
        return factory.type(this)
    }
}
