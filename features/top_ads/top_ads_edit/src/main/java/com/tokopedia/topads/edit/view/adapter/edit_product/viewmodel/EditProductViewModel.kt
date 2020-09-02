package com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel

import com.tokopedia.topads.edit.view.adapter.edit_product.EditProductListAdapterTypeFactory

/**
 * Created by Pika on 8/4/20.
 */
abstract class EditProductViewModel {
    abstract fun type(typesFactory: EditProductListAdapterTypeFactory): Int
}
