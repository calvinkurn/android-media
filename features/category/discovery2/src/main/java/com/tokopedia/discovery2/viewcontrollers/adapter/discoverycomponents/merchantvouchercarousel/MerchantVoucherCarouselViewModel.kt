package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val VOUCHER_PER_PAGE = 10

class MerchantVoucherCarouselViewModel(
    application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(),
    CoroutineScope {
    @JvmField
    @Inject
    var merchantVoucherUseCase: MerchantVoucherUseCase? = null

    private val _couponList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    val couponList: LiveData<ArrayList<ComponentsItem>> = _couponList
    private val _loadError: MutableLiveData<Boolean> = MutableLiveData()
    val loadError: LiveData<Boolean> = _loadError
    private val _headerData: MutableLiveData<ComponentsItem?> = MutableLiveData()
    val headerData: LiveData<ComponentsItem?> = _headerData

    private var isLoading = false

    fun fetchCouponData() {
        launchCatchError(block = {
            merchantVoucherUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint)
            components.shouldRefreshComponent = null
            setVoucherList()
        }, onError = {
                components.noOfPagesLoaded = 1
                components.shouldRefreshComponent = null
                _loadError.value = true
            })
    }

    fun fetchCarouselPaginatedCoupon() {
        isLoading = true
        launchCatchError(block = {
            if (merchantVoucherUseCase?.getCarouselPaginatedData(components.id, components.pageEndPoint) == true) {
                getVoucherList()?.let {
                    isLoading = false
                    _couponList.value = addLoadMore(it)
                    syncData.value = true
                }
            } else {
                paginatedErrorData()
            }
        }, onError = {
                paginatedErrorData()
            })
    }

    private fun paginatedErrorData() {
        components.horizontalProductFailState = true
        getVoucherList()?.let {
            isLoading = false
            _couponList.value = addErrorReLoadView(it)
            syncData.value = true
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private fun setVoucherList() {
        getVoucherList()?.let {
            if (it.isNotEmpty()) {
                _loadError.value = false
                _couponList.value = addLoadMore(it)
                syncData.value = true
            } else {
                _loadError.value = true
            }
        }
    }

    private fun getVoucherList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { productList ->
            return productList as ArrayList<ComponentsItem>
        }
        return null
    }

    fun getLihatSemuaHeader() {
        var lihatSemuaComponentData: ComponentsItem? = null
        components.lihatSemua?.let {
            if (!(
                components.noOfPagesLoaded == 1 && components.getComponentsItem()
                    .isNullOrEmpty()
                )
            ) {
                it.run {
                    val lihatSemuaDataItem = DataItem(
                        title = header,
                        subtitle = subheader,
                        btnApplink = applink
                    )
                    lihatSemuaComponentData = ComponentsItem(
                        name = ComponentsList.MerchantVoucherCarousel.componentName,
                        data = listOf(lihatSemuaDataItem),
                        creativeName = components.creativeName
                    )
                }
            }
        }
        _headerData.value = lihatSemuaComponentData
    }

    fun isLastPage(): Boolean {
        return !Utils.nextPageAvailable(components, VOUCHER_PER_PAGE)
    }

    fun isLoadingData() = isLoading

    fun getPageSize() = VOUCHER_PER_PAGE

    private fun addLoadMore(couponDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val componentList: ArrayList<ComponentsItem> = ArrayList()
        componentList.addAll(couponDataList)
        if (Utils.nextPageAvailable(components, VOUCHER_PER_PAGE)) {
            componentList.add(
                ComponentsItem(name = ComponentNames.LoadMore.componentName).apply {
                    pageEndPoint = components.pageEndPoint
                    parentComponentId = components.id
                    id = ComponentNames.LoadMore.componentName
                    loadForHorizontal = true
                    discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
                }
            )
        }
        return componentList
    }

    private fun addErrorReLoadView(couponDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val componentList: ArrayList<ComponentsItem> = ArrayList()
        componentList.addAll(couponDataList)
        componentList.add(
            ComponentsItem(name = ComponentNames.CarouselErrorLoad.componentName).apply {
                pageEndPoint = components.pageEndPoint
                parentComponentId = components.id
                parentComponentName = components.name
                id = ComponentNames.CarouselErrorLoad.componentName
                parentComponentPosition = components.position
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            }
        )
        return componentList
    }

    override fun refreshProductCarouselError() {
        getVoucherList()?.let {
            isLoading = false
            _couponList.value = it
            syncData.value = true
        }
    }
}
