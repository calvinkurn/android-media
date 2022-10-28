package com.tokopedia.devicefingerprint.datavisor.workmanager

interface DataVisorRepository {
    fun saveToken(token: String)
    fun getToken(): String
    fun saveWorkerTimeStamp(timeStamp: Long)
    fun getWorkerTimeStamp(): Long
    fun saveRunAttemptCount(runAttemptCount: Int)
    fun getRunAttemptCount(): Int
}