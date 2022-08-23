package com.tokopedia.devicefingerprint.datavisor.repository

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance.Companion.DEFAULT_VALUE_DATAVISOR
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.NO_TIME_STAMP
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorRepository

internal class DataVisorSharedPreferences(
    val context: Context,
): DataVisorRepository {

    private fun editSharedPreference(action: SharedPreferences.Editor.() -> Unit) {
        val sp = getSharedPreferences() ?: return
        val editor = sp.edit()
        action(editor)
        editor.apply()
    }

    private fun getSharedPreferences(): SharedPreferences? {
        val sharedPrefName = VisorFingerprintInstance.getSharedPrefName()
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
    }

    override fun saveToken(token: String) {
        editSharedPreference {
            putString(KEY_TOKEN, token)
        }
    }

    override fun getToken(): String =
        getSharedPreferences()?.getString(KEY_TOKEN, DEFAULT_VALUE_DATAVISOR)
                ?: DEFAULT_VALUE_DATAVISOR

    override fun saveWorkerTimeStamp(timeStamp: Long) {
        editSharedPreference {
            putLong(KEY_TS_WORKER, timeStamp)
        }
    }

    override fun saveRunAttemptCount(runAttemptCount: Int) {
        editSharedPreference {
            putInt(KEY_TOKEN_RUN_ATTEMPT_COUNT, runAttemptCount)
        }
    }

    override fun getWorkerTimeStamp(): Long =
        getSharedPreferences()?.getLong(KEY_TS_WORKER, NO_TIME_STAMP) ?: NO_TIME_STAMP

    override fun getRunAttemptCount(): Int =
        getSharedPreferences()?.getInt(KEY_TOKEN_RUN_ATTEMPT_COUNT, 0) ?: 0

    companion object {
        private const val KEY_TOKEN = "tk"
        private const val KEY_TS_WORKER = "ts_worker"
        private const val KEY_TOKEN_RUN_ATTEMPT_COUNT = "tk_run_attempt_count"
    }
}