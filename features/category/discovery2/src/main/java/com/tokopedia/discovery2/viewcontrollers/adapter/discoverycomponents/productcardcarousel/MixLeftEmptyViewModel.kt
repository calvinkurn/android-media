package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class MixLeftEmptyViewModel(
    application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {
    private val mixLeftBannerData: MutableLiveData<MixLeft> = MutableLiveData()
    fun getMixLeftBannerDataLD(): LiveData<MixLeft> = mixLeftBannerData

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        mixLeftBannerData.value = components.properties?.mixLeft
    }
}