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
    private val interestedCount: MutableLiveData<String> = MutableLiveData()
    private val stockWordLiveData: MutableLiveData<StockWording> = MutableLiveData()
    private lateinit var context: Context
    private var productItemData: DataItem? = null

    init {
        components.data?.let {
            if (it.isNotEmpty()) {
                productItemData = components.data?.get(0)
            }
        }
    }

    companion object {
        const val OFFICIAL_STORE = 1
        const val GOLD_MERCHANT = 2
        const val EMPTY = 0
    }

    fun setContext(context: Context) {
        this.context = context
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
        val isBebasActive = productItemData?.freeOngkir?.isActive

        if (isBebasActive == true) {
            freeOngkirImage.value = productItemData!!.freeOngkir!!.freeOngkirImageUrl
        } else {
            freeOngkirImage.value = ""
        }
        return freeOngkirImage
    }


    fun getPDPViewCount(): LiveData<String> {
        val pdpViewData = productItemData?.pdpView

        if (pdpViewData != null) {
            try {
                if (pdpViewData.toInt() >= 1000) {
                    pdpViewLiveCount.value = Utils.getCountView(pdpViewData.toDouble())
                } else {
                    pdpViewLiveCount.value = ""
                }
            } catch (exception: NumberFormatException) {
            }
        } else {
            pdpViewLiveCount.value = ""
        }
        return pdpViewLiveCount
    }


    fun getInterestedCount(): MutableLiveData<String> {
        val notifyMeCount = productItemData?.notifyMeCount
        val interestThreshold = productItemData?.thresholdInterest
        interestedCount.value = ""

        if (notifyMeCount != null && interestThreshold != null && notifyMeCount >= interestThreshold) {
            interestedCount.value = Utils.getCountView(notifyMeCount.toDouble(), "tertarik")
        } else {
            interestedCount.value = ""
        }
        return interestedCount
    }

    fun getStockWord(): LiveData<StockWording> {
        var stockWordTitleColour = "#1e31353b"
        var stockWordTitle = productItemData?.stockWording?.title
        var stockAvailableCount: String? = ""
        val stockWording = StockWording()

        if (!stockWordTitle.isNullOrEmpty()) {
            stockWording.title = stockWordTitle
            stockWording.color = productItemData?.stockWording?.color
                    ?: stockWordTitleColour
        } else {
            val campaignSoldCount = productItemData?.campaignSoldCount
            val threshold: Int? = productItemData?.threshold
            val customStock: Int? = productItemData?.customStock

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

    override fun initDaggerInject() {
    }

}