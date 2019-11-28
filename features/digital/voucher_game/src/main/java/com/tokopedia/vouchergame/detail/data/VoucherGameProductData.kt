package com.tokopedia.vouchergame.detail.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory

/**
 * Created by resakemal on 13/08/19.
 */
open class VoucherGameProductData: CatalogProduct(){
        override var dataCollections: List<DataCollection> = listOf()

        class DataCollection(name: String, products: List<CatalogProductData>): CatalogProduct.DataCollection(name, products), Visitable<VoucherGameDetailAdapterFactory> {

                override fun type(typeFactory: VoucherGameDetailAdapterFactory) = typeFactory.type(this)

                override var products: List<VoucherGameProduct> = listOf()
        }
}