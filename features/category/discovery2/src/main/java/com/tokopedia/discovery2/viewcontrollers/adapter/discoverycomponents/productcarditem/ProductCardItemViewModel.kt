package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ProductCardItemViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val componentName: MutableLiveData<String> = MutableLiveData()
    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val shopBadge: MutableLiveData<Int> = MutableLiveData()
    private val freeOngkirImage: MutableLiveData<String> = MutableLiveData()
    private val pdpViewLiveCount: MutableLiveData<String> = MutableLiveData()
    private val stockWordLiveData: MutableLiveData<StockWording> = MutableLiveData()
    private lateinit var context: Context


    companion object {
        const val OFFICIAL_STORE = 1
        const val GOLD_MERCHANT = 2
        const val EMPTY = 0
    }

    fun setContext(context: Context) {
        this.context = context
    }


    override fun initDaggerInject() {
    }


    fun getComponentName(): MutableLiveData<String> {
        componentName.value = components.name
        return componentName
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

    // Apply Transformation on this live data
    fun getFreeOngkirImage(): LiveData<String> {
        dataItem.value?.freeOngkir?.isActive.let {
            if (dataItem.value?.freeOngkir?.isActive == true) {
                freeOngkirImage.value = dataItem.value?.freeOngkir?.freeOngkirImageUrl
            } else {
                freeOngkirImage.value = ""
            }
        }
        return freeOngkirImage
    }


    fun getPDPViewCount(): LiveData<String> {
        val pdpViewData = dataItem.value?.pdpView
        pdpViewData.let {
            try {
                if (pdpViewData?.toInt()!! >= 1000) {
                    val viewCount = Utils.getPDPCount(pdpViewData!!.toDouble())
                    if (viewCount.isNotEmpty()) {
                        pdpViewLiveCount.value = viewCount
                    } else {
                        pdpViewLiveCount.value = ""
                    }
                } else {
                    pdpViewLiveCount.value = ""
                }
            } catch (exception: NumberFormatException) {
            }
        }
        return pdpViewLiveCount
    }

    fun getStockWord(): LiveData<StockWording> {
        var stockWordTitleColour = "#1e31353b"
        var stockWordTitle = dataItem.value?.stockWording?.title
        var stockAvailableCount: String? = ""
        val stockWording = StockWording()

        if (stockWordTitle != null && stockWordTitle.isNotEmpty()) {
            stockWording.title = stockWordTitle
            stockWording.color = dataItem.value?.stockWording?.color
                    ?: stockWordTitleColour
        } else {
            val campaignSoldCount = dataItem.value?.campaignSoldCount
            val threshold: Int? = dataItem.value?.threshold
            val customStock: Int? = dataItem.value?.customStock

            if (campaignSoldCount != null && threshold != null && customStock != null) {
                if (campaignSoldCount > 0) {
                    when {
                        customStock == 0 -> {
                            stockWordTitle = "Terjual habis"
                        }
                        customStock == 1 -> {
                            stockWordTitle = "Stok terakhir, beli sekarang!"
                        }
                        customStock <= threshold -> {
                            stockWordTitle = "Tersisa"
                            stockAvailableCount = customStock.toString()
                            stockWordTitleColour = "##ef144a"
                        }
                        else -> {
                            stockWordTitle = "Terjual"
                            stockAvailableCount = campaignSoldCount.toString()
                        }
                    }
                    stockWordTitle += stockAvailableCount
                } else {
                    stockWordTitle = "Masih Tersedia"
                }
            }
            stockWording.title = stockWordTitle
            stockWording.color = stockWordTitleColour
        }
        stockWordLiveData.value = stockWording
        return stockWordLiveData
    }

    fun handleUIClick() {
        navigate(context, dataItem.value?.applinks)
    }

}