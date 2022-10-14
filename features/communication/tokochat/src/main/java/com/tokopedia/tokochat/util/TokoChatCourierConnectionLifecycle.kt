package com.tokopedia.tokochat.util

import com.gojek.courier.lifecycle.ConnectionLifecycle
import com.gojek.courier.lifecycle.LifecycleEvent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object TokoChatCourierConnectionLifecycle : ConnectionLifecycle {

    val publishSubject = PublishSubject.create<LifecycleEvent>()

    override fun observe(): Observable<LifecycleEvent> {
        return publishSubject.hide()
    }
}
