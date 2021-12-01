package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MerchantVoucherCarouselViewModel(application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(),
    CoroutineScope {
    @Inject
    lateinit var merchantVoucherUseCase: MerchantVoucherUseCase

    private val _couponList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    val couponList: LiveData<ArrayList<ComponentsItem>> = _couponList
    private val _loadError: MutableLiveData<Boolean> = MutableLiveData()
    val loadError: LiveData<Boolean> = _loadError
    private val _headerData: MutableLiveData<ComponentsItem?> = MutableLiveData()
    val headerData: LiveData<ComponentsItem?> = _headerData

    fun fetchCouponData() {
        launchCatchError(block = {
            merchantVoucherUseCase.loadFirstPageComponents(components.id, components.pageEndPoint)
            setVoucherList()
        }, onError = {
            components.noOfPagesLoaded = 1
            _loadError.value = true
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private fun setVoucherList() {
        getVoucherList()?.let {
            if (it.isNotEmpty()) {
                _loadError.value = false
                _couponList.value = it
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
            if (!(components.noOfPagesLoaded == 1 && components.getComponentsItem().isNullOrEmpty()))
                it.run {
                    val lihatSemuaDataItem = DataItem(title = header,
                        subtitle = subheader, btnApplink = applink)
                    lihatSemuaComponentData = ComponentsItem(
                        name = ComponentsList.MerchantVoucherCarousel.componentName,
                        data = listOf(lihatSemuaDataItem),
                        creativeName = components.creativeName)
                }
        }
        _headerData.value = lihatSemuaComponentData
    }

}