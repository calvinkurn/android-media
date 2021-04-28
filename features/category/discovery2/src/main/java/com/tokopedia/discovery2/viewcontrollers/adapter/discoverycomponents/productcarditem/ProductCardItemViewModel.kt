package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcarditem

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.PDP_APPLINK
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeRequest
import com.tokopedia.discovery2.usecase.campaignusecase.CampaignNotifyUserCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardItemUseCase
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
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

private const val OFFICIAL_STORE = 1
private const val GOLD_MERCHANT = 2
private const val EMPTY = 0
private const val SOURCE = "discovery"
private const val REGISTER = "REGISTER"
private const val UNREGISTER = "UNREGISTER"
private const val NOTIFY_ME_TEXT = "tertarik"
private const val DEFAULT_COLOR = "#1e31353b"

class ProductCardItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val stockWordData: StockWording = StockWording()
    private lateinit var context: Context
    private val showLoginLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val notifyMeCurrentStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val showNotifyToast: MutableLiveData<Triple<Boolean, String?, Int?>> = MutableLiveData()
    private val componentPosition: MutableLiveData<Int?> = MutableLiveData()

    @Inject
    lateinit var campaignNotifyUserCase: CampaignNotifyUserCase
    @Inject
    lateinit var productCardItemUseCase: ProductCardItemUseCase
    @Inject
    lateinit var discoveryTopAdsTrackingUseCase: TopAdsTrackingUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentPosition.value = position
        components.data?.let {
            if (!it.isNullOrEmpty()) {
                dataItem.value = it[0]
            }
        }
    }

    fun getShowLoginData(): LiveData<Boolean> = showLoginLiveData
    fun notifyMeCurrentStatus(): LiveData<Boolean> = notifyMeCurrentStatus
    fun showNotifyToastMessage(): LiveData<Triple<Boolean, String?, Int?>> = showNotifyToast
    fun getComponentPosition() = componentPosition

    fun setContext(context: Context) {
        this.context = context
    }

    fun getComponentName(): String {
        var componentName = ""
        components.name?.let {
            componentName = it
        }
        return componentName
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

    fun getUserID():String? {
        return UserSession(application).userId
    }

    fun getDataItemValue() = dataItem

    fun chooseShopBadge(): Int {
        val productData = dataItem.value
        return if (productData?.goldMerchant == true && productData.officialStore == true) {
            OFFICIAL_STORE
        } else if (productData?.goldMerchant == true) {
            GOLD_MERCHANT
        } else if (productData?.officialStore == true) {
            OFFICIAL_STORE
        } else {
            EMPTY
        }
    }


    fun getFreeOngkirImage(dataItem: DataItem): String {
        val isBebasActive = dataItem.freeOngkir?.isActive
        return if (isBebasActive == true) {
            dataItem.freeOngkir?.freeOngkirImageUrl ?: ""
        } else {
            ""
        }
    }


    fun getPDPViewCount(dataItem: DataItem): String {
        val pdpViewData = dataItem.pdpView.toDoubleOrZero()
        return if (pdpViewData >= 1000) {
            Utils.getCountView(pdpViewData)
        } else {
            ""
        }
    }

    fun getInterestedCount(dataItem: DataItem): String {
        val notifyMeCount = dataItem.notifyMeCount
        val interestThreshold = dataItem.thresholdInterest
        return if (interestThreshold != null && notifyMeCount >= interestThreshold) {
            Utils.getCountView(notifyMeCount.toDoubleOrZero(), NOTIFY_ME_TEXT)
        } else {
            ""
        }
    }

    fun getStockWord(): StockWording {
        var stockWordTitleColour = getStockColor(R.color.clr_1e31353b)
        var stockWordTitle = dataItem.value?.stockWording?.title
        var stockAvailableCount: String? = ""

        dataItem.value?.let { it ->
            if (!stockWordTitle.isNullOrEmpty()) {
                stockWordData.title = stockWordTitle
                stockWordData.color = it.stockWording?.color
                        ?: stockWordTitleColour
            } else {
                val campaignSoldCount = it.campaignSoldCount
                val threshold: Int? = it.threshold.toIntOrZero()
                val customStock: Int? = it.customStock.toIntOrZero()

                if (campaignSoldCount != null && threshold != null && customStock != null) {
                    if (campaignSoldCount.toIntOrZero() > 0) {
                        when {
                            customStock == 0 -> {
                                stockWordTitle = getStockText(R.string.terjual_habis)
                            }
                            customStock == 1 -> {
                                stockWordTitle = getStockText(R.string.stok_terakhir_beli_sekarang)
                            }
                            customStock <= threshold -> {
                                stockWordTitle = getStockText(R.string.tersisa)
                                stockAvailableCount = customStock.toString()
                                stockWordTitleColour = getStockColor(R.color.clr_ef144a)
                            }
                            else -> {
                                stockWordTitle = getStockText(R.string.terjual)
                                stockAvailableCount = campaignSoldCount.toString()
                            }
                        }
                        stockWordTitle += stockAvailableCount
                    } else {
                        stockWordTitle = getStockText(R.string.masih_tersedia)
                    }
                }
                stockWordData.title = stockWordTitle
                stockWordData.color = stockWordTitleColour
            }
        }
        return stockWordData
    }

    private fun getStockText(textID: Int): String {
        var stockText = ""
        try {
            stockText = application.resources.getString(textID)
        } catch (exception: Resources.NotFoundException) {
        }
        return stockText
    }

    private fun getStockColor(colorID: Int): String {
        try {
            application.resources.getString(colorID)
        } catch (exception: Resources.NotFoundException) {
        }
        return DEFAULT_COLOR
    }

    fun handleNavigation() {
        val applink = dataItem.value?.applinks
        if (applink.isNullOrEmpty()) {
            val productId = dataItem.value?.productId
            if (!productId.isNullOrEmpty()) {
                navigate(context, "$PDP_APPLINK${productId}")
            }
        } else {
            navigate(context, applink)
        }
    }

    fun notifyMeVisibility(): Boolean {
        return components.data?.firstOrNull()?.notifyMe != null
    }

    fun subscribeUser() {
        if (isUserLoggedIn()) {
            dataItem.value?.let { productItemData ->
                productItemData.notifyMe?.let {
                    launchCatchError(block = {
                        val campaignNotifyResponse = campaignNotifyUserCase.subscribeToCampaignNotifyMe(getNotifyRequestBundle(productItemData))
                        campaignNotifyResponse.checkCampaignNotifyMeResponse?.let { campaignResponse ->
                            if (campaignResponse.success == true) {
                                productItemData.notifyMe = !it
                                notifyMeCurrentStatus.value = productItemData.notifyMe
                                showNotifyToast.value = Triple(false, campaignResponse.message, productItemData.campaignId.toIntOrZero())
                                this@ProductCardItemViewModel.syncData.value = productCardItemUseCase.notifyProductComponentUpdate(components.parentComponentId, components.pageEndPoint)
                            } else {
                                showNotifyToast.value = Triple(true, campaignResponse.errorMessage, 0)
                            }
                        }
                    }, onError = {
                        showNotifyToast.value = Triple(true, context.getString(R.string.product_card_error_msg), 0)
                        it.printStackTrace()
                    })
                }
            }
        } else {
            showLoginLiveData.value = true
        }
    }

    override fun loggedInCallback() {
        subscribeUser()
    }

    fun sendTopAdsClick() {
        dataItem.value?.let {
            val topAdsClickUrl = it.topadsClickUrl
            if (it.isTopads == true && topAdsClickUrl != null) {
                discoveryTopAdsTrackingUseCase.hitClick(this::class.qualifiedName, topAdsClickUrl, it.productId
                        ?: "", it.name ?: "", it.imageUrl ?: "")
            }
        }
    }

    fun sendTopAdsView() {
        dataItem.value?.let {
            val topAdsViewUrl = it.topadsViewUrl
            if (it.isTopads == true && topAdsViewUrl != null && !components.topAdsTrackingStatus) {
                discoveryTopAdsTrackingUseCase.hitImpressions(this::class.qualifiedName, topAdsViewUrl, it.productId
                        ?: "", it.name ?: "", it.imageUrl ?: "")
                components.topAdsTrackingStatus = true
            }
        }
    }

    private fun getNotifyRequestBundle(dataItem: DataItem): CampaignNotifyMeRequest {
        val campaignNotifyMeRequest = CampaignNotifyMeRequest()
        campaignNotifyMeRequest.campaignID = dataItem.campaignId.toIntOrZero()
        campaignNotifyMeRequest.productID = dataItem.productId.toIntOrZero()
        campaignNotifyMeRequest.action = if (dataItem.notifyMe == true) {
            UNREGISTER
        } else {
            REGISTER
        }
        campaignNotifyMeRequest.source = SOURCE
        return campaignNotifyMeRequest
    }


}