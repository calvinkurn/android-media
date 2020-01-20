package com.tokopedia.purchase_platform.features.atc_variant.view.adapter

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.CheckoutVariantActionListener
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.adapter.CheckoutVariantAdapterTypeFactory

class NormalCheckoutAdapterTypeFactory(listener: CheckoutVariantActionListener) : CheckoutVariantAdapterTypeFactory(listener) {
    //we do not want the shimmering layout
    override fun type(viewModel: LoadingModel): Int {
        return HideViewHolder.LAYOUT
    }

}