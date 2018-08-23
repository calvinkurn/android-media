package com.tokopedia.product.manage.item.video.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.item.video.view.adapter.ProductAddVideoAdapterTypeFactory

class TitleVideoChosenViewModel : Visitable<ProductAddVideoAdapterTypeFactory>, ProductAddVideoBaseViewModel {

    override fun type(typeFactory: ProductAddVideoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}