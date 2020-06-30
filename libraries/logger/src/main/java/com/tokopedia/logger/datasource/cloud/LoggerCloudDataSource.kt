package com.tokopedia.logger.datasource.cloud

abstract class LoggerCloudDataSource<T,U> {

    abstract suspend fun sendLogToServer(config: T, eventList: List<U>): Boolean

}