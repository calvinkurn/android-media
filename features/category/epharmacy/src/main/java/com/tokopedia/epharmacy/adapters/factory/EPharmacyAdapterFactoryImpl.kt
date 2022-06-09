package com.tokopedia.epharmacy.adapters.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyProductDataModel
import com.tokopedia.epharmacy.component.viewholder.EPharmacyPrescriptionViewHolder
import com.tokopedia.epharmacy.component.viewholder.EPharmacyProductViewHolder

class EPharmacyAdapterFactoryImpl(private val ePharmacyListener: EPharmacyListener) : BaseAdapterTypeFactory() , EPharmacyAdapterFactory {

    override fun type(data: EPharmacyPrescriptionDataModel): Int {
        return EPharmacyPrescriptionViewHolder.LAYOUT
    }

    override fun type(data: EPharmacyProductDataModel): Int {
        return EPharmacyProductViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type){
            EPharmacyPrescriptionViewHolder.LAYOUT -> EPharmacyPrescriptionViewHolder(view, ePharmacyListener)
            EPharmacyProductViewHolder.LAYOUT -> EPharmacyProductViewHolder(view, ePharmacyListener)
            else -> super.createViewHolder(view,type)
        }
    }
}