package com.tokopedia.user.session.datastore.workmanager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.datastore.DataStoreMigrationHelper
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class DataStoreMigrationWorker(appContext: Context, workerParams: WorkerParameters): CoroutineWorker(appContext, workerParams) {

    private fun getDataStoreMigrationPreference(context: Context): SharedPreferences {
	return context.getSharedPreferences(DATA_STORE_PREF, Context.MODE_PRIVATE)
    }

    val dataStore = UserSessionDataStoreClient.getInstance(applicationContext)
    val userSession = UserSession(applicationContext)

    private fun isNeedMigration(): Boolean = !getDataStoreMigrationPreference(applicationContext).getBoolean(KEY_MIGRATION_STATUS, false)

    override suspend fun doWork(): Result {
	return withContext(Dispatchers.IO) {
	    try {
	        if(userSession.isLoggedIn) {
		    val syncResult = checkDataSync()
		    if (isNeedMigration() && (dataStore.getUserId().first().isEmpty() || syncResult.isNotEmpty())) {
			println("worker_migrate")
			migrateData()
		    } else {
			println("worker_check_only")
			logSyncResult(syncResult)
			println("worker_check_result: $syncResult")
		    }
		} else {
		    println("worker_do_nothing")
		}
		Result.success()
	    } catch (e: Exception) {
	        e.printStackTrace()
		logWorkerError(e)
	        Result.failure()
	    }
	}
    }

    private suspend fun migrateData() {
	try {
	    DataStoreMigrationHelper().migrateToDataStore(dataStore, userSession)
	    //Check if still difference between the data
	    val migrationResult = checkDataSync()
	    if(migrationResult.isEmpty()) {
		getDataStoreMigrationPreference(applicationContext).edit().putBoolean(KEY_MIGRATION_STATUS, true).apply()
		logMigrationResultSuccess()
		println("worker_migration_success")
	    } else {
		getDataStoreMigrationPreference(applicationContext).edit().putBoolean(KEY_MIGRATION_STATUS, false).apply()
		logMigrationResultFailed(migrationResult)
		println("worker_migration_failed $migrationResult")
	    }
	}catch (e: Exception) {
	    e.printStackTrace()
	    logMigrationException(e)
	}
    }

    private suspend fun checkDataSync(): List<String> {
	val result = mutableListOf<String>()
        val dataStore = UserSessionDataStoreClient.getInstance(applicationContext)
	val userSession = UserSession(applicationContext)

	if(dataStore.getName().first().trim() != userSession.name.trim()) {
	    result.add("name")
	}
	if(dataStore.getAccessToken().first().trim() != userSession.accessToken.trim()) {
	    result.add("accessToken")
	}
	if(dataStore.getRefreshToken().first().trim() != userSession.freshToken.trim()) {
	    result.add("refreshToken")
	}
	if(dataStore.getEmail().first().trim() != userSession.email.trim()) {
	    result.add("email")
	}
	if(dataStore.getUserId().first().trim() != userSession.userId.trim()) {
	    result.add("userId")
	}
	if(dataStore.getShopId().first().trim() != userSession.shopId.trim()) {
	    result.add("shopId")
	}
	if(dataStore.getShopName().first().trim() != userSession.shopName.trim()) {
	    result.add("shopName")
	}
	if(dataStore.isGoldMerchant().first() != userSession.isGoldMerchant) {
	    result.add("isGoldMerchant")
	}
	if(dataStore.isAffiliate().first() != userSession.isAffiliate) {
	    result.add("isAffiliate")
	}
	if(dataStore.getTempPhoneNumber().first().trim() != userSession.tempPhoneNumber.trim()) {
	    result.add("tempPhoneNumber")
	}
	if(dataStore.getTempEmail().first().trim() != userSession.tempEmail.trim()) {
	    result.add("tempEmail")
	}
	if(dataStore.isFirstTimeUser().first() != userSession.isFirstTimeUser) {
	    result.add("isFirstTimeUser")
	}
	if(dataStore.isMsisdnVerified().first() != userSession.isMsisdnVerified) {
	    result.add("isMsisdnVerified")
	}
	if(dataStore.hasPassword().first() != userSession.hasPassword()) {
	    result.add("hasPassword")
	}
	if(dataStore.getProfilePicture().first().trim() != userSession.profilePicture.trim()) {
	    result.add("profilePicture")
	}
	if(dataStore.hasShownSaldoWithdrawalWarning().first() != userSession.hasShownSaldoWithdrawalWarning()) {
	    result.add("saldoWithdrawalWaring")
	}
	if(dataStore.hasShownSaldoIntroScreen().first() != userSession.hasShownSaldoIntroScreen()) {
	    result.add("hasShownSaldoIntroScreen")
	}
	if(dataStore.getGCToken().first().trim() != userSession.gcToken?.trim()) {
	    result.add("gcToken")
	}
	if(dataStore.getShopAvatar().first().trim() != userSession.shopAvatar.trim()) {
	    result.add("shopAvatar")
	}
	if(dataStore.isPowerMerchantIdle().first() != userSession.isPowerMerchantIdle) {
	    result.add("isPowerMerchantIdle")
	}
	if(dataStore.getAutofillUserData().first().trim() != userSession.autofillUserData.trim()) {
	    result.add("autofillUserData")
	}
	if(dataStore.getLoginMethod().first().trim() != userSession.loginMethod.trim()) {
	    result.add("loginMethod")
	}
	if(dataStore.isShopOfficialStore().first() != userSession.isShopOfficialStore) {
	    result.add("isShopOfficialStore")
	}
	if(dataStore.getDeviceId().first().trim() != userSession.deviceId.trim()) {
	    result.add("deviceId")
	}
	if(dataStore.getFcmTimestamp().first() != userSession.fcmTimestamp) {
	    result.add("fcmTimestamp")
	}
	if(dataStore.isShopOwner().first() != userSession.isShopOwner) {
	    result.add("isShopOwner")
	}
	if(dataStore.isShopAdmin().first() != userSession.isShopAdmin) {
	    result.add("isShopAdmin")
	}
	if(dataStore.isLocationAdmin().first() != userSession.isLocationAdmin) {
	    result.add("isLocationAdmin")
	}
	if(dataStore.isMultiLocationShop().first() != userSession.isMultiLocationShop) {
	    result.add("isMultiLocationShop")
	}
	if(dataStore.isLoggedIn().first() != userSession.isLoggedIn) {
	    result.add("isLoggedIn")
	}
	if(dataStore.getPhoneNumber().first().trim() != userSession.phoneNumber.trim()) {
	    result.add("phoneNumber")
	}
	if(dataStore.getTokenType().first().trim() != userSession.tokenType.trim()) {
	    result.add("tokenType")
	}
	if(dataStore.getTwitterAccessTokenSecret().first().trim() != userSession.twitterAccessTokenSecret?.trim()) {
	    result.add("twitterAccessTokenSecret")
	}
	if(dataStore.getTwitterShouldPost().first() != userSession.twitterShouldPost) {
	    result.add("twitterShouldPost")
	}
	if(dataStore.getTwitterAccessToken().first().trim() != userSession.twitterAccessToken?.trim()) {
	    result.add("twitterAccessToken")
	}

	return result
    }

    private fun logMigrationResultSuccess() {
	ServerLogger.log(Priority.P2, "USER_SESSION_DATA_STORE",
	    mapOf(
		"type" to "migration_result_success"
	    )
	)
    }

    private fun logMigrationResultFailed(result: List<String>) {
	ServerLogger.log(Priority.P2, "USER_SESSION_DATA_STORE",
	    mapOf(
		"type" to "migration_result_failed",
		"total_difference" to result.size.toString(),
		"result_data" to result.toString()
	    )
	)
    }

    private fun logSyncResult(result: List<String>) {
	ServerLogger.log(Priority.P2, "USER_SESSION_DATA_STORE",
	    mapOf(
		"type" to "sync_result",
		"total_difference" to result.size.toString(),
		"result_data" to result.toString()
	    )
	)
    }

    private fun logMigrationException(ex: Exception) {
	ServerLogger.log(Priority.P2, "USER_SESSION_DATA_STORE",
	    mapOf(
		"type" to "migration_result_exception",
		"error" to Log.getStackTraceString(ex)
	    )
	)
    }

    private fun logWorkerError(ex: Exception) {
	ServerLogger.log(Priority.P2, "USER_SESSION_DATA_STORE",
	    mapOf(
		"type" to "worker_error",
		"error" to Log.getStackTraceString(ex)
	    )
	)
    }

    companion object {
	const val WORKER_ID = "DATASTORE_MIGRATION_WORKER"
	const val INTERVAL_WEEKLY = 7L

	const val DATA_STORE_PREF = "DATA_STORE_MIGRATION_PREF"
	const val KEY_MIGRATION_STATUS = "data_store_migration_status"

	fun scheduleWorker(context: Context) {
	    try {
		val periodicWorker = PeriodicWorkRequest
		    .Builder(DataStoreMigrationWorker::class.java, 15, TimeUnit.MINUTES)
		    .setConstraints(Constraints.NONE)
		    .build()

		WorkManager.getInstance(context).enqueueUniquePeriodicWork(
		    WORKER_ID,
		    ExistingPeriodicWorkPolicy.KEEP,
		    periodicWorker
		)
	    } catch (ex: Exception) { }
	}
    }
}