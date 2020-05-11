package com.tokopedia.entertainment.pdp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.calendar.Legend
import com.tokopedia.entertainment.pdp.common.util.EventDateUtil
import com.tokopedia.entertainment.pdp.data.*
import com.tokopedia.entertainment.pdp.data.pdp.*
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventPDPViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                            private val usecase: EventProductDetailUseCase,
                                            private val useCaseHoliday: TravelCalendarHolidayUseCase
) : BaseViewModel(dispatcher) {

    private val eventProductDetaiListlMutable = MutableLiveData<List<EventPDPModel>>()
    val eventProductDetailList: LiveData<List<EventPDPModel>>
        get() = eventProductDetaiListlMutable

    private val eventProductDetailMutable = MutableLiveData<EventProductDetailEntity>()
    val eventProductDetail: LiveData<EventProductDetailEntity>
        get() = eventProductDetailMutable

    private val eventHolidayMutable = MutableLiveData<List<Legend>>()
    val eventHoliday: LiveData<List<Legend>>
        get() = eventHolidayMutable

    private val isErrorMutable = MutableLiveData<EventPDPErrorEntity>()
    val isError: LiveData<EventPDPErrorEntity>
        get() = isErrorMutable

    fun getIntialList() {
        val list: List<EventPDPModel> = requestEmptyViewModels()
        eventProductDetaiListlMutable.value = list
    }

    fun getDataProductDetail(rawQueryPDP: String, rawQueryContent: String, urlPdp: String) {
        launch {
            val result = usecase.executeUseCase(rawQueryPDP, rawQueryContent, true, urlPdp)
            val resultHoliday = useCaseHoliday.execute()
            when (result) {
                is Success -> {
                    eventProductDetailMutable.value = result.data.eventProductDetailEntity
                    getDataHighlight(mapperHighlight(result.data))
                    getDataAbout(mapperAbout(result.data))
                    getDataFacilities(mapperFacilities(result.data))
                    getDataLocationDetail(mapperLocationDetail(result.data))
                    getDataInformation(mapperInformation(result.data))
                }

                is Fail -> {
                    isErrorMutable.value = EventPDPErrorEntity(true, result.throwable.message)
                }
            }

            when(resultHoliday){
                is Success -> {
                    eventHolidayMutable.value = mappingHolidayData(resultHoliday.data)
                }

                is Fail -> {
                    eventHolidayMutable.value = arrayListOf()
                }
            }
        }
    }


    fun getDataHighlight(eventPDPHighlightEntity: EventPDPHighlightEntity) {
        eventProductDetailList.value?.let {
            val updatedList = it.toMutableList()
            updatedList[EVENT_PDP_DESC_ORDER] = eventPDPHighlightEntity
            updatedList[EVENT_PDP_DESC_ORDER].isLoaded = true
            eventProductDetaiListlMutable.value = updatedList
        }
    }

    fun getDataAbout(eventPDPAboutEntity: EventPDPAboutEntity) {
        eventProductDetailList.value?.let {
            val updatedList = it.toMutableList()
            updatedList[EVENT_PDP_ABOUT_ORDER] = eventPDPAboutEntity
            updatedList[EVENT_PDP_ABOUT_ORDER].isLoaded = true
            eventProductDetaiListlMutable.value = updatedList
        }
    }

    fun getDataFacilities(eventPDPFacilitiesEntity: EventPDPFacilitiesEntity) {
        eventProductDetailList.value?.let {
            val updatedList = it.toMutableList()
            updatedList[EVENT_PDP_FACILITIES_ORDER] = eventPDPFacilitiesEntity
            updatedList[EVENT_PDP_FACILITIES_ORDER].isLoaded = true
            eventProductDetaiListlMutable.value = updatedList
        }
    }

    fun getDataLocationDetail(eventPDPLocationDetailEntity: EventPDPLocationDetailEntity) {
        eventProductDetailList.value?.let {
            val updatedList = it.toMutableList()
            updatedList[EVENT_PDP_LOCATION_DETAIL_ORDER] = eventPDPLocationDetailEntity
            updatedList[EVENT_PDP_LOCATION_DETAIL_ORDER].isLoaded = true
            eventProductDetaiListlMutable.value = updatedList
        }
    }

    fun getDataInformation(eventPDPInformationEntity: EventPDPInformationEntity) {
        eventProductDetailList.value?.let {
            val updatedList = it.toMutableList()
            updatedList[EVENT_PDP_INFORMATION_ORDER] = eventPDPInformationEntity
            updatedList[EVENT_PDP_INFORMATION_ORDER].isLoaded = true
            eventProductDetaiListlMutable.value = updatedList
        }
    }

    private fun requestEmptyViewModels(): List<EventPDPModel> {
        return listOf(
                EventPDPHighlightEntity(),
                EventPDPAboutEntity(),
                EventPDPFacilitiesEntity(),
                EventPDPLocationDetailEntity(),
                EventPDPInformationEntity()
        )
    }

    private fun mapperHighlight(result: EventPDPContentCombined): EventPDPHighlightEntity {
        val list: MutableList<Highlight> = mutableListOf()
        val pdpData = result.eventProductDetailEntity.eventProductDetail.productDetailData
        val facilities = result.eventProductDetailEntity.eventProductDetail.productDetailData.facilities.sortedBy { it.priority }
        if (!facilities.isNullOrEmpty()) {
            for (i in facilities.indices) {
                if (facilities[i].type == typeHighlight)
                    list.add(Highlight(facilities[i].iconUrl,
                            facilities[i].title,
                            facilities[i].description))
            }
        }
        return EventPDPHighlightEntity(pdpData.cityName, pdpData.displayName, list)
    }

    private fun mapperFacilities(result: EventPDPContentCombined): EventPDPFacilitiesEntity {
        val list: MutableList<Facilities> = mutableListOf()
        val facilities = result.eventProductDetailEntity.eventProductDetail.productDetailData.facilities.sortedBy { it.priority }
        if (!facilities.isNullOrEmpty()) {
            for (i in facilities.indices) {
                if (facilities[i].type == typeFacilities)
                    list.add(facilities[i])
            }
        }
        return EventPDPFacilitiesEntity(list)
    }

    private fun mapperAbout(result: EventPDPContentCombined): EventPDPAboutEntity {
        var section = SectionData()
        val sectionsData = result.eventContentByIds.eventContentById.data.sectionData
        if (!sectionsData.isNullOrEmpty()) {
            for (i in sectionsData.indices) {
                if (sectionsData[i].section.equals(SECTION_ABOUT))
                    section = sectionsData[i]
            }
        }
        return EventPDPAboutEntity(result.eventProductDetailEntity.eventProductDetail.productDetailData.longRichDesc, section)
    }

    private fun mapperLocationDetail(result: EventPDPContentCombined): EventPDPLocationDetailEntity {
        var section = SectionData()
        var outlet = Outlet()
        val sectionsData = result.eventContentByIds.eventContentById.data.sectionData
        if(result.eventProductDetailEntity.eventProductDetail.productDetailData.outlets.isNotEmpty()) {
            outlet = result.eventProductDetailEntity.eventProductDetail.productDetailData.outlets[0]
            for (i in sectionsData.indices) {
                if (sectionsData[i].section.equals(SECTION_LOCATION))
                    section = sectionsData[i]
            }
        }
        return EventPDPLocationDetailEntity(outlet, section)
    }

    private fun mapperInformation(result: EventPDPContentCombined): EventPDPInformationEntity {
        var section = SectionData()
        val sectionsData = result.eventContentByIds.eventContentById.data.sectionData
        if (!sectionsData.isNullOrEmpty()) {
            for (i in sectionsData.indices) {
                if (sectionsData[i].section.equals(SECTION_INFORMATION))
                    section = sectionsData[i]
            }
        }
        return EventPDPInformationEntity(section)
    }

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(EventDateUtil.stringToDate("yyyy-MM-dd", holiday.attribute.date),
                    holiday.attribute.label))
        }
        return legendList
    }

    companion object {
        const val EVENT_PDP_DESC_ORDER = 0
        const val EVENT_PDP_ABOUT_ORDER = 1
        const val EVENT_PDP_FACILITIES_ORDER = 2
        const val EVENT_PDP_LOCATION_DETAIL_ORDER = 3
        const val EVENT_PDP_INFORMATION_ORDER = 4

        const val typeHighlight = 1
        const val typeFacilities = 2

        const val SECTION_ABOUT = "Tentang Kegiatan Ini"
        const val SECTION_LOCATION = "Gimana cara ke sana sih?"
        const val SECTION_INFORMATION = "Informasi Penting "

    }
}