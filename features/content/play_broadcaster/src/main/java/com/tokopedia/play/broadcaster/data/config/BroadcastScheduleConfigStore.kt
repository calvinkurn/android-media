package com.tokopedia.play.broadcaster.data.config

import java.util.*


/**
 * Created by mzennis on 01/12/20.
 */
interface BroadcastScheduleConfigStore {

    fun setMinScheduleDate(date: Date)

    fun setMaxScheduleDate(date: Date)

    fun setDefaultScheduleDate(date: Date)

    fun getMinScheduleDate(): Date

    fun getMaxScheduleDate(): Date

    fun getDefaultScheduleDate(): Date
}