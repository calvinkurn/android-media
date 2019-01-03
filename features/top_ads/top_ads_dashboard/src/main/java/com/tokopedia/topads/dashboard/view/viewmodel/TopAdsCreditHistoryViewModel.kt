package com.tokopedia.topads.dashboard.view.viewmodel

import android.arch.lifecycle.ViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.AppExecutors
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor
import com.tokopedia.topads.dashboard.data.model.credit_history.TopAdsCreditHistory
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

class TopAdsCreditHistoryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       private val userSessionInterface: UserSessionInterface,
                                                       private val topAdsDatePickerInteractor: TopAdsDatePickerInteractor)
    : ViewModel(), CoroutineScope {
    private val job = Job()

    val lastSelectionDatePickerIndex: Int
        get() = topAdsDatePickerInteractor.lastSelectionDatePickerIndex

    val lastSelectionDatePickerType: Int
        get() = topAdsDatePickerInteractor.lastSelectionDatePickerType

    val endDate: Date
        get() {
            val endCalendar = Calendar.getInstance()
            return topAdsDatePickerInteractor.getEndDate(endCalendar.time)
        }

    val startDate: Date
        get() {
            val startCalendar = Calendar.getInstance()
            startCalendar.add(Calendar.DAY_OF_YEAR, -DatePickerConstant.DIFF_ONE_WEEK)
            return topAdsDatePickerInteractor.getStartDate(startCalendar.time)
        }

    override val coroutineContext: CoroutineContext
        get() = AppExecutors.uiContext + job


    fun getCreditHistory(rawQuery: String, startDate: Date? = null, endDate: Date? = null,
                         onSucces: (TopAdsCreditHistory) -> Unit, onError: (Throwable) -> Unit) {
        job.children.map { it.cancel() }
        launch {
            val params = mapOf(
                    PARAM_SHOP_ID to userSessionInterface.shopId.toInt(),
                    PARAM_USER_ID to userSessionInterface.userId.toInt(),
                    PARAM_START_DATE to startDate?.let { SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it) },
                    PARAM_END_DATE to endDate?.let { SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(it) }
            )
            try {
                val response = withContext(AppExecutors.bgContext) {

                    val graphqlRequest = GraphqlRequest(rawQuery, TYPE_CREDIT_RESPONSE, params)
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                }

                val error = response.getError(TYPE_CREDIT_RESPONSE)
                if (error == null || error.isEmpty()){
                    val data = response.getData<TopAdsCreditHistory.CreditsResponse>(TYPE_CREDIT_RESPONSE)
                    if (data.response.errors.isEmpty())
                        onSucces(data.response.dataHistory)
                    else
                        onError(ResponseErrorException(data.response.errors))
                } else {
                    onError(MessageErrorException(error[0].message))
                }
            } catch (t: Throwable){
                onError(t)
            }

        }
    }

    fun isDateUpdated(startDate: Date?, endDate: Date?): Boolean {
        if (startDate == null && endDate == null) return true
        var dateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate)
        var cachedDateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(this.startDate)
        if (!dateText.equals(cachedDateText, true)) return true

        dateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate)
        cachedDateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(this.endDate)
        return !dateText.equals(cachedDateText, true)
    }

    fun saveDate(startDate: Date, endDate: Date) {
        topAdsDatePickerInteractor.saveDate(startDate, endDate)
    }

    fun saveSelectionDatePicker(selectionType: Int, lastSelection: Int) {
        topAdsDatePickerInteractor.saveSelectionDatePicker(selectionType, lastSelection)
    }

    fun resetDate() {
        topAdsDatePickerInteractor.resetDate()
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