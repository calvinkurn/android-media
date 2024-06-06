package com.tokopedia.universal_sharing.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactory

data class UniversalSharingPostPurchaseShopTitleUiModel(
    val name: String
) : Visitable<UniversalSharingTypeFactory> {
    override fun type(typeFactory: UniversalSharingTypeFactory): Int {
        return typeFactory.type(this)
    }
}
