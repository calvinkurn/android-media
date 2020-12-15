package com.tokopedia.logisticaddaddress.domain.usecase

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticaddaddress.data.repository.ShopAddressRepository
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import org.json.JSONObject
import rx.Observable
import javax.inject.Inject

class GetDistrictRecomToken @Inject constructor(val repository: ShopAddressRepository)
    : UseCase<Token>() {

    companion object {
        const val PARAM_AUTH = "PARAM_AUTH"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<Token> {
        val param = requestParams?.getObject(PARAM_AUTH) as Map<String, String>
        return repository.getLocation(param).map {
            val response = it.body()
            val jsonObject = JSONObject(response?.stringData)

            val data = GsonBuilder().create()
                    .fromJson(jsonObject.toString(), ShopAddressTokenResponse::class.java)
            data?.token
        }
    }

    internal inner class ShopAddressTokenResponse {
        @SerializedName("token")
        @Expose
        val token: Token? = null
    }

}