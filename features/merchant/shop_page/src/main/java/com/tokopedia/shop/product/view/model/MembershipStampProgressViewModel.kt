package com.tokopedia.shop.product.view.model

import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipData
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

class MembershipStampProgressViewModel(
        val membershipData: MembershipData = MembershipData())
    : BaseShopProductViewModel {
    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int = typeFactory.type(this)
}