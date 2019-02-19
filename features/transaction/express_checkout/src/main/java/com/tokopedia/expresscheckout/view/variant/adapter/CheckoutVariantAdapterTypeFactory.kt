package com.tokopedia.expresscheckout.view.variant.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewholder.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantAdapterTypeFactory(val listener: CheckoutVariantActionListener) : BaseAdapterTypeFactory(), CheckoutVariantTypeFactory {

    override fun type(viewModel: NoteViewModel): Int {
        return NoteViewHolder.LAYOUT
    }

    override fun type(viewModel: ProductViewModel): Int {
        return ProductViewHolder.LAYOUT
    }

    override fun type(viewModel: ProfileViewModel): Int {
        return ProfileViewHolder.LAYOUT
    }

    override fun type(viewModel: QuantityViewModel): Int {
        return QuantityViewHolder.LAYOUT
    }

    override fun type(viewModel: SummaryViewModel): Int {
        return SummaryViewHolder.LAYOUT
    }

    override fun type(viewModel: TypeVariantViewModel): Int {
        return TypeVariantViewHolder.LAYOUT
    }

    override fun type(viewModel: InsuranceViewModel): Int {
        return InsuranceViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return LoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            NoteViewHolder.LAYOUT -> NoteViewHolder(view, listener)
            ProductViewHolder.LAYOUT -> ProductViewHolder(view, listener)
            ProfileViewHolder.LAYOUT -> ProfileViewHolder(view, listener)
            QuantityViewHolder.LAYOUT -> QuantityViewHolder(view, listener)
            SummaryViewHolder.LAYOUT -> SummaryViewHolder(view, listener)
            TypeVariantViewHolder.LAYOUT -> TypeVariantViewHolder(view, listener)
            InsuranceViewHolder.LAYOUT -> InsuranceViewHolder(view, listener)
            LoadingViewHolder.LAYOUT -> LoadingViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }

    }

}