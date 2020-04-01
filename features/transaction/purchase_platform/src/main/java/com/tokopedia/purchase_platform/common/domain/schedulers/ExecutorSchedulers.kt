package com.tokopedia.purchase_platform.common.domain.schedulers

import rx.Scheduler

interface ExecutorSchedulers {

    val io: Scheduler

    val main: Scheduler
}