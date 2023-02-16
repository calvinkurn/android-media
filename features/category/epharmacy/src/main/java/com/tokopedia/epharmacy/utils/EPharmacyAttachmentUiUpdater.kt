package com.tokopedia.epharmacy.utils

import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyPrescriptionDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyStaticInfoDataModel

class EPharmacyAttachmentUiUpdater(var mapOfData: LinkedHashMap<String, BaseEPharmacyDataModel>) {

    fun updateModel(model: BaseEPharmacyDataModel){
        updateData(model.name(),model)
    }

    private fun updateData(key: String, baseCatalogDataModel: BaseEPharmacyDataModel) {
        mapOfData[key] = baseCatalogDataModel
    }
}
