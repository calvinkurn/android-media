package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel

data class EPharmacyStaticInfoDataModel(val name : String = "", val type : String = "")
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