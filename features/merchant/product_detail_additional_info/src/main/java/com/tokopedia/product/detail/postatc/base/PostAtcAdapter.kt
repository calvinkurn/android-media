package com.tokopedia.product.detail.postatc.base

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.product.detail.postatc.component.productinfo.ProductInfoDelegate

class PostAtcAdapter : BaseAdapter<Any>() {
    init {
        delegatesManager.addDelegate(ProductInfoDelegate())
    }
}
