package com.tokopedia.broadcaster.mediator

interface LivePusherStatistic {
    fun getBandwidth(): String
    fun getTraffic(): String
    fun getFps(): String
}