package com.tokopedia.product.edit.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.edit.view.adapter.ProductAddVideoAdapterTypeFactory

class TitleVideoChosenViewModel : Visitable<ProductAddVideoAdapterTypeFactory>, ProductAddVideoBaseViewModel{

    override fun type(typeFactory: ProductAddVideoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}