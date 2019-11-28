package com.tokopedia.atc_variant.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.atc_variant.view.CheckoutVariantActionListener
import com.tokopedia.atc_variant.view.viewholder.*
import com.tokopedia.atc_variant.view.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

open class CheckoutVariantAdapterTypeFactory(val listener: CheckoutVariantActionListener) : BaseAdapterTypeFactory(), CheckoutVariantTypeFactory {

    override fun type(viewModel: NoteViewModel): Int {
        return NoteViewHolder.LAYOUT
    }

    override fun type(viewModel: ProductViewModel): Int {
        return ProductViewHolder.LAYOUT
    }

    override fun type(viewModel: QuantityViewModel): Int {
        return QuantityViewHolder.LAYOUT
    }

    override fun type(viewModel: TypeVariantViewModel): Int {
        return TypeVariantViewHolder.LAYOUT
    }

    override fun type(viewModel: InsuranceRecommendationViewModel): Int {
        return InsuranceRecommendationViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return HideViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            NoteViewHolder.LAYOUT -> NoteViewHolder(view, listener)
            ProductViewHolder.LAYOUT -> ProductViewHolder(view, listener)
            QuantityViewHolder.LAYOUT -> QuantityViewHolder(view, listener)
            TypeVariantViewHolder.LAYOUT -> TypeVariantViewHolder(view, listener)
            InsuranceRecommendationViewHolder.LAYOUT -> InsuranceRecommendationViewHolder(view, listener)
            HideViewHolder.LAYOUT -> HideViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }

    }

}