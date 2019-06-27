package com.tokopedia.normalcheckout.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory
import com.tokopedia.expresscheckout.view.variant.viewholder.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

class NormalCheckoutAdapterTypeFactory(listener: CheckoutVariantActionListener) : CheckoutVariantAdapterTypeFactory(listener) {
    //we do not want the shimmering layout
    override fun type(viewModel: LoadingModel): Int {
        return HideViewHolder.LAYOUT
    }

}