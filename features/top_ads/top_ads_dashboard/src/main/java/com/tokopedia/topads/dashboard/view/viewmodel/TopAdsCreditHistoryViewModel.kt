package com.tokopedia.topads.dashboard.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.AppExecutors
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor
import com.tokopedia.topads.dashboard.data.model.credit_history.TopAdsCreditHistory
import com.tokopedia.topads.dashboard.data.utils.extention.getRealData
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

class TopAdsCreditHistoryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       private val userSessionInterface: UserSessionInterface)
    : ViewModel(), CoroutineScope {
    private val job = Job()

    val creditsHistory = MutableLiveData<TopAdsCreditHistory>()
    val errors = MutableLiveData<Throwable>()

    override val coroutineContext: CoroutineContext
        get() = AppExecutors.uiContext + job


    fun getCreditHistory(rawQuery: String, startDate: Date? = null, endDate: Date? = null) {
        job.children.map { it.cancel() }
        val params = mapOf(
                PARAM_SHOP_ID to userSessionInterface.shopId.toInt(),
                PARAM_USER_ID to userSessionInterface.userId.toInt(),
                PARAM_START_DATE to startDate?.let { SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it) },
                PARAM_END_DATE to endDate?.let { SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it) }
        )
        launchCatchError(block = {
            val data = withContext(AppExecutors.bgContext){
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_CREDIT_RESPONSE, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getRealData<TopAdsCreditHistory.CreditsResponse>()

            if (data.response.errors.isEmpty())
                creditsHistory.value = data.response.dataHistory
            else
                errors.value = ResponseErrorException(data.response.errors)
        }){
            errors.value = it
        }
    }

    fun clearJob(){
        if (isActive){
            job.cancel()
        }
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_USER_ID = "userId"
        private const val PARAM_START_DATE = "startDate"
        private const val PARAM_END_DATE = "endDate"

        private val TYPE_CREDIT_RESPONSE = TopAdsCreditHistory.CreditsResponse::class.java
    }
}