package com.tokopedia.shop.newproduct.view.datamodel

import com.tokopedia.shop.common.data.viewmodel.BaseMembershipViewModel
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory

class MembershipStampProgressViewModel(
        var listOfData: List<BaseMembershipViewModel> = listOf())
    : BaseShopProductViewModel {
    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int = typeFactory.type(this)
}