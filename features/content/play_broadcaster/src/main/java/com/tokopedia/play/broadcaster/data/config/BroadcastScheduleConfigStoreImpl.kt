package com.tokopedia.play.broadcaster.data.config

import java.util.*
import javax.inject.Inject


/**
 * Created by mzennis on 01/12/20.
 */
class BroadcastScheduleConfigStoreImpl @Inject constructor(): BroadcastScheduleConfigStore {

    /**
     * Seller can not pick start date < time.now()
     * Seller can only set start date plus 30 minutes from time.now()
     * Maximum scheduling date is time.now() + 7 days
     * [Docs](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/951092702/Save+as+draft+on+preparation+page)
     */
    private var minimumDate: Date = Date()
    private var maximumDate: Date = Date()
    private var defaultDate: Date = Date(System.currentTimeMillis() + (30*60000))

    override fun setMinimum(date: Date) {
        minimumDate = date
    }

    override fun setMaximum(date: Date) {
        maximumDate = date
    }

    override fun setDefault(date: Date) {
        defaultDate = date
    }

    override fun getMinimum(): Date {
        return minimumDate
    }

    override fun getMaximum(): Date {
        return maximumDate
    }

    override fun getDefault(): Date {
        return defaultDate
    }

}