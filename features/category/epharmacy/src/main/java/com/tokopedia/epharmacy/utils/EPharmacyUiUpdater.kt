package com.tokopedia.epharmacy.utils

import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel

class EPharmacyUiUpdater(var mapOfData: MutableMap<String, BaseEPharmacyDataModel>) {

    init {
        updateModel(EPharmacyPrescriptionDataModel(PRESCRIPTION_COMPONENT,
            PRESCRIPTION_COMPONENT, arrayListOf()))
    }

    val prescriptionInfoMap: EPharmacyPrescriptionDataModel?
        get() = mapOfData[PRESCRIPTION_COMPONENT] as? EPharmacyPrescriptionDataModel

    fun updateModel(model: BaseEPharmacyDataModel){
        updateData(model.type(),model)
    }

    private fun updateData(key: String, baseCatalogDataModel: BaseEPharmacyDataModel) {
        mapOfData[key] = baseCatalogDataModel
    }
}