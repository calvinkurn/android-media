package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

private const val TOKOPOINTS_DETAIL_APPLINK = "tokopedia://tokopoints/tukar-detail/"

class TokopointsItemViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        dataItem.value = components.data?.get(0)
    }

    fun getDataItemValue():LiveData<DataItem> = dataItem

    override fun initDaggerInject() {

    }

    fun onTokopointsItemClicked() {
        if (dataItem.value?.slug != null && dataItem.value?.slug!!.isNotEmpty())
            RouteManager.route(application, "${TOKOPOINTS_DETAIL_APPLINK}${dataItem.value?.slug}")
    }
}