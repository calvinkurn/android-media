package com.tokopedia.epharmacy.component.model

import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel

data class EPharmacyDataModel(
    val listOfComponents : MutableList<BaseEPharmacyDataModel> = mutableListOf()
)