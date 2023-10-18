package com.tokopedia.purchase_platform.common.schedulers

import rx.Scheduler

interface ExecutorSchedulers {

    val io: Scheduler

    val main: Scheduler
}
