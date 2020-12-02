package com.tokopedia.play.broadcaster.data.config

import java.util.*


/**
 * Created by mzennis on 01/12/20.
 */
interface BroadcastScheduleConfigStore {

    fun setMinimum(date: Date)

    fun setMaximum(date: Date)

    fun setDefault(date: Date)

    fun getMinimum(): Date

    fun getMaximum(): Date

    fun getDefault(): Date
}