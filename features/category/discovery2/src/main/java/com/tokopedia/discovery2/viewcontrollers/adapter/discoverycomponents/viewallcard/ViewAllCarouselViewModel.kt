package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.viewallcard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ViewAllCarouselViewModel(
    val application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components) {

    private val data: MutableLiveData<DataItem> = MutableLiveData()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.data?.firstOrNull()?.let {
            data.value = it
        }
    }

    fun getData(): LiveData<DataItem> = data
}
