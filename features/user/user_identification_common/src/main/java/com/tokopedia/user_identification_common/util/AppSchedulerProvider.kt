package com.tokopedia.user_identification_common.util

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AppSchedulerProvider : SchedulerProvider {
    override fun ui(): Scheduler? {
        return AndroidSchedulers.mainThread()
    }

    override fun io(): Scheduler? {
        return Schedulers.io()
    }
}