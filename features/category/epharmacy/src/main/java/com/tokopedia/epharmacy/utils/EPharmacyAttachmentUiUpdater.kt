package com.tokopedia.epharmacy.utils

import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyShimmerDataModel

class EPharmacyAttachmentUiUpdater(var mapOfData: LinkedHashMap<String, BaseEPharmacyDataModel>) {

    fun updateModel(model: BaseEPharmacyDataModel) {
        updateData(model.name(), model)
    }

    private fun updateData(key: String, baseCatalogDataModel: BaseEPharmacyDataModel) {
        mapOfData[key] = baseCatalogDataModel
    }

    fun addShimmer() {
        mapOfData.clear()
        updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_1, SHIMMER_COMPONENT))
        updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_2, SHIMMER_COMPONENT))
    }
}
