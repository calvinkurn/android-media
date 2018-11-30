package com.tokopedia.expresscheckout.view.variant.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface CheckoutVariantTypeFactory {

    fun type(viewModel: CheckoutVariantNoteViewModel): Int

    fun type(viewModel: CheckoutVariantProductViewModel): Int

    fun type(viewModel: CheckoutVariantProfileViewModel): Int

    fun type(viewModel: CheckoutVariantQuantityViewModel): Int

    fun type(viewModel: CheckoutVariantSummaryViewModel): Int

    fun type(viewModel: CheckoutVariantTypeVariantViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}