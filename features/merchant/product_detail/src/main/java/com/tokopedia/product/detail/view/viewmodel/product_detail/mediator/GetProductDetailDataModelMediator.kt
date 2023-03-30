package com.tokopedia.product.detail.view.viewmodel.product_detail.mediator

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.view.viewmodel.product_detail.base.SubViewModelMediator

/**
 * Created by yovi.putra on 30/03/23"
 * Project name: android-tokopedia-core
 **/

interface GetProductDetailDataModelMediator : SubViewModelMediator {

    fun getPdpLayout(): DynamicProductInfoP1?

    fun getPdpData(): ProductInfoP2UiData?

    fun getVariant(): ProductVariant?
}
