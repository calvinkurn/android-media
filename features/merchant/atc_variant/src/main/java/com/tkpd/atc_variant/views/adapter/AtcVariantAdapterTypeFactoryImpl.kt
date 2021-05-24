package com.tkpd.atc_variant.views.adapter

import android.view.View
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.data.uidata.VariantHeaderDataModel
import com.tkpd.atc_variant.data.uidata.VariantQuantityDataModel
import com.tkpd.atc_variant.data.uidata.VariantShimmeringDataModel
import com.tkpd.atc_variant.views.AtcVariantListener
import com.tkpd.atc_variant.views.viewholder.AtcVariantComponentViewHolder
import com.tkpd.atc_variant.views.viewholder.AtcVariantHeaderViewHolder
import com.tkpd.atc_variant.views.viewholder.AtcVariantQuantityViewHolder
import com.tkpd.atc_variant.views.viewholder.AtcVariantShimmeringViewHolder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by Yehezkiel on 06/05/21
 */

class AtcVariantAdapterTypeFactoryImpl(private val variantListener: AtcVariantListener) : BaseAdapterTypeFactory(), AtcVariantTypeFactory {

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

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            AtcVariantHeaderViewHolder.LAYOUT -> AtcVariantHeaderViewHolder(view, variantListener)
            AtcVariantQuantityViewHolder.LAYOUT -> AtcVariantQuantityViewHolder(view)
            AtcVariantShimmeringViewHolder.LAYOUT -> AtcVariantShimmeringViewHolder(view)
            AtcVariantComponentViewHolder.LAYOUT -> AtcVariantComponentViewHolder(view, variantListener)
            else -> return super.createViewHolder(view, type)
        }
    }
}