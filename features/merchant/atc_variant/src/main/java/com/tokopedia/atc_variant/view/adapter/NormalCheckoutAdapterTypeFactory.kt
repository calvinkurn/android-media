package com.tokopedia.atc_variant.view.adapter

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.atc_variant.view.CheckoutVariantActionListener

class NormalCheckoutAdapterTypeFactory(listener: CheckoutVariantActionListener) : CheckoutVariantAdapterTypeFactory(listener) {
    //we do not want the shimmering layout
    override fun type(viewModel: LoadingModel): Int {
        return HideViewHolder.LAYOUT
    }

}