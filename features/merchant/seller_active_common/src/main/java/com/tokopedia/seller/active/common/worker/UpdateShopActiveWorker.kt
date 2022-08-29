package com.tokopedia.seller.active.common.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.seller.active.common.domain.usecase.UpdateShopActiveUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateShopActiveWorker(
    appContext: Context, params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        private const val TAG_WORKER = "update_shop_active_worker"
        private var workRequest: OneTimeWorkRequest? = null

        @JvmStatic
        fun execute(context: Context) {
            if (workRequest == null) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                workRequest = OneTimeWorkRequest.Builder(UpdateShopActiveWorker::class.java)
                    .setConstraints(constraints)
                    .build()
            }

            workRequest?.let {
                WorkManager.getInstance(context)
                    .beginUniqueWork(TAG_WORKER, ExistingWorkPolicy.REPLACE, it)
                    .enqueue()
            }
        }
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val repository = GraphqlInteractor.getInstance().graphqlRepository
                val updateShopActiveUseCase = UpdateShopActiveUseCase(repository)
                updateShopActiveUseCase.setParam()
                updateShopActiveUseCase.executeOnBackground()
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}