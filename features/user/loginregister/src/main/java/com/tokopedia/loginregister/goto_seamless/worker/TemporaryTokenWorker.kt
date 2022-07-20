package com.tokopedia.loginregister.goto_seamless.worker

import android.content.Context
import androidx.work.*
import com.gojek.icp.identity.loginsso.data.models.Profile
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessHelper
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessLogger
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessPreference
import com.tokopedia.loginregister.goto_seamless.di.DaggerGotoSeamlessComponent
import com.tokopedia.loginregister.goto_seamless.model.GetTemporaryKeyParam
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TemporaryTokenWorker(val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    @Inject
    lateinit var getTemporaryKeyUseCase: GetTemporaryKeyUseCase

    @Inject
    lateinit var gotoSeamlessPreference: GotoSeamlessPreference

    @Inject
    lateinit var gotoSeamlessHelper: GotoSeamlessHelper

    @Inject
    lateinit var userSession: UserSessionInterface

    init {
        DaggerGotoSeamlessComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override suspend fun doWork(): Result {
        if(userSession.isLoggedIn) {
            return withContext(Dispatchers.IO) {
                try {
                    val params = GetTemporaryKeyParam(
                        module = GetTemporaryKeyUseCase.MODULE_GOTO_SEAMLESS,
                        currentToken = gotoSeamlessPreference.getTemporaryToken()
                    )
                    val result = getTemporaryKeyUseCase(params)
                    if(result.data.key.isNotEmpty()) {
                        gotoSeamlessPreference.storeTemporaryToken(result.data.key)
                        val profile = Profile(
                            accessToken = result.data.key,
                            name = userSession.name,
                            customerId = userSession.userId,
                            countryCode = "",
                            phone = userSession.phoneNumber,
                            email = userSession.email,
                            profileImageUrl = userSession.profilePicture
                        )
                        gotoSeamlessHelper.updateUserProfileToSDK(profile)
                    }
                    Result.success()
                } catch (e: Exception) {
                    GotoSeamlessLogger.logError("refresh_goto_token_worker", e)
                    Result.failure()
                }
            }
        } else {
            return Result.success()
        }
    }

    companion object {
        private const val WORKER_ID = "GOTO_SEAMLESS_WORKER"
        private const val REFRESH_INTERVAL_DAYS = 7L

        fun scheduleWorker(appContext: Context) {
            try {
                val periodicWorker = PeriodicWorkRequest
                    .Builder(TemporaryTokenWorker::class.java, REFRESH_INTERVAL_DAYS, TimeUnit.DAYS)
                    .setConstraints(Constraints.NONE)
                    .build()

                WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
                    WORKER_ID,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWorker
                )
            } catch (ex: Exception) {
            }
        }
    }
}