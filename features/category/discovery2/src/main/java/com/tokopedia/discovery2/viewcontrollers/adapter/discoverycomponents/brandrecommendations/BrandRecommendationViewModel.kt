package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.BrandRecommendation.SQUARE_DESIGN
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel


class BrandRecommendationViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var listData: List<ComponentsItem>? = null
    private val componentItemLiveData: MutableLiveData<List<ComponentsItem>> = MutableLiveData()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentData.value = components
    }

    fun mapBrandRecomItems() {
        components.data?.let {
            if(listData == null){
                listData = DiscoveryDataMapper.
                mapListToComponentList(it, ComponentNames.BrandRecommendationItem.componentName,
                        components.name, position, components.properties?.type ?: SQUARE_DESIGN).
                filter { list -> !list.data?.firstOrNull()?.imageUrlMobile.isNullOrEmpty() }
            }
            componentItemLiveData.value = listData
        }
    }

    fun getComponentDataLiveData(): LiveData<ComponentsItem> = componentData
    fun getListDataLiveData(): LiveData<List<ComponentsItem>> = componentItemLiveData
    fun getComponentPosition() = position
    fun getListData() = listData

}