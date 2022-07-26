package com.tokopedia.user.session.datastore.workmanager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.DataStoreMigrationHelper
import com.tokopedia.user.session.datastore.UserSessionAbTestPlatform
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class DataStoreMigrationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private fun getDataStoreMigrationPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(DATA_STORE_PREF, Context.MODE_PRIVATE)
    }

    val dataStore = UserSessionDataStoreClient.getInstance(applicationContext)
    val userSession = UserSession(applicationContext)

    private fun isMigrationSuccess(): Boolean =
        getDataStoreMigrationPreference(applicationContext).getBoolean(KEY_MIGRATION_STATUS, false)

    private fun isEnableDataStore(): Boolean =
        UserSessionAbTestPlatform.isDataStoreEnable(applicationContext)

    override suspend fun doWork(): Result {
        if (isEnableDataStore()) {
            return withContext(Dispatchers.IO) {
                try {
                    if (userSession.isLoggedIn) {
                        val syncResult = DataStoreMigrationHelper.checkDataSync(applicationContext)
                        if (!isMigrationSuccess() &&
                            (dataStore.getUserId().first().isEmpty() || syncResult.isNotEmpty())
                        ) {
                            migrateData()
                        } else {
                            logSyncResult(syncResult)
                        }
                    }
                    Result.success()
                } catch (e: Exception) {
                    e.printStackTrace()
                    logWorkerError(e)
                    Result.failure()
                }
            }
        } else {
            return Result.success()
        }
    }

    private suspend fun migrateData() {
        try {
            DataStoreMigrationHelper.migrateToDataStore(dataStore, userSession)
            //Check if still difference between the data
            val migrationResult = DataStoreMigrationHelper.checkDataSync(applicationContext)
            if (migrationResult.isEmpty()) {
                getDataStoreMigrationPreference(applicationContext).edit()
                    .putBoolean(KEY_MIGRATION_STATUS, true).apply()
                logMigrationResultSuccess()
            } else {
                getDataStoreMigrationPreference(applicationContext).edit()
                    .putBoolean(KEY_MIGRATION_STATUS, false).apply()
                logMigrationResultFailed(migrationResult)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logMigrationException(e)
        }
    }

    private fun logMigrationResultSuccess() {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "migration_result_success"
            )
        )
    }

    private fun logMigrationResultFailed(result: List<String>) {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "migration_result_failed",
                "total_difference" to result.size.toString(),
                "result_data" to result.toString()
            )
        )
    }

    private fun logSyncResult(result: List<String>) {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "sync_result",
                "total_difference" to result.size.toString(),
                "result_data" to result.toString()
            )
        )
    }

    private fun logMigrationException(ex: Exception) {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "migration_result_exception",
                "error" to Log.getStackTraceString(ex)
            )
        )
    }

    private fun logWorkerError(ex: Exception) {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "worker_error",
                "error" to Log.getStackTraceString(ex)
            )
        )
    }

    companion object {
        const val WORKER_ID = "DATASTORE_MIGRATION_WORKER"

        const val DATA_STORE_PREF = "DATA_STORE_MIGRATION_PREF"
        const val KEY_MIGRATION_STATUS = "data_store_migration_status"

        const val USER_SESSION_LOGGER_TAG = "USER_SESSION_DATA_STORE"

        private const val WORKER_INTERVAL = 5L

        @JvmStatic
        fun scheduleWorker(context: Context) {
            try {
                val periodicWorker = PeriodicWorkRequest
                    .Builder(DataStoreMigrationWorker::class.java, WORKER_INTERVAL, TimeUnit.DAYS)
                    .setConstraints(Constraints.NONE)
                    .build()

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    WORKER_ID,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWorker
                )
            } catch (ex: Exception) {
            }
        }
    }
}