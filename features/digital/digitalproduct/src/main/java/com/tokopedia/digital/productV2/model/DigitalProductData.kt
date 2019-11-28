package com.tokopedia.digital.productV2.model

import com.tokopedia.common.topupbills.data.product.CatalogData

/**
 * Created by resakemal on 28/11/19.
 */
class DigitalProductData: CatalogData() {
        override var product: DigitalProductItemData = DigitalProductItemData()

        override val enquiryFields: List<DigitalProductInput> = listOf()
}