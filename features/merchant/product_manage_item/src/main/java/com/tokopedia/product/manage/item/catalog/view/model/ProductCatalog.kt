package com.tokopedia.product.manage.item.catalog.view.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.item.catalog.view.adapter.ProductCatalogTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCatalog(var catalogId : Int = 0, var catalogName : String = "", var catalogImage : String = "")
    : Parcelable, Visitable<ProductCatalogTypeFactory>{

    override fun type(typeFactory: ProductCatalogTypeFactory): Int = typeFactory.type(this)

}