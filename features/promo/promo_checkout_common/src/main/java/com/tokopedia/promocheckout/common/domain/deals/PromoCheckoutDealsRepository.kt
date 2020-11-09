package com.tokopedia.promocheckout.common.domain.deals

import com.google.gson.JsonObject
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import rx.Observable

interface PromoCheckoutDealsRepository {
    fun postVerify(book: Boolean, requestBody: JsonObject): Observable<DealsVerifyResponse>
}