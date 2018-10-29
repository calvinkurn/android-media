
package com.tokopedia.flashsale.management.product.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.product.adapter.FlashSaleProductAdapterTypeFactory

class FlashSaleProductViewModel: Visitable<FlashSaleProductAdapterTypeFactory>{
    var id: String? = ""
    var shopId:String? = ""

    override fun type(typeFactory: FlashSaleProductAdapterTypeFactory) = typeFactory.type(this)

}