package com.tokopedia.expresscheckout.view.variant.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface CheckoutVariantTypeFactory {

    fun type(viewModel: NoteViewModel): Int

    fun type(viewModel: ProductViewModel): Int

    fun type(viewModel: ProfileViewModel): Int

    fun type(viewModel: QuantityViewModel): Int

    fun type(viewModel: SummaryViewModel): Int

    fun type(viewModel: TypeVariantViewModel): Int

    fun type(viewModel: InsuranceViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}