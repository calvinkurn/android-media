package com.tokopedia.purchase_platform.features.express_checkout.view.variant.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface CheckoutVariantTypeFactory {

    fun type(uiModel: NoteUiModel): Int

    fun type(uiModel: ProductUiModel): Int

    fun type(uiModel: ProfileUiModel): Int

    fun type(uiModel: QuantityUiModel): Int

    fun type(uiModel: SummaryUiModel): Int

    fun type(uiModel: TypeVariantUiModel): Int

    fun type(uiModel: InsuranceUiModel): Int

    fun type(uiModel: InsuranceRecommendationUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}