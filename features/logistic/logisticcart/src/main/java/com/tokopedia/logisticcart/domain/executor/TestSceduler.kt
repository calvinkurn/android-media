package com.tokopedia.logisticcart.domain.executor

import rx.Scheduler
import rx.schedulers.Schedulers

class TestSceduler: SchedulerProvider {
    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()
}