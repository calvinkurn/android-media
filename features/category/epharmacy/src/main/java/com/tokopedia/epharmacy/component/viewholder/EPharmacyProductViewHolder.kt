package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel

class EPharmacyProductViewHolder(private val view: View,
                                 private val ePharmacyListener: EPharmacyListener) : AbstractViewHolder<EPharmacyProductDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.epharmacy_product_view_item
    }

    override fun bind(element: EPharmacyProductDataModel) {
        renderProductData(element)
    }

    private fun renderProductData(dataModel: EPharmacyProductDataModel) {

    }
}