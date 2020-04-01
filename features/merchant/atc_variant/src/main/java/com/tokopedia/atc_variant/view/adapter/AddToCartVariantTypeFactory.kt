package com.tokopedia.atc_variant.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.atc_variant.view.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface AddToCartVariantTypeFactory {

    fun type(viewModel: NoteViewModel): Int

    fun type(viewModel: ProductViewModel): Int

    fun type(viewModel: QuantityViewModel): Int

    fun type(viewModel: TypeVariantViewModel): Int

    fun type(viewModel: InsuranceRecommendationViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}