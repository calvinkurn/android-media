package com.tokopedia.product.estimasiongkir.view.adapter

import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingErrorDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingShimmerDataModel

/**
 * Created by Yehezkiel on 25/01/21
 */
interface ProductShippingFactory {
    fun type(dataProduct: ProductShippingHeaderDataModel): Int
    fun type(dataProduct: ProductShippingServiceDataModel): Int
    fun type(dataProduct: ProductShippingShimmerDataModel): Int
    fun type(dataProduct: ProductShippingErrorDataModel): Int
}