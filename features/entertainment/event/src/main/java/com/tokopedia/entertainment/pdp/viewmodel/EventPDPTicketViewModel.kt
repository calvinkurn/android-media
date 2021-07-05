package com.tokopedia.entertainment.pdp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.calendar.Legend
import com.tokopedia.entertainment.home.utils.DateUtils.SECOND_IN_MILIS
import com.tokopedia.entertainment.pdp.common.util.EventDateUtil
import com.tokopedia.entertainment.pdp.data.Category
import com.tokopedia.entertainment.pdp.data.EventPDPTicketModel
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.pdp.EventVerifyResponseV2
import com.tokopedia.entertainment.pdp.data.pdp.VerifyRequest
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class EventPDPTicketViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                                  private val graphqlRepository: GraphqlRepository,
                                                  private val usecase: EventProductDetailUseCase,
                                                  private val useCaseHoliday: TravelCalendarHolidayUseCase) : BaseViewModel(dispatcher) {

    val error: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val _ticketModel = MutableLiveData<List<EventPDPTicketModel>>()
    val ticketModel: LiveData<List<EventPDPTicketModel>> get() = _ticketModel

    private val _recommendationTicketModel = MutableLiveData<List<EventPDPTicketModel>>()
    val recommendationTicketModel: LiveData<List<EventPDPTicketModel>> get() = _recommendationTicketModel

    private val productDetailEntityMutable = MutableLiveData<EventProductDetailEntity>()
    val productDetailEntity: LiveData<EventProductDetailEntity>
        get() = productDetailEntityMutable

    private val mutableVerifyResponse = MutableLiveData<EventVerifyResponseV2>()
    val verifyResponse: LiveData<EventVerifyResponseV2>
        get() = mutableVerifyResponse

    private val eventHolidayMutable = MutableLiveData<List<Legend>>()
    val eventHoliday: LiveData<List<Legend>>
        get() = eventHolidayMutable


    var lists: MutableList<EventPDPTicketModel> = mutableListOf()
    var recommendationList: MutableList<EventPDPTicketModel> = mutableListOf()

    var categoryData = Category()

    fun getData(url: String, selectedDateString: String, state: Boolean, rawQueryPDP: String, rawQueryContent: String) {
        launch {
            lists.clear()
            recommendationList.clear()
            val data = usecase.executeUseCase(rawQueryPDP, rawQueryContent, state, url)
            val dataHoliday = useCaseHoliday.execute()
            val selectedDate = removeTime(Date(selectedDateString.toLong() * SECOND_IN_MILIS))

            when (data) {
                is Success -> {
                    productDetailEntityMutable.value = data.data.eventProductDetailEntity
                    data.data.eventProductDetailEntity.eventProductDetail.productDetailData.packages.forEach {
                        var isHaveSelectedDate = false
                        for (item in it.dates) {
                            val date = removeTime(Date(item.toLong() * SECOND_IN_MILIS))
                            if (date == selectedDate) {
                                isHaveSelectedDate = true
                                break
                            }
                        }
                        if (isHaveSelectedDate) {
                            it.isRecommendationPackage = false
                            lists.add(it)
                        } else if (it.dates.isNotEmpty()) {
                            it.isRecommendationPackage = true
                            recommendationList.add(it)
                        }
                    }

                    if (data.data.eventProductDetailEntity.eventProductDetail.productDetailData.category.isNotEmpty())
                        categoryData = data.data.eventProductDetailEntity.eventProductDetail.productDetailData.category[0]

                    _ticketModel.value = lists
                    _recommendationTicketModel.value = recommendationList
                }
                is Fail -> {
                    error.value = data.throwable.message
                }
            }

            when (dataHoliday) {
                is Success -> {
                    eventHolidayMutable.value = mappingHolidayData(dataHoliday.data)
                }

                is Fail -> {
                    eventHolidayMutable.value = arrayListOf()
                }
            }
        }
    }

    fun verify(rawQuery: String, verifyRequest: VerifyRequest) {
        val params = mapOf(eventVerify to verifyRequest)
        launchCatchError(block = {
            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, EventVerifyResponseV2::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<EventVerifyResponseV2>()
            if (data.eventVerify.error.isNullOrEmpty()) {
                mutableVerifyResponse.value = data
            } else {
                error.value = data.eventVerify.errorDescription
            }
        }) {
            error.value = it.message
        }
    }

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(EventDateUtil.stringToDate("yyyy-MM-dd", holiday.attribute.date),
                    holiday.attribute.label))
        }
        return legendList
    }

    private fun removeTime(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.time
    }

    companion object {
        const val eventVerify = "eventVerify"
    }
}