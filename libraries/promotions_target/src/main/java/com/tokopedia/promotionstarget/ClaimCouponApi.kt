package com.tokopedia.promotionstarget

import com.tokopedia.promotionstarget.data.claim.ClaimPayload
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.usecase.ClaimPopGratificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import java.util.concurrent.Semaphore

//todo Rahul use proper throwable
//todo Make oldClaimPayload thread safe
class ClaimCouponApi(val scope: CoroutineScope,
                     val claimPopGratificationUseCase: ClaimPopGratificationUseCase) {

    private val HTTP_CODE_OK = "200"
    var oldClaimPayload: ClaimPayload? = null
    val semaphore = Semaphore(1)
    var oldApiJob: Deferred<ClaimPopGratificationResponse>? = null

    fun claimGratification(claimPayload: ClaimPayload, responseCallback: ClaimCouponApiResponseCallback) {

        suspend fun sendResponse(response: ClaimPopGratificationResponse?) {
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.popGratificationClaim?.resultStatus?.code == HTTP_CODE_OK) {
                        responseCallback.onNext(Success(response))
                    } else {
                        responseCallback.onNext(Fail(Throwable("UnknownError")))
                    }
                } else {
                    responseCallback.onError(Throwable("UnknownError"))
                }
            }
        }

        val ceh = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
            GlobalScope.launch(Dispatchers.Main) {
                responseCallback.onError(Throwable("UnknownError"))
            }

        }
        scope.launch(Dispatchers.IO + ceh) {
            supervisorScope {
                /*
                * 1. If currentPayload is same as old
                * 2. Then send back the previous response
                * 3. Clean resources
                * */
                if (isCurrentPayloadSameAsOld(claimPayload)) {
                    updateClaimPayload(null)
                    val response = oldApiJob?.await()
                    sendResponse(response)
                } else {
                    /*
                    * For different payload
                    * 1. Discard previous deferred call
                    * 2. Discard oldPayload
                    * 3. Make new api call
                    * */


                    updateClaimPayload(claimPayload)
                    oldApiJob?.cancel()

                    oldApiJob = async { claimPopGratificationUseCase.getResponse(claimPopGratificationUseCase.getQueryParams(claimPayload)) }
                    val response = oldApiJob?.await()
                    sendResponse(response)
                }
            }
        }
    }

    private fun updateClaimPayload(claimPayload: ClaimPayload?) {
        semaphore.acquireUninterruptibly()
        oldClaimPayload = claimPayload
        semaphore.release()
    }


    private fun isCurrentPayloadSameAsOld(currentClaimPayload: ClaimPayload): Boolean {
        return oldClaimPayload != null && oldClaimPayload?.campaignSlug == currentClaimPayload.campaignSlug && oldClaimPayload?.page == currentClaimPayload.page
    }
}

interface ClaimCouponApiResponseCallback {
    fun onNext(claimPopGratificationResponse: Result<ClaimPopGratificationResponse>)
    fun onError(th: Throwable)
}