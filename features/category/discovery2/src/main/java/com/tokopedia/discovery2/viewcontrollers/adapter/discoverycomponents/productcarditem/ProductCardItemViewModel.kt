package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.productcardcarousel.FreeOngkir
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ProductCardItemViewModel(val application: Application, private val components: ComponentsItem) : DiscoveryBaseViewModel() {

    val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val shopBadge: MutableLiveData<Int> = MutableLiveData()
    private val freeOngkirImage: MutableLiveData<String> = MutableLiveData()

    companion object {
        const val OFFICAIL_STORE = 1
        const val GOLD_MERCHANT = 2
        const val EMPTY = 0
    }


    override fun initDaggerInject() {
    }


    fun getDataItemValue(): LiveData<DataItem> {
        dataItem.value = components.data?.get(0)
        Log.d("sgvs",components.data?.get(0)?.name)
        return dataItem
    }

    fun getShopBadge(): LiveData<Int> {
        shopBadge.value = chooseShopBadge()
        return shopBadge
    }

    private fun chooseShopBadge(): Int {
        val data = components.data?.get(0)
        return if (data?.goldMerchant == true && data.officialStore == true) {
            OFFICAIL_STORE
        } else if (data?.goldMerchant == true) {
            GOLD_MERCHANT
        } else if (data?.officialStore == true) {
            OFFICAIL_STORE
        } else {
            EMPTY
        }
    }

    fun getFreeOngkirImage(): LiveData<String> {
//        val data = components.data?.get(0)
//        Log.d("freeongkirdata", data?.freeOngkir.toString())
      //  val freeOngkir = (data?.freeOngkir as Map<String,Any>)
//        val freeOngkir = GsonBuilder().create()
//                .fromJson<FreeOngkir>(data?.freeOngkir.toString(),
//                        FreeOngkir::class.java)
       // freeOngkirImage.value = if (freeOngkir.get("is_active") == true) freeOngkir.get("img_url").toString() else ""
        return freeOngkirImage
    }
}