package com.tokopedia.promotionstarget.presenter

import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.subscriber.GratificationData
import com.tokopedia.promotionstarget.usecase.GetPopGratificationUseCase
import kotlinx.coroutines.*
import javax.inject.Inject

class DialogManagerPresenter @Inject constructor(val getPopGratificationUseCase: GetPopGratificationUseCase) {

    private val job = Job()
    private var scope = CoroutineScope(job + Dispatchers.Main)
    private val ceh = CoroutineExceptionHandler { _, e ->
        run {
            e.printStackTrace()
        }
    }

    suspend fun getGratificationAndShowDialog(gratificationData: GratificationData, method: Function1<GetPopGratificationResponse, Any>): Job {
        scope.launch(Dispatchers.IO + ceh) {
            val data = getPopGratificationUseCase.let {
                it.getResponse(it.getQueryParams(gratificationData.campaignSlug, gratificationData.page))
            }
            withContext(Dispatchers.Main) {
                method.invoke(data)
            }
        }
        return job
    }

    fun onDestroy() {
        if (job.isActive) {
            job.cancel()
        }
    }

}