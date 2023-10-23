package com.tokopedia.gamification.giftbox.presentation.presenter

import android.content.Context
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.di.MAIN
import com.tokopedia.gamification.giftbox.data.entities.AutoApplyResponse
import com.tokopedia.gamification.giftbox.domain.AutoApplyUseCase
import com.tokopedia.gamification.giftbox.presentation.views.CustomToast
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class CouponListResultPresenter @Inject constructor(val context: Context,
                                                    val autoApplyUseCase: AutoApplyUseCase,
                                                    @Named(IO) val workerDispatcher: CoroutineDispatcher,
                                                    @Named(MAIN) val uiDispatcher: CoroutineDispatcher
) : CoroutineScope {
    companion object {
        const val HTTP_STATUS_OK = "200"
    }

    private val customToast: CustomToast = CustomToast

    override val coroutineContext: CoroutineContext = workerDispatcher

    fun autoApply(code: String, autoApplyMessage: String?) {
        launchCatchError(block = {
            val map = autoApplyUseCase.getQueryParams(code)
            val response = autoApplyUseCase.getResponse(map)
            withContext(uiDispatcher) {
                showAutoApplyMessage(response, autoApplyMessage)
            }
        }, onError = {})
    }

    private fun showAutoApplyMessage(autoApplyResponse: AutoApplyResponse, autoApplyMessage: String?) {
        val code = autoApplyResponse.tokopointsSetAutoApply?.resultStatus?.code
        val message = autoApplyResponse.tokopointsSetAutoApply?.resultStatus?.message?.firstOrNull().orEmpty()
        if (code == HTTP_STATUS_OK) {
            if (!autoApplyMessage.isNullOrEmpty()) {
                customToast.show(context, autoApplyMessage)
            } else {
                if (message.isNotEmpty()) {
                    customToast.show(context, message)
                }
            }
        } else {
            if (message.isNotEmpty()) {
                customToast.show(context, message, isError = true)
            }
        }
    }
}
