package com.tokopedia.purchase_platform.common.schedulers

import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object DefaultSchedulers : ExecutorSchedulers {

    override val io: Scheduler
        get() = Schedulers.io()

    override val main: Scheduler
        get() = AndroidSchedulers.mainThread()
}
