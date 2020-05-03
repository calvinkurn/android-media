package com.tokopedia.logger.datasource.cloud

abstract class LoggerCloudDataSource<T> {

    abstract suspend fun sendLogToServer(token: String, eventList: List<T>): Boolean

}