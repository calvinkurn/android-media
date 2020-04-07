package com.tokopedia.notifcenter.data.viewbean

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.UserInfo
import com.tokopedia.notifcenter.presentation.adapter.typefactory.product.MultipleProductCardFactory

class MultipleProductCardViewBean(
        var templateKey: String = "",
        var notificationId: String = "",
        var userInfo: UserInfo = UserInfo(),
        var product: ProductData = ProductData()
): Visitable<MultipleProductCardFactory> {

    override fun type(typeFactory: MultipleProductCardFactory): Int {
        return typeFactory.type(this)
    }

}