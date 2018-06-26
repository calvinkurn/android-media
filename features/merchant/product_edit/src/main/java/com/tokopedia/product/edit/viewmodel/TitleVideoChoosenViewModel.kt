package com.tokopedia.product.edit.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.edit.adapter.ProductAddVideoAdapterTypeFactory

class TitleVideoChoosenViewModel : Visitable<ProductAddVideoAdapterTypeFactory>, ProductAddVideoBaseViewModel{

    override fun type(typeFactory: ProductAddVideoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}