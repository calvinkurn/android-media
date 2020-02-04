package com.tokopedia.vouchergame.detail.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory

/**
 * Created by resakemal on 26/11/19.
 */
class VoucherGameProduct(var position: Int = 0,
                         var selected: Boolean = false): CatalogProduct(), Visitable<VoucherGameDetailAdapterFactory> {
        override fun type(typeFactory: VoucherGameDetailAdapterFactory) = typeFactory.type(this)
}