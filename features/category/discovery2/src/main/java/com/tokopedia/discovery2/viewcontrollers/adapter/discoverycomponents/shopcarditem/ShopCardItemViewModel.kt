package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcarditem

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ShopCardItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val componentPosition: MutableLiveData<Int?> = MutableLiveData()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
        componentPosition.value = position
        componentData.value = components
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getComponentPosition() = componentPosition

    fun getComponentName(): String {
        var componentName = ""
        components.name?.let {
            componentName = it
        }
        return componentName
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

    fun getUserID(): String? {
        return UserSession(application).userId
    }

    fun getComponentLiveData() = componentData

    fun getShopCardData(): DataItem? {
        return getShopCardDataItem()
    }

    private fun getShopCardDataItem(): DataItem? {
        return components.data?.firstOrNull()
    }

}