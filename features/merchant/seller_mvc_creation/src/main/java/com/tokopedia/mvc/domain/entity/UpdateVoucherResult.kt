package com.tokopedia.mvc.domain.entity

data class UpdateVoucherResult(
    val updateVoucherModel: UpdateVoucherModel = UpdateVoucherModel()
) {
    data class UpdateVoucherModel(
        val status: Int = 0,
        val message: String = "",
        val processTime: Long = 0,
        val data: UpdateVoucherData = UpdateVoucherData()
    ) {
        data class UpdateVoucherData(
            val redirectUrl: String = "",
            val voucherId: Long = 0,
            val status: String = ""
        )
    }
}
