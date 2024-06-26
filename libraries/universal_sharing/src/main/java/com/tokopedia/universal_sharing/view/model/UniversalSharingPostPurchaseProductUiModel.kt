package com.tokopedia.universal_sharing.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactory

data class UniversalSharingPostPurchaseProductUiModel(
    val orderId: String = "",
    val productId: String = "",
    val name: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val shopName: String = ""
) : Visitable<UniversalSharingTypeFactory> {
    override fun type(typeFactory: UniversalSharingTypeFactory): Int {
        return typeFactory.type(this)
    }
}
