package com.tkpd.atcvariant.view.adapter

import android.view.View
import com.tkpd.atcvariant.data.uidata.*
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheetListener
import com.tkpd.atcvariant.view.viewholder.*
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.common.view.AtcVariantListener
import rx.subscriptions.CompositeSubscription

/**
 * Created by Yehezkiel on 06/05/21
 */

class AtcVariantAdapterTypeFactoryImpl(
        private val variantListener: AtcVariantListener,
        private val compositeSubscription: CompositeSubscription,
        private val atcVariantBottomSheetListener: AtcVariantBottomSheetListener
) : BaseAdapterTypeFactory(), AtcVariantTypeFactory {

    override fun type(data: VariantHeaderDataModel): Int {
        return AtcVariantHeaderViewHolder.LAYOUT
    }

    override fun type(data: VariantShimmeringDataModel): Int {
        return AtcVariantShimmeringViewHolder.LAYOUT
    }

    override fun type(data: VariantQuantityDataModel): Int {
        return AtcVariantQuantityViewHolder.LAYOUT
    }

    override fun type(data: VariantComponentDataModel): Int {
        return AtcVariantComponentViewHolder.LAYOUT
    }

    override fun type(data: VariantErrorDataModel): Int {
        return AtcVariantErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            AtcVariantHeaderViewHolder.LAYOUT -> AtcVariantHeaderViewHolder(view, variantListener, atcVariantBottomSheetListener)
            AtcVariantQuantityViewHolder.LAYOUT -> AtcVariantQuantityViewHolder(view, variantListener, compositeSubscription)
            AtcVariantShimmeringViewHolder.LAYOUT -> AtcVariantShimmeringViewHolder(view)
            AtcVariantComponentViewHolder.LAYOUT -> AtcVariantComponentViewHolder(view, variantListener)
            AtcVariantErrorViewHolder.LAYOUT -> AtcVariantErrorViewHolder(view, variantListener)
            else -> return super.createViewHolder(view, type)
        }
    }
}