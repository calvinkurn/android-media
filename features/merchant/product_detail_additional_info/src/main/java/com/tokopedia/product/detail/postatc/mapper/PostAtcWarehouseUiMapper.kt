package com.tokopedia.product.detail.postatc.mapper

import com.tokopedia.product.detail.postatc.data.model.PostAtcInfo
import com.tokopedia.product.detail.postatc.data.model.PostAtcLayout

/**
 * Created by yovi.putra on 04/09/23"
 * Project name: android-tokopedia-core
 **/

internal fun PostAtcLayout.WarehouseInfo.asUiModel() = PostAtcInfo.WarehouseInfo(
    warehouseId = warehouseID
)
