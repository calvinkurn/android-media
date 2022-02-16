package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.digital_product_detail.data.model.data.DigitalDynamicInput
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import javax.inject.Inject

@DigitalPDPScope
class DigitalDynamicInputMapper  @Inject constructor() {
    val paramProduct = "product_id"

    fun mapDynamicInputProduct(digitalDynamicInput: DigitalDynamicInput): CatalogProduct? {
        return digitalDynamicInput.enquiryFields.find {it.name == paramProduct}?.dataCollections?.firstOrNull()?.products?.firstOrNull()
    }
}