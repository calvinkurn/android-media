package com.tokopedia.vouchergame.detail.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory

/**
 * Created by resakemal on 13/08/19.
 */
class VoucherGameProduct(var position: Int = 0,
                         var selected: Boolean = false): CatalogProductData(), Visitable<VoucherGameDetailAdapterFactory> {
        override fun type(typeFactory: VoucherGameDetailAdapterFactory) = typeFactory.type(this)
}