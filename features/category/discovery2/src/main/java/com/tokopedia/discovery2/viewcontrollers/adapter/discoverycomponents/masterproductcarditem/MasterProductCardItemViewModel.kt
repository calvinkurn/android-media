package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeRequest
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.campaignusecase.CampaignNotifyUserCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardItemUseCase
import com.tokopedia.discovery2.usecase.topAdsUseCase.DiscoveryTopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val REGISTER = "REGISTER"
private const val UNREGISTER = "UNREGISTER"
private const val SOURCE = "discovery"

class MasterProductCardItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val productCardModelLiveData: MutableLiveData<ProductCardModel> = MutableLiveData()
    private val componentPosition: MutableLiveData<Int?> = MutableLiveData()
    private val showLoginLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val notifyMeCurrentStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val showNotifyToast: MutableLiveData<Pair<Boolean, String?>> = MutableLiveData()

    @Inject
    lateinit var discoveryTopAdsTrackingUseCase: DiscoveryTopAdsTrackingUseCase

    @Inject
    lateinit var campaignNotifyUserCase: CampaignNotifyUserCase

    @Inject
    lateinit var productCardItemUseCase: ProductCardItemUseCase

    init {
        initDaggerInject()
        componentPosition.value = position
        components.data?.let {
            if (!it.isNullOrEmpty()) {
                val productData = it.first()
                productData.hasNotifyMe = getNotifyText(productData.notifyMe).isNotEmpty()
                dataItem.value = productData
                setProductStockWording(productData)
                productCardModelLiveData.value = DiscoveryDataMapper().mapDataItemToProductCardModel(productData, components.name)
            }
        }
    }

    private fun setProductStockWording(dataItem: DataItem) {
        if (dataItem.stockWording == null || dataItem.stockWording?.title.isNullOrEmpty()) {
            dataItem.stockWording = getStockWord(dataItem)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getComponentPosition() = componentPosition

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

    fun getDataItemValue() = dataItem
    fun getProductModelValue() = productCardModelLiveData

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

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun getTemplateType() = components.properties?.template ?: GRID
    fun getShowLoginData(): LiveData<Boolean> = showLoginLiveData
    fun notifyMeCurrentStatus(): LiveData<Boolean> = notifyMeCurrentStatus
    fun showNotifyToastMessage(): LiveData<Pair<Boolean, String?>> = showNotifyToast

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
                                showNotifyToast.value = Pair(false, campaignResponse.message)
                                this@MasterProductCardItemViewModel.syncData.value = productCardItemUseCase.notifyProductComponentUpdate(components.parentComponentId, components.pageEndPoint)
                            } else {
                                showNotifyToast.value = Pair(true, campaignResponse.errorMessage)
                            }
                        }
                    }, onError = {
                        showNotifyToast.value = Pair(true, "")
                        it.printStackTrace()
                    })
                }
            }
        } else {
            showLoginLiveData.value = true
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

    override fun loggedInCallback() {
        subscribeUser()
    }

    private fun getStockWord(dataItem: DataItem): StockWording {
        val stockWordData = StockWording()
        var stockWordTitle = dataItem.stockWording?.title
        var stockAvailableCount: String? = ""
        dataItem.let {
            if (!stockWordTitle.isNullOrEmpty()) {
                stockWordData.title = stockWordTitle
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
            }
        }
        return stockWordData
    }

    private fun getStockText(textID: Int): String {
        var stockText = ""
        try {
            stockText = application.applicationContext.resources.getString(textID)
        } catch (exception: Resources.NotFoundException) {
        }
        return stockText
    }

    fun getNotifyText(notifyStatus: Boolean?): String {
        notifyStatus?.let {
            return if (notifyStatus) application.applicationContext
                    .resources.getString(R.string.product_card_module_label_un_subscribe)
            else application.applicationContext.resources.getString(R.string.product_card_module_label_subscribe)
        }
        return ""
    }
}