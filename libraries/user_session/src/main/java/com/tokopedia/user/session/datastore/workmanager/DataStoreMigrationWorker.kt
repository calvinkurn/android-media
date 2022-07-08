package com.tokopedia.user.session.datastore.workmanager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.datastore.DataStoreMigrationHelper
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker.Companion.WorkOps.LOG_SYNC
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker.Companion.WorkOps.MIGRATED
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker.Companion.WorkOps.NO_OPS
import com.tokopedia.user.session.di.ComponentFactory

import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataStoreMigrationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private fun getDataStoreMigrationPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(DATA_STORE_PREF, Context.MODE_PRIVATE)
    }

    val dataStore = UserSessionDataStoreClient.getInstance(applicationContext)

    private fun isMigrationSuccess(): Boolean =
        getDataStoreMigrationPreference(applicationContext).getBoolean(KEY_MIGRATION_STATUS, false)

    @Inject
    lateinit var dataStorePreference: DataStorePreference

    @Inject
    lateinit var userSession: UserSessionInterface

    init {
        ComponentFactory.instance.createUserSessionComponent(applicationContext).inject(this)
    }

    override suspend fun doWork(): Result {
        if (dataStorePreference.isDataStoreEnabled()) {
            return try {
                var ops = NO_OPS
                if (userSession.isLoggedIn) {
                    val syncResult = DataStoreMigrationHelper.checkDataSync(applicationContext)
                    if (!isMigrationSuccess() &&
                        (dataStore.getUserId().first().isEmpty() || syncResult.isNotEmpty())
                    ) {
                        migrateData()
                        ops = MIGRATED
                    } else {
                        logSyncResult(syncResult)
                        ops = LOG_SYNC
                    }
                }
                Result.success(workDataOf(OPERATION_KEY to ops))
            } catch (e: Exception) {
                e.printStackTrace()
                logWorkerError(e)
                Result.failure()
            }
        } else {
            return Result.success(workDataOf(OPERATION_KEY to WorkOps.NO_OPS))
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
                "type" to "migration_result_success"
            )
        )
    }

    private fun logMigrationResultFailed(result: List<String>) {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "type" to "migration_result_failed",
                "total_difference" to result.size.toString(),
                "result_data" to result.toString()
            )
        )
    }

    private fun logSyncResult(result: List<String>) {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "type" to "sync_result",
                "total_difference" to result.size.toString(),
                "result_data" to result.toString()
            )
        )
    }

    private fun logMigrationException(ex: Exception) {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "type" to "migration_result_exception",
                "error" to Log.getStackTraceString(ex)
            )
        )
    }

    private fun logWorkerError(ex: Exception) {
        ServerLogger.log(
            Priority.P2, USER_SESSION_LOGGER_TAG,
            mapOf(
                "type" to "worker_error",
                "error" to Log.getStackTraceString(ex)
            )
        )
    }

    companion object {
        const val WORKER_ID = "DATASTORE_MIGRATION_WORKER"

        const val DATA_STORE_PREF = "DATA_STORE_MIGRATION_PREF"
        const val KEY_MIGRATION_STATUS = "data_store_migration_status"

        const val USER_SESSION_LOGGER_TAG = "USER_SESSION_DATA_STORE"

        const val OPERATION_KEY = "operation"

        object WorkOps {
            const val NO_OPS = 0
            const val MIGRATED = 1
            const val LOG_SYNC = 2
        }

        @JvmStatic
        fun scheduleWorker(context: Context) {
            try {
                val periodicWorker = PeriodicWorkRequest
                    .Builder(DataStoreMigrationWorker::class.java, 5, TimeUnit.DAYS)
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