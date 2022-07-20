package com.tokopedia.play.broadcaster.analytic.setup.schedule

/**
 * Created by kenny.hadisaputra on 24/03/22
 */
interface PlayBroScheduleAnalytic {

    /**
     * Old analytic
     */
    fun viewDialogConfirmDeleteSchedule()

    fun clickStartLiveBeforeScheduleTime()

    /**
     * New Analytic
     */
    fun clickSetSchedule()

    fun clickSaveSchedule()

    fun clickDeleteSchedule()

    fun clickCloseSetupSchedule()
}