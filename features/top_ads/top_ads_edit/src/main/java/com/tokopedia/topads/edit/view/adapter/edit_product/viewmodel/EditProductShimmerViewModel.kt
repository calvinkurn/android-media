package com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel

import com.tokopedia.topads.edit.view.adapter.edit_product.EditProductListAdapterTypeFactory


/**
 * Created by Pika on 8/4/20.
 */
class EditProductShimmerViewModel() : EditProductViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: EditProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}