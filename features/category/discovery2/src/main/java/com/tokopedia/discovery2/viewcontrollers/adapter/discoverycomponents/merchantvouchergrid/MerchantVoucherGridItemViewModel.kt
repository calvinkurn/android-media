package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MerchantVoucherGridItemViewModel(
    application: Application,
    val component: ComponentsItem,
    val position: Int
): DiscoveryBaseViewModel(), CoroutineScope {

    private val componentData = MutableLiveData<DataItem>()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        component.data?.firstOrNull()?.let {
            componentData.value = it
        }
    }

    fun getComponentData(): LiveData<DataItem> = componentData
}
