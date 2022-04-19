package com.tokopedia.play.broadcaster.data.config

import com.tokopedia.play.broadcaster.util.extension.dayLater
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
    private var maximumDate: Date = Date().dayLater(7)
    private var defaultDate: Date = Date(System.currentTimeMillis() + (30 * (60 * 1000)))

    override fun setMinScheduleDate(date: Date) {
        minimumDate = date
    }

    override fun setMaxScheduleDate(date: Date) {
        maximumDate = date
    }

    override fun setDefaultScheduleDate(date: Date) {
        defaultDate = date
    }

    override fun getMinScheduleDate(): Date {
        return minimumDate
    }

    override fun getMaxScheduleDate(): Date {
        return maximumDate
    }

    override fun getDefaultScheduleDate(): Date {
        return defaultDate
    }
}