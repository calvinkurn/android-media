package com.tkpd.atc_variant.views.adapter

import com.tkpd.atc_variant.data.VariantHeaderDataModel
import com.tkpd.atc_variant.data.VariantStockDataModel
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

/**
 * Created by Yehezkiel on 06/05/21
 */

class AtcVariantAdapterTypeFactoryImpl : BaseAdapterTypeFactory(), AtcVariantTypeFactory {

    override fun type(data: VariantHeaderDataModel): Int {
        return 0
    }

    override fun type(data: VariantStockDataModel): Int {
        return 0
    }
//
//    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
////        return when (type) {
////
////            else -> return super.createViewHolder(view, type)
////        }
//    }
}