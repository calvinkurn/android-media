package com.tokopedia.topads.credit.history.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.experimental.CoroutineContext

class TopAdsCreditHistoryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       private val userSessionInterface: UserSessionInterface,
                                                       @Named("Main")
                                                       private val baseDispatcher: CoroutineDispatcher)
    : ViewModel(), CoroutineScope {
    private val job = Job()

    val creditsHistory = MutableLiveData<Result<TopAdsCreditHistory>>()

    override val coroutineContext: CoroutineContext
        get() = baseDispatcher + job


    fun getCreditHistory(rawQuery: String, startDate: Date? = null, endDate: Date? = null) {
        job.children.map { it.cancel() }
        val params = mapOf(
                PARAM_SHOP_ID to userSessionInterface.shopId.toInt(),
                PARAM_USER_ID to userSessionInterface.userId.toInt(),
                PARAM_START_DATE to startDate?.let { SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it) },
                PARAM_END_DATE to endDate?.let { SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it) }
        )
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_CREDIT_RESPONSE, params, false)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TopAdsCreditHistory.CreditsResponse>()

            if (data.response.errors.isEmpty())
                creditsHistory.value = Success(data.response.dataHistory)
            else
                creditsHistory.value = Fail(ResponseErrorException(data.response.errors))
        }){
            creditsHistory.value = Fail(it)
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