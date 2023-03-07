package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.network.response.PrescriptionImage

data class EPharmacyPrescriptionDataModel(val name : String = "", val type : String = "",
                                          var prescriptions : ArrayList<PrescriptionImage?>?,
                                          var isReUpload: Boolean = true)
    : BaseEPharmacyDataModel {

    override fun name(): String = name

    override fun type(): String  = type

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: BaseEPharmacyDataModel): Boolean {
        return this === newData
    }

    override fun getChangePayload(newData: BaseEPharmacyDataModel): Bundle? {
        return null
    }
}