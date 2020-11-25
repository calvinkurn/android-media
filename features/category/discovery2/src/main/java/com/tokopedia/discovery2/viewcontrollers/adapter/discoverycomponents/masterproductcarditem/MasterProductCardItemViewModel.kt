package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.R
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
                dataItem.value = it[0]
                productCardModelLiveData.value = DiscoveryDataMapper().mapDataItemToProductCardModel(it[0], components.name)
            }
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
}