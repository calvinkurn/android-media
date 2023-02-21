package com.tokopedia.editshipping.ui.customproductlogistic

import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel

sealed class CPLState {
    data class FirstLoad(val data: CustomProductLogisticModel) : CPLState()
    object Loading : CPLState()
    data class Update(val shipper: ShipperCPLModel, val shipperGroup: String) : CPLState()
    data class Failed(val throwable: Throwable) : CPLState()
}
