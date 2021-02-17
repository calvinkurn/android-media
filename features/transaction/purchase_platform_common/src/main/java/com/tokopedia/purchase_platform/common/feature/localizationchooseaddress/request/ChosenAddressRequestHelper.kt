package com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request

import com.tokopedia.usecase.RequestParams

// Todo : recheck param key name
const val KEY_CHOSEN_ADDRESS = "chosen_address"

fun addChosenAddressParam(requestParams: RequestParams): RequestParams {
    requestParams.putObject(KEY_CHOSEN_ADDRESS, getChosenAddress())
    return requestParams
}

fun getChosenAddress(): ChosenAddress? {
    // Todo : get data from local cache
//    return ChosenAddress()
    return null
}