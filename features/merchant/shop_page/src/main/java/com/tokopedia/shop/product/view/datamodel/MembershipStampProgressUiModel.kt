package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.common.data.viewmodel.BaseMembershipViewModel
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

class MembershipStampProgressUiModel(
        var listOfData: List<BaseMembershipViewModel> = listOf())
    : BaseShopProductViewModel {
    override fun type(typeFactory: ShopProductAdapterTypeFactory?): Int = typeFactory?.type(this).orZero()
}