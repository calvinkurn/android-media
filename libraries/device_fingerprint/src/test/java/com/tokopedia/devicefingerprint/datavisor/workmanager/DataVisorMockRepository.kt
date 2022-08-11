package com.tokopedia.devicefingerprint.datavisor.workmanager

import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance.Companion.DEFAULT_VALUE_DATAVISOR
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.NO_TIME_STAMP
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

class DataVisorMockRepository: DataVisorRepository {

    var _token: String = DEFAULT_VALUE_DATAVISOR
    var _workerTimeStamp: Long = NO_TIME_STAMP
    var _runAttemptCount: Int = 0

    override fun saveToken(token: String) {
        this._token = token
    }

    override fun getToken(): String = _token

    override fun saveWorkerTimeStamp(timeStamp: Long) {
        this._workerTimeStamp = timeStamp
    }

    override fun getWorkerTimeStamp(): Long = _workerTimeStamp

    override fun saveRunAttemptCount(runAttemptCount: Int) {
        this._runAttemptCount = runAttemptCount
    }

    override fun getRunAttemptCount(): Int {
        return this._runAttemptCount
    }

    fun verifyToken(token: String?) {
        token ?: return

        assertThat(this._token, `is`(token))
    }

    fun reduceTimeStampBy(durationInMs: Long) {
        _workerTimeStamp -= durationInMs
    }

    fun verifyWorkerTimeStamp(workerTimeStamp: Long?) {
        workerTimeStamp ?: return

        assertThat(this._workerTimeStamp, `is`(workerTimeStamp))
    }

    fun verifyRunAttemptCount(expectedRunAttemptCount: Int?) {
        expectedRunAttemptCount ?: return

        assertThat(this._runAttemptCount, `is`(expectedRunAttemptCount))
    }
}