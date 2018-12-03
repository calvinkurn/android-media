package com.tokopedia.expresscheckout.view.variant.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewholder.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantAdapterTypefactory(val listener: CheckoutVariantActionListener) : BaseAdapterTypeFactory(), CheckoutVariantTypeFactory {

    override fun type(viewModel: CheckoutVariantNoteViewModel): Int {
        return CheckoutVariantNoteViewHolder.LAYOUT
    }

    override fun type(viewModel: CheckoutVariantProductViewModel): Int {
        return CheckoutVariantProductViewHolder.LAYOUT
    }

    override fun type(viewModel: CheckoutVariantProfileViewModel): Int {
        return CheckoutVariantProfileViewHolder.LAYOUT
    }

    override fun type(viewModel: CheckoutVariantQuantityViewModel): Int {
        return CheckoutVariantQuantityViewHolder.LAYOUT
    }

    override fun type(viewModel: CheckoutVariantSummaryViewModel): Int {
        return CheckoutVariantSummaryViewHolder.LAYOUT
    }

    override fun type(viewModel: CheckoutVariantTypeVariantViewModel): Int {
        return CheckoutVariantTypeVariantViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            CheckoutVariantNoteViewHolder.LAYOUT -> CheckoutVariantNoteViewHolder(view)
            CheckoutVariantProductViewHolder.LAYOUT -> CheckoutVariantProductViewHolder(view)
            CheckoutVariantProfileViewHolder.LAYOUT -> CheckoutVariantProfileViewHolder(view, listener)
            CheckoutVariantQuantityViewHolder.LAYOUT -> CheckoutVariantQuantityViewHolder(view, listener)
            CheckoutVariantSummaryViewHolder.LAYOUT -> CheckoutVariantSummaryViewHolder(view, listener)
            CheckoutVariantTypeVariantViewHolder.LAYOUT -> CheckoutVariantTypeVariantViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }

    }

}