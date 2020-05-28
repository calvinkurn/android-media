package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ProductCardItemViewModel(val application: Application, private val components: ComponentsItem, val position:Int) : DiscoveryBaseViewModel() {

    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val shopBadge: MutableLiveData<Int> = MutableLiveData()
    private val freeOngkirImage: MutableLiveData<String> = MutableLiveData()
    private lateinit var context :Context


    companion object {
        const val OFFICIAL_STORE = 1
        const val GOLD_MERCHANT = 2
        const val EMPTY = 0
    }

    fun setContext(context: Context ){
        this.context = context
    }


    override fun initDaggerInject() {
    }


    fun getDataItemValue(): LiveData<DataItem> {
        dataItem.value = components.data?.get(0)
        return dataItem
    }

    fun getShopBadge(): LiveData<Int> {
        shopBadge.value = chooseShopBadge()
        return shopBadge
    }

    private fun chooseShopBadge(): Int {
        val data = components.data?.get(0)
        return if (data?.goldMerchant == true && data.officialStore == true) {
            OFFICIAL_STORE
        } else if (data?.goldMerchant == true) {
            GOLD_MERCHANT
        } else if (data?.officialStore == true) {
            OFFICIAL_STORE
        } else {
            EMPTY
        }
    }

    fun getFreeOngkirImage(): LiveData<String> {
        if (dataItem.value?.freeOngkir?.isActive!!) {
            freeOngkirImage.value = dataItem.value?.freeOngkir?.freeOngkirImageUrl
        } else {
            freeOngkirImage.value = ""
        }
        return freeOngkirImage
    }

    fun handleUIClick() {
        navigate(context, dataItem.value?.applinks)
    }

}