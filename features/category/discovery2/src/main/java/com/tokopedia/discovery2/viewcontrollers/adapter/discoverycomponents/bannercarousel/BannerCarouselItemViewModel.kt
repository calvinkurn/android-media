package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Constant.CompType.SHOP_CARD
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class BannerCarouselItemViewModel(
    application: Application,
    private val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        componentData.value = components
    }

    fun getComponentLiveData(): LiveData<ComponentsItem> = componentData

    fun getBannerData(): DataItem? {
        return getDataItem()
    }

    fun getNavigationUrl(): String? {
        return getDataItem()?.imageClickUrl
    }

    fun getCompType(): String {
        return components.properties?.compType.orEmpty()
    }

    fun isCompTypeShopCard(): Boolean {
        return getCompType() == SHOP_CARD
    }

    private fun getDataItem(): DataItem? {
        components.data?.let {
            if (it.isNotEmpty()) return it[0]
        }
        return null
    }
}
