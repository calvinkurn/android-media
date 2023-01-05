package com.tokopedia.epharmacy.adapters.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.component.model.*

interface EPharmacyAdapterFactory {

    fun type(data : EPharmacyStaticInfoDataModel) : Int
    fun type(data : EPharmacyPrescriptionDataModel) : Int
    fun type(data : EPharmacyProductDataModel) : Int
    fun type(data : EPharmacyAttachmentDataModel) : Int
    fun type(data : EPharmacyTickerDataModel) : Int
    fun type(data : EPharmacyShimmerDataModel) : Int
    fun type(data : EPharmacyAccordionProductDataModel) : Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
