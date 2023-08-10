package com.tokopedia.autocompletecomponent

import com.tokopedia.autocompletecomponent.util.SchedulersProvider
import rx.Scheduler
import rx.internal.schedulers.TrampolineScheduler

internal object TestSchedulersProvider: SchedulersProvider {
    override fun io(): Scheduler = TrampolineScheduler.INSTANCE

    override fun ui(): Scheduler = TrampolineScheduler.INSTANCE

    override fun computation(): Scheduler = TrampolineScheduler.INSTANCE
}
