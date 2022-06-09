package com.tokopedia.epharmacy.component.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel

class EPharmacyPrescriptionViewHolder(private val view: View,
                                      private val ePharmacyListener: EPharmacyListener) : AbstractViewHolder<EPharmacyPrescriptionDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.epharmacy_prescription_view_item
    }

    override fun bind(element: EPharmacyPrescriptionDataModel) {
        render(element)
    }

    private fun render(dataModel: EPharmacyPrescriptionDataModel) {

    }
}