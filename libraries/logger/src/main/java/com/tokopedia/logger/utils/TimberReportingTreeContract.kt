package com.tokopedia.logger.utils

interface TimberReportingTreeContract {
    fun success()
    fun fail()
    fun logNotImportant()
}