package com.tokopedia.topads.edit.view.adapter.product.viewmodel

import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactory


class ProductItemViewModel(var data: ResponseProductList.Result.TopadsGetListProduct.Data) : ProductViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: ProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}