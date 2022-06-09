package com.tokopedia.epharmacy.adapters.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel

interface EPharmacyAdapterFactory {

    fun type(data : EPharmacyPrescriptionDataModel) : Int
    fun type(data : EPharmacyProductDataModel) : Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}