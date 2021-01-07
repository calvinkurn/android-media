package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoveryext.checkForNullAndSize
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class CircularSliderBannerViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val title: MutableLiveData<String> = MutableLiveData()
    init {
        title.value = components.title
    }

    fun getTitleLiveData(): LiveData<String> {
        return title
    }

    fun getItemsList(): ArrayList<CircularModel>? {
        components.data?.let {
            return DiscoveryDataMapper.discoveryDataMapper.mapProductListToCircularModel(it)
        }
        return null
    }

    fun getBannerItem(position: Int): DataItem? {
        components.data.checkForNullAndSize(position)?.let {
            it[position].parentComponentName = components.name
            return it[position]
        }
        return null
    }

    fun getComponentPosition() = position
}