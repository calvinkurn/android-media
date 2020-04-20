package com.tokopedia.purchase_platform.common.domain.schedulers

import rx.Scheduler
import rx.schedulers.Schedulers

object IOSchedulers: ExecutorSchedulers {

    override val io: Scheduler
        get() = Schedulers.io()

    override val main: Scheduler
        get() = Schedulers.io()
}