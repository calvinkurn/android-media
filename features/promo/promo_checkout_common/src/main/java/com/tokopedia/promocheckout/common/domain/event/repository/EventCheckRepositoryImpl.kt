package com.tokopedia.promocheckout.common.domain.event.repository

import com.tokopedia.promocheckout.common.domain.event.network_api.EventCheckoutApi
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import rx.Observable
import javax.inject.Inject

class EventCheckRepositoryImpl @Inject constructor(private val eventCheckoutApi: EventCheckoutApi): EventCheckRepository {

    override fun postVerify(book : Boolean, eventVerifyBody: EventVerifyBody) : Observable<EventVerifyResponse> {
       return eventCheckoutApi.postVerify(createMapParam(book), eventVerifyBody)
    }

    companion object {
        const val ERROR_DEFAULT = "Terjadi Kesalahan, silakan ulang beberapa saat lagi"
        const val BOOK = "book"
    }

    fun createMapParam(book: Boolean): HashMap<String, Boolean> {
        val mapParam = HashMap<String, Boolean>()
        mapParam[BOOK] = book
        return mapParam
    }


}