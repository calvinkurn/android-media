package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardPaginationLoadState
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CalendarWidgetCarouselViewModel(
    val application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components), CoroutineScope {

    companion object {
        const val PRODUCT_PER_PAGE = 10
    }

    @JvmField
    @Inject
    var calenderWidgetUseCase: ProductCardsUseCase? = null
    private var isLoading = false
    private val calendarCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val calendarLoadError: MutableLiveData<Boolean> = MutableLiveData()
    private var maxHeight = 0

    fun getCalendarCarouselItemsListData(): LiveData<ArrayList<ComponentsItem>> =
        calendarCarouselList

    fun getCalendarLoadState(): LiveData<Boolean> = calendarLoadError

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
        handleErrorState()
        fetchProductCarouselData()
    }

    private fun handleErrorState() {
        if (components.verticalProductFailState) {
            calendarLoadError.value = true
        }
    }

    fun fetchProductCarouselData() {
        launchCatchError(block = {
            if (components.properties?.calendarType?.equals(Constant.Calendar.DYNAMIC) == true) {
                calenderWidgetUseCase?.loadFirstPageComponents(
                    components.id,
                    components.pageEndPoint,
                    PRODUCT_PER_PAGE
                )
            }
            components.shouldRefreshComponent = null
            setCalendarList()
        }, onError = {
            components.noOfPagesLoaded = 1
            components.verticalProductFailState = true
            components.shouldRefreshComponent = null
            calendarLoadError.value = true
        })
    }

    fun resetComponent() {
        components.noOfPagesLoaded = 0
        components.pageLoadedCounter = 1
    }

    private suspend fun setCalendarList() {
        val calendarList = getCalendarList()
        val calendarSize = calendarList?.size ?: 0
        calendarList?.let {
            if (it.isNotEmpty()) {
                calendarLoadError.value = false
                maxHeight = maxOf(reSyncProductCardHeight(it), maxHeight)
                it.forEach { item ->
                    val dataItem = item.data?.firstOrNull()
                    dataItem?.maxHeight = maxHeight
                    // WORKAROUND, because backend does not supply calendar Layout.
                    var calendarLayout = item.properties?.calendarLayout ?: ""
                    if (calendarLayout.isEmpty()) {
                        calendarLayout = when (calendarSize) {
                            1 -> {
                                Constant.Calendar.SINGLE
                            }
                            2 -> {
                                Constant.Calendar.DOUBLE
                            }
                            3 -> {
                                Constant.Calendar.TRIPLE
                            }
                            else -> {
                                Constant.Calendar.CAROUSEL
                            }
                        }
                        item.properties?.calendarLayout = calendarLayout
                    }
                }
                calendarCarouselList.value = addLoadMore(it)
                syncData.value = true
            } else {
                calendarLoadError.value = true
            }
        }
    }

    suspend fun reSyncProductCardHeight(list: ArrayList<ComponentsItem>): Int {
        val calendarCardModelArray = ArrayList<DataItem>()
        list.forEach {
            it.data?.firstOrNull()?.let { dataItem ->
                calendarCardModelArray.add(dataItem)
            }
        }
        val prop = list.firstOrNull()?.properties
        return calendarCardModelArray.getMaxHeightForCarouselView(
            application.applicationContext,
            Dispatchers.Default,
            prop?.calendarLayout,
            prop?.calendarType
        )
    }

    fun getCalendarList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { calendarList ->
            return calendarList as ArrayList<ComponentsItem>
        }
        return null
    }

    fun getPageSize() = PRODUCT_PER_PAGE

    fun isLoadingData() = isLoading

    fun fetchCarouselPaginatedCalendars() {
        isLoading = true
        launchCatchError(block = {
            when (calenderWidgetUseCase?.getCarouselPaginatedData(
                components.id,
                components.pageEndPoint,
                PRODUCT_PER_PAGE
            )) {
                ProductCardPaginationLoadState.LOAD_MORE -> {
                    getCalendarList()?.let {
                        maxHeight = maxOf(reSyncProductCardHeight(it), maxHeight)
                        it.forEach { item ->
                            item.data?.firstOrNull()?.maxHeight = maxHeight
                        }
                        isLoading = false
                        calendarCarouselList.value = addLoadMore(it)
                        syncData.value = true
                    }
                }

                ProductCardPaginationLoadState.REACH_END_OF_PAGE -> {
                    setCalendarList()
                }

                else -> {
                    paginatedErrorData()
                }
            }
        }, onError = {
            paginatedErrorData()
        })
    }

    private fun paginatedErrorData() {
        components.horizontalProductFailState = true
        getCalendarList()?.let {
            isLoading = false
            calendarCarouselList.value = addErrorReLoadView(it)
            syncData.value = true
        }
    }

    fun isLastPage(): Boolean {
        return !Utils.nextPageAvailable(components, PRODUCT_PER_PAGE)
    }

    private fun addLoadMore(calendarDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val calendarLoadState: ArrayList<ComponentsItem> = ArrayList()
        calendarLoadState.addAll(calendarDataList)
        if (Utils.nextPageAvailable(components, PRODUCT_PER_PAGE)) {
            calendarLoadState.add(
                ComponentsItem(name = ComponentNames.LoadMore.componentName).apply {
                    pageEndPoint = components.pageEndPoint
                    parentComponentId = components.id
                    id = ComponentNames.LoadMore.componentName
                    loadForHorizontal = true
                    discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
                }
            )
        }
        return calendarLoadState
    }

    private fun addErrorReLoadView(calendarDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val calendarLoadState: ArrayList<ComponentsItem> = ArrayList()
        calendarLoadState.addAll(calendarDataList)
        calendarLoadState.add(
            ComponentsItem(name = ComponentNames.CarouselErrorLoad.componentName).apply {
                pageEndPoint = components.pageEndPoint
                parentComponentId = components.id
                id = ComponentNames.CarouselErrorLoad.componentName
                parentComponentPosition = components.position
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            }
        )
        return calendarLoadState
    }
}
