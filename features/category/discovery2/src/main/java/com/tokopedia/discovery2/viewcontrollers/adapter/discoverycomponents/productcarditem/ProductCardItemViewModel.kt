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
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero

class ProductCardItemViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private var componentName: String = ""
    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val shopBadge: MutableLiveData<Int> = MutableLiveData()
    private val stockWordData: StockWording = StockWording()
    private lateinit var context: Context


    companion object {
        const val OFFICIAL_STORE = 1
        const val GOLD_MERCHANT = 2
        const val EMPTY = 0
    }

    fun setContext(context: Context) {
        this.context = context
    }

    fun getComponentName(): String {
        components.name?.let {
            componentName = it
        }
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


    fun getFreeOngkirImage(dataItem: DataItem): String {
        val isBebasActive = dataItem.freeOngkir?.isActive
        return if (isBebasActive == true) {
            dataItem.freeOngkir.freeOngkirImageUrl
        } else {
            ""
        }
    }


    fun getPDPViewCount(dataItem: DataItem): String {
        val pdpViewData = dataItem.pdpView

        return if (pdpViewData.toIntOrZero() >= 1000) {
            Utils.getCountView(pdpViewData.toDoubleOrZero())
        } else {
            ""
        }
    }


    fun getInterestedCount(dataItem: DataItem): String {
        val notifyMeCount = dataItem.notifyMeCount
        val interestThreshold = dataItem.thresholdInterest

        return if (notifyMeCount != null && interestThreshold != null && notifyMeCount >= interestThreshold) {
            Utils.getCountView(notifyMeCount.toDouble(), "tertarik")
        } else {
            ""
        }
    }

    fun getStockWord(dataItem: DataItem): StockWording {
        var stockWordTitleColour = "#1e31353b"
        var stockWordTitle = dataItem.stockWording?.title
        var stockAvailableCount: String? = ""

        if (!stockWordTitle.isNullOrEmpty()) {
            stockWordData.title = stockWordTitle
            stockWordData.color = dataItem.stockWording?.color
                    ?: stockWordTitleColour
        } else {
            val campaignSoldCount = dataItem.campaignSoldCount
            val threshold: Int? = dataItem.threshold
            val customStock: Int? = dataItem.customStock

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
                            stockWordTitleColour = "#ef144a"
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
            stockWordData.title = stockWordTitle
            stockWordData.color = stockWordTitleColour
        }
        return stockWordData
    }

    fun handleUIClick() {
        dataItem.value?.applinks?.let { applink ->
            navigate(context, applink)
        }
    }

    override fun initDaggerInject() {
    }

}