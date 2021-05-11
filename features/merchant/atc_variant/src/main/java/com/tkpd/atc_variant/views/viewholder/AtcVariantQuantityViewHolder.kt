package com.tkpd.atc_variant.views.viewholder

import android.view.View
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.data.uidata.VariantQuantityDataModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by Yehezkiel on 11/05/21
 */
class AtcVariantQuantityViewHolder(private val view: View) : AbstractViewHolder<VariantQuantityDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_quantity_viewholder
    }

    override fun bind(element: VariantQuantityDataModel?) {

    }

}