package com.tokopedia.entertainment.pdp.viewmodel

import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.google.gson.Gson
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeemedData
import java.lang.reflect.Type

fun createRedeemData(): EventRedeem {
    return Gson().fromJson(getJson("redeem_event.json"),
        EventRedeem::class.java)
}

fun createRedeemEmptyRedemptionsData(): EventRedeem {
    return Gson().fromJson(getJson("redeem_event_empty_redemption.json"),
        EventRedeem::class.java)
}

fun createRedeemEmptyRedemptionsDataMap(): Map<Type, RestResponse> {
    val restResponse = RestResponse(createRedeemEmptyRedemptionsData(), 200, false)
    return mapOf<Type, RestResponse>(
        EventRedeem::class.java to restResponse
    )
}

fun createRedeemDataMap(): Map<Type, RestResponse> {
    val restResponse = RestResponse(createRedeemData(), 200, false)
    return mapOf<Type, RestResponse>(
        EventRedeem::class.java to restResponse
    )
}

fun createRedeemDataMap(throwable: Throwable): Map<Type, RestResponse> {
    val restResponse = RestResponse(throwable, 500, false)
    return mapOf<Type, RestResponse>(
        Throwable::class.java to restResponse
    )
}

fun createRedeemNullDataMap(): Map<Type, RestResponse?> {
    return mapOf<Type, RestResponse?>(
        EventRedeem::class.java to null
    )
}

fun createRedeemErrorBodyDataMap(throwable: Throwable): Map<Type, RestResponse> {
    val restResponse = RestResponse(throwable, 500, false)
    restResponse.errorBody = getJson("redeem_event_error.json")
    restResponse.isError = true
    return mapOf<Type, RestResponse>(
        EventRedeem::class.java to restResponse
    )
}

fun createRedeemErrorBodyNullDataMap(throwable: Throwable): Map<Type, RestResponse> {
    val restResponse = RestResponse(throwable, 500, false)
    restResponse.errorBody = null
    restResponse.isError = true
    return mapOf<Type, RestResponse>(
        EventRedeem::class.java to restResponse
    )
}

fun createRedeemDataIsErrorTrueMap(): Map<Type, RestResponse> {
    val restResponse = RestResponse(createRedeemData(), 200, false)
    restResponse.errorBody = getJson("redeem_event_error.json")
    restResponse.isError = true
    return mapOf<Type, RestResponse>(
        EventRedeem::class.java to restResponse
    )
}

fun createRedeemedData(): EventRedeemedData {
    return Gson().fromJson(getJson("redeemed_event.json"),
        EventRedeemedData::class.java)
}
