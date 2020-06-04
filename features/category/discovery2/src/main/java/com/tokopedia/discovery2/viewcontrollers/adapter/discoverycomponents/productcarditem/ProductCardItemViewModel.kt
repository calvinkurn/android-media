package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeRequest
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.campaignusecase.CampaignNotifyUserCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductCardItemViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private var componentName: String = ""
    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val shopBadge: MutableLiveData<Int> = MutableLiveData()
    private val stockWordData: StockWording = StockWording()
    private lateinit var context: Context
    private var productData: DataItem? = null
    private val showLoginLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val notifyMeCurrentStatus: MutableLiveData<Boolean> = MutableLiveData()

    @Inject
    lateinit var campaignNotifyUserCase: CampaignNotifyUserCase

    companion object {
        const val OFFICIAL_STORE = 1
        const val GOLD_MERCHANT = 2
        const val EMPTY = 0
        const val SOURCE = "discovery"
        const val REGISTER = "REGISTER"
        const val UNREGISTER = "UNREGISTER"
    }

    init {
        components.data?.let {
            if (!it.isNullOrEmpty()) {
                productData = it[0]
            }
        }
        initDaggerInject()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getShowLoginData(): LiveData<Boolean> = showLoginLiveData
    fun notifyMeCurrentStatus(): LiveData<Boolean> = notifyMeCurrentStatus
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

    fun handleNavigation() {
        dataItem.value?.applinks?.let { applink ->
            navigate(context, applink)
        }
    }

    fun notifyMeVisibility(): Boolean? {
        return components.properties?.buttonNotification
    }

    fun subscribeUser() {
        if (isUserLoggedIn()) {
            productData?.let { productItemData ->
                launchCatchError(block = {
                    val campaignNotifyResponse = campaignNotifyUserCase.subscribeToCampaignNotifyMe(getNotifyRequestBundle(productItemData))
                    if (campaignNotifyResponse.checkCampaignNotifyMeResponse?.success == true) {
                        productItemData.notifyMe = !productItemData.notifyMe
                        notifyMeCurrentStatus.value = productItemData.notifyMe
                    }
                }, onError = {
                    it.printStackTrace()
                })
            }

        } else {
            showLoginLiveData.value = true
        }
    }

    private fun getNotifyRequestBundle(dataItem: DataItem): CampaignNotifyMeRequest {
        val campaignNotifyMeRequest = CampaignNotifyMeRequest()
        campaignNotifyMeRequest.campaignID = dataItem.campaignId.toIntOrZero()
        campaignNotifyMeRequest.productID = dataItem.productId.toIntOrZero()
        campaignNotifyMeRequest.action = if (dataItem.notifyMe) {
            UNREGISTER
        } else {
            REGISTER
        }
        campaignNotifyMeRequest.source = SOURCE
        return campaignNotifyMeRequest
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

}