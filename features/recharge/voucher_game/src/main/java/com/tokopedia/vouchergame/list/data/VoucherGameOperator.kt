package com.tokopedia.vouchergame.list.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.data.product.CatalogOperatorAttributes
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListAdapterFactory

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameOperator(var position: Int = 0,
                          id: String = "",
                          attributes: CatalogOperatorAttributes = CatalogOperatorAttributes())
        : CatalogOperator(id, attributes), Visitable<VoucherGameListAdapterFactory> {
        override fun type(typeFactory: VoucherGameListAdapterFactory) = typeFactory.type(this)
}