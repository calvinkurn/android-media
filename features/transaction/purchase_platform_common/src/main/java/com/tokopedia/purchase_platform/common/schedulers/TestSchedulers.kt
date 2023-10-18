package com.tokopedia.purchase_platform.common.schedulers

import rx.Scheduler
import rx.schedulers.Schedulers

object TestSchedulers : ExecutorSchedulers {

    override val io: Scheduler
        get() = Schedulers.trampoline()

    override val main: Scheduler
        get() = Schedulers.trampoline()
}
