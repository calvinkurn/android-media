package com.tokopedia.atc_variant.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.atc_variant.view.AddToCartVariantActionListener
import com.tokopedia.atc_variant.view.viewholder.*
import com.tokopedia.atc_variant.view.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

open class AddToCartVariantAdapterTypeFactory(val listenerNormal: AddToCartVariantActionListener) : BaseAdapterTypeFactory(), AddToCartVariantTypeFactory {

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
            NoteViewHolder.LAYOUT -> NoteViewHolder(view, listenerNormal)
            ProductViewHolder.LAYOUT -> ProductViewHolder(view, listenerNormal)
            QuantityViewHolder.LAYOUT -> QuantityViewHolder(view, listenerNormal)
            TypeVariantViewHolder.LAYOUT -> TypeVariantViewHolder(view, listenerNormal)
            InsuranceRecommendationViewHolder.LAYOUT -> InsuranceRecommendationViewHolder(view, listenerNormal)
            HideViewHolder.LAYOUT -> HideViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }

    }

}