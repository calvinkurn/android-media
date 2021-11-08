package com.tokopedia.promocheckout.common.domain.event.repository

import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import rx.Observable
import rx.Subscriber

interface EventCheckRepository {
    fun postVerify(book: Boolean, eventVerifyBody: EventVerifyBody): Observable<EventVerifyResponse>
}