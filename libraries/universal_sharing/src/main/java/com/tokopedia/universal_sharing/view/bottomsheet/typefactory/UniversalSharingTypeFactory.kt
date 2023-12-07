package com.tokopedia.universal_sharing.view.bottomsheet.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.universal_sharing.view.model.UniversalSharingGlobalErrorUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel

/**
 * Reuse this interface for other recycler view
 * Add new ui model here
 */
interface UniversalSharingTypeFactory : AdapterTypeFactory {
    fun type(uiModel: UniversalSharingPostPurchaseShopTitleUiModel): Int
    fun type(uiModel: UniversalSharingPostPurchaseProductUiModel): Int
    fun type(uiModel: UniversalSharingGlobalErrorUiModel): Int
}
