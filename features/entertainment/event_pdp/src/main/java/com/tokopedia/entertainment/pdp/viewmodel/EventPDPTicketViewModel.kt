package com.tokopedia.entertainment.pdp.viewmodel

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.entertainment.pdp.common.util.EventDateUtil
import com.tokopedia.entertainment.pdp.data.Category
import com.tokopedia.entertainment.pdp.data.EventPDPTicketModel
import com.tokopedia.entertainment.pdp.data.Group
import com.tokopedia.entertainment.pdp.data.Package
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class EventPDPTicketViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                                  private val usecase: EventProductDetailUseCase) : BaseViewModel(dispatcher) {

    lateinit var resources: Resources

    val error: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val _ticketModel = MutableLiveData<List<EventPDPTicketModel>>()
    val ticketModel : LiveData<List<EventPDPTicketModel>> get() = _ticketModel

    var lists: MutableList<EventPDPTicketModel> = mutableListOf()
    var listsActiveDate: MutableList<Date> = mutableListOf()

    var categoryData = Category()

    var EXTRA_SCHEDULE_ID = ""
    var EXTRA_GROUPS_ID = ""

    fun getData(resources: Resources, url: String, selectedDate: String, state: Boolean) {
        this.resources = resources
        launch {
            lists.clear()
            listsActiveDate.clear()
            val data = usecase.executeUseCase(getRawQueryPDP(), getRawQueryContent(), state, url)
            when (data) {
                is Success -> {
                    if(selectedDate.isNotBlank()){
                        data.data.eventProductDetailEntity.eventProductDetail.productDetailData.schedules.forEach {
                            if(EventDateUtil.convertUnixToToday(it.schedule.startDate.toLong()) == selectedDate.toLong()){
                                EXTRA_SCHEDULE_ID = it.schedule.id
                                it.groups.forEach {
                                    EXTRA_GROUPS_ID = it.id
                                    it.packages.forEach { lists.add(it) }
                                }
                            }
                            listsActiveDate.add(Date(it.schedule.startDate.toLong()*1000L))
                        }
                    } else{
                        if(data.data.eventProductDetailEntity.eventProductDetail.productDetailData.schedules.size == 1) {
                            data.data.eventProductDetailEntity.eventProductDetail.productDetailData.schedules.forEach {
                                EXTRA_SCHEDULE_ID = it.schedule.id
                                it.groups.forEach {
                                    EXTRA_GROUPS_ID = it.id
                                    it.packages.forEach { lists.add(it) }
                                }
                            }
                        }
                    }
                    if(data.data.eventProductDetailEntity.eventProductDetail.productDetailData.category.isNotEmpty())
                        categoryData = data.data.eventProductDetailEntity.eventProductDetail.productDetailData.category[0]
                    _ticketModel.value = lists
                }
                is Fail -> {
                    error.value = data.throwable.toString()
                }
            }
        }
    }


    private fun getRawQueryPDP(): String {
        if (::resources.isInitialized){
            return GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_product_detail)
        }
        else return ""
    }

    private fun getRawQueryContent(): String {
        if (::resources.isInitialized)
            return GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_content_by_id)
        else return ""
    }
}