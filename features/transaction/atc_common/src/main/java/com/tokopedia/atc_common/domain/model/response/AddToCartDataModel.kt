package com.tokopedia.atc_common.domain.model.response

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

data class AddToCartDataModel(
    var errorMessage: ArrayList<String> = arrayListOf(),
    var status: String = "",
    var data: DataModel = DataModel(),
    var errorReporter: ErrorReporterModel = ErrorReporterModel()
) {
    companion object {
        const val STATUS_OK = "OK"
    }
}