package com.tokopedia.product.detail.view.viewmodel.product_detail.mediator

import com.tokopedia.library.subviewmodel.SubViewModelMediator
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData

/**
 * Created by yovi.putra on 30/03/23"
 * Project name: android-tokopedia-core
 **/

interface GetProductDetailDataMediator : SubViewModelMediator {

    fun getP1(): ProductInfoP1?

    fun getP2(): ProductInfoP2UiData?

    fun getP2Login(): ProductInfoP2Login?

    fun getVariant(): ProductVariant?
}
