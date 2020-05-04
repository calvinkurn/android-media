package com.tokopedia.promocheckout.common.domain.event.repository

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.event.network_api.EventCheckoutApi
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import rx.Scheduler
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class EventCheckRepositoryImpl @Inject constructor(private val eventCheckoutApi: EventCheckoutApi): EventCheckRepository {

    override fun postVerify(book : Boolean, eventVerifyBody: EventVerifyBody,subscriber: Subscriber<EventVerifyResponse>?) {
       eventCheckoutApi.postVerify(createMapParam(book), eventVerifyBody)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(subscriber)
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