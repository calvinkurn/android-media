package com.tokopedia.play.view.uimodel


/**
 * @author by astidhiyaa on 08/06/22
 */
data class WarehouseInfoUiModel(
    val warehouseId: String,
    val isOOC: Boolean,
) {
    companion object {
        val Empty: WarehouseInfoUiModel
            get() = WarehouseInfoUiModel(
                warehouseId = "0",
                isOOC = false,
            )
    }
}