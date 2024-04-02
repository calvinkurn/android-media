package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.kotlin.extensions.view.EMPTY

abstract class BaseEPharmacySimpleDataModelImpl(
    open val name: String = String.EMPTY,
    open val type: String = String.EMPTY
) : BaseEPharmacyDataModel {

    override fun name(): String = name

    override fun type(): String = type

    override fun equalsWith(newData: BaseEPharmacyDataModel): Boolean {
        return this == newData
    }

    override fun getChangePayload(newData: BaseEPharmacyDataModel): Bundle? {
        return null
    }
}
