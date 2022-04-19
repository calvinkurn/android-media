package com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel

import com.tokopedia.topads.common.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.view.adapter.edit_product.EditProductListAdapterTypeFactory

/**
 * Created by Pika on 8/4/20.
 */
class EditProductItemViewModel(var data: GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem) : EditProductViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: EditProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}