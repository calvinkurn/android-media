package com.tokopedia.user.session.datastore.workmanager

import android.content.Context
import android.util.Log
import androidx.work.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.DataStoreMigrationHelper
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.datastore.workmanager.WorkOps.MIGRATED
import com.tokopedia.user.session.datastore.workmanager.WorkOps.NO_OPS
import com.tokopedia.user.session.datastore.workmanager.WorkOps.OPERATION_KEY
import com.tokopedia.user.session.di.ComponentFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataStoreMigrationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var dataStore: UserSessionDataStore

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
                    val syncResult = DataStoreMigrationHelper.checkDataSync(dataStore, userSession)
                    if (syncResult.isNotEmpty()) {
                        logSyncResult(syncResult)
                        migrateData()
                        ops = MIGRATED
                    }
                }
                Result.success(workDataOf(OPERATION_KEY to ops))
            } catch (e: Exception) {
                logWorkerError(e)
                Result.failure()
            }
        } else {
            return Result.success(workDataOf(OPERATION_KEY to NO_OPS))
        }
    }

    private suspend fun migrateData() {
        try {
            DataStoreMigrationHelper.migrateToDataStore(dataStore, userSession)
            // Check if still difference between the data
            val migrationResult = DataStoreMigrationHelper.checkDataSync(dataStore, userSession)
            if (migrationResult.isEmpty()) {
                dataStorePreference.setMigrationStatus(true)
            } else {
                dataStorePreference.setMigrationStatus(false)
                logMigrationResultFailed(migrationResult)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logMigrationException(e)
        }
    }

    private fun logMigrationResultFailed(result: List<String>) {
        ServerLogger.log(
            Priority.P2,
            USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "migration_result_failed",
                "total_difference" to result.size.toString(),
                "result_data" to result.toString()
            )
        )
    }

    private fun logSyncResult(result: List<String>) {
        ServerLogger.log(
            Priority.P2,
            USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "sync_result",
                "total_difference" to result.size.toString(),
                "result_data" to result.toString()
            )
        )
    }

    private fun logMigrationException(ex: Exception) {
        ServerLogger.log(
            Priority.P2,
            USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "migration_result_exception",
                "error" to Log.getStackTraceString(ex)
            )
        )
    }

    private fun logWorkerError(ex: Exception) {
        ServerLogger.log(
            Priority.P2,
            USER_SESSION_LOGGER_TAG,
            mapOf(
                "method" to "worker_error",
                "error" to Log.getStackTraceString(ex)
            )
        )
    }

    companion object {
        private const val WORKER_ID = "DATASTORE_MIGRATION_WORKER"
        private const val WORKER_ID_V2 = "DATASTORE_MIGRATION_WORKER_V2"

        const val USER_SESSION_LOGGER_TAG = "USER_SESSION_DATA_STORE"

        private const val WORKER_INTERVAL = 3L

        @JvmStatic
        fun scheduleWorker(context: Context) {
            try {
                val workManager = WorkManager.getInstance(context)
                workManager.cancelUniqueWork(WORKER_ID)
                val periodicWorker = PeriodicWorkRequest
                    .Builder(DataStoreMigrationWorker::class.java, WORKER_INTERVAL, TimeUnit.DAYS)
                    .setConstraints(Constraints.NONE)
                    .build()

                workManager.enqueueUniquePeriodicWork(
                    WORKER_ID_V2,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWorker
                )
            } catch (ex: Exception) {
            }
        }
    }
}
