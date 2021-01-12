package com.tokopedia.topads.credit.history.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class TopAdsCreditHistoryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       private val userSessionInterface: UserSessionInterface,
                                                       private val autoTopUpUSeCase: TopAdsAutoTopUpUSeCase,
                                                       private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
                                                       @Named("Main")
                                                       val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val creditsHistory = MutableLiveData<Result<TopAdsCreditHistory>>()
    val getAutoTopUpStatus = MutableLiveData<Result<AutoTopUpStatus>>()
    val creditAmount = MutableLiveData<String>()


    fun getCreditHistory(rawQuery: String, startDate: Date? = null, endDate: Date? = null) {
        val params = mapOf(
                PARAM_SHOP_ID to userSessionInterface.shopId.toIntOrZero(),
                PARAM_USER_ID to userSessionInterface.userId.toIntOrZero(),
                PARAM_START_DATE to startDate?.let { SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it) },
                PARAM_END_DATE to endDate?.let { SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it) }
        )
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_CREDIT_RESPONSE, params, false)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TopAdsCreditHistory.CreditsResponse>()

            if (data.response.errors.isEmpty())
                creditsHistory.value = Success(data.response.dataHistory)
            else
                creditsHistory.value = Fail(ResponseErrorException(data.response.errors))
        }) {
            creditsHistory.value = Fail(it)
        }
    }

    fun getAutoTopUpStatus(rawQuery: String) {
        autoTopUpUSeCase.setQuery(rawQuery)
        autoTopUpUSeCase.setParams()
        autoTopUpUSeCase.execute({ data ->
            when {
                data.response == null -> getAutoTopUpStatus.value = Fail(Exception("Gagal mengambil status"))
                data.response.errors.isEmpty() -> getAutoTopUpStatus.value = Success(data.response.data)
                else -> getAutoTopUpStatus.value = Fail(ResponseErrorException(data.response.errors))
            }

        }, {
            getAutoTopUpStatus.value = Fail(it)
        })
    }

    fun getShopDeposit() {
        topAdsGetShopDepositUseCase.execute({
            creditAmount.value = it.topadsDashboardDeposits.data.amountFmt
        }
                , {
            Timber.e(it, "P1#TOPADS_CREDIT_HISTORY_VIEW_MODEL_DEPOSIT#%s", it.localizedMessage)

        })
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_USER_ID = "userId"
        private const val PARAM_START_DATE = "startDate"
        private const val PARAM_END_DATE = "endDate"

        private val TYPE_CREDIT_RESPONSE = TopAdsCreditHistory.CreditsResponse::class.java
    }
}