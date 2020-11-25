package com.tokopedia.talk.common.utils

interface TalkReplyLoadTimeMonitoringListener {
    fun onStartPltListener()
    fun onStopPltListener()
}

interface TalkReadingLoadTimeMonitoringListener {
    fun onStartPltListener()
    fun onStopPltListener()
}

interface TalkWriteLoadTimeMonitoringListener {
    fun onStartPltListener()
    fun onStopPltListener()
}

interface TalkInboxLoadTimeMonitoringListener {
    fun onStartPltListener()
    fun onStopPltListener()
}