package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

private const val TOKOPOINTS_DETAIL_APPLINK = "tokopedia://tokopoints/tukar-detail/"

class TokopointsItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        dataItem.value = components.data?.get(0)
    }

    fun getDataItemValue(): LiveData<DataItem> = dataItem


    fun onTokopointsItemClicked(context: Context) {
        dataItem.value?.slug?.let {
            if (it.isNotEmpty()) {
                navigate(context, "${TOKOPOINTS_DETAIL_APPLINK}${it}")
            }
        }
    }
}