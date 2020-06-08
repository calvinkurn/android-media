package com.tokopedia.officialstore.official.presentation.dynamic_channel

import com.tokopedia.officialstore.official.presentation.viewmodel.ProductFlashSaleDataModel

interface OfficialStoreFlashSaleCardViewTypeFactory {

    fun type(productFlashSaleDataModel: ProductFlashSaleDataModel): Int

}
