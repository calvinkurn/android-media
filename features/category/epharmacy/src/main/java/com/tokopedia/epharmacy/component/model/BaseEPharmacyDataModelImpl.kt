package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel

abstract class BaseEPharmacyDataModelImpl(
    open val name: String = "",
    open val type: String = "") : BaseEPharmacyDataModel {

    override fun name(): String = name

    override fun type(): String = type

    override fun equalsWith(newData: BaseEPharmacyDataModel): Boolean {
        return this == newData
    }

    override fun getChangePayload(newData: BaseEPharmacyDataModel): Bundle? {
        return null
    }
}
