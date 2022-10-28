package com.tokochat.tokochat_config_common.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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

class TokoChatProcessLifecycleObserver : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) {
        TokoChatCourierConnectionLifecycle.publishSubject.onNext(LifecycleEvent.Started)
    }

    override fun onStop(owner: LifecycleOwner) {
        TokoChatCourierConnectionLifecycle.publishSubject.onNext(LifecycleEvent.Stopped)
    }
}
