package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ProductCardItemViewModel(val application: Application, private  val components: ComponentsItem) : DiscoveryBaseViewModel() {

    val dataItem: MutableLiveData<DataItem> = MutableLiveData()

    override fun initDaggerInject() {
    }




    fun getDataItemValue(): LiveData<DataItem> {
        dataItem.value =  components.data?.get(0)
        return dataItem
    }
}