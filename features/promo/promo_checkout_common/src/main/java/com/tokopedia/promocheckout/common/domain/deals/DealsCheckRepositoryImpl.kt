package com.tokopedia.promocheckout.common.domain.deals

import com.google.gson.JsonObject
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import rx.Observable
import javax.inject.Inject

class DealsCheckRepositoryImpl @Inject constructor(private val dealsCheckoutApi: DealsCheckoutApi) : PromoCheckoutDealsRepository {

    override fun postVerify(book: Boolean, requestBody: JsonObject): Observable<DealsVerifyResponse> {
        return dealsCheckoutApi.postVerify(createMapParam(book), requestBody)
    }

    fun createMapParam(book: Boolean): HashMap<String, Boolean> {
        val mapParam = HashMap<String, Boolean>()
        mapParam[BOOK] = book
        return mapParam
    }

    companion object {
        const val BOOK = "book"
    }
}