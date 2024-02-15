package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeRequest
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.setComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.campaignusecase.CampaignNotifyUserCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardItemUseCase
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

private const val REGISTER = "REGISTER"
private const val UNREGISTER = "UNREGISTER"
private const val SOURCE = "discovery"
private const val CAROUSEL_NOT_FOUND = -1

class MasterProductCardItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val dataItem: MutableLiveData<DataItem> = MutableLiveData()
    private val productCardModelLiveData: MutableLiveData<ProductCardModel> = MutableLiveData()
    private val componentPosition: MutableLiveData<Int?> = MutableLiveData()
    private val showLoginLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val notifyMeCurrentStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val showNotifyToast: SingleLiveEvent<Pair<Boolean, String?>> = SingleLiveEvent()
    private val scrollToSimilarProductComponentID: SingleLiveEvent<String> = SingleLiveEvent()
    private var lastQuantity: Int = 0

    @JvmField
    @Inject
    var discoveryTopAdsTrackingUseCase: TopAdsTrackingUseCase? = null

    @JvmField
    @Inject
    var campaignNotifyUserCase: CampaignNotifyUserCase? = null

    @JvmField
    @Inject
    var productCardItemUseCase: ProductCardItemUseCase? = null

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
        componentPosition.value = position
        components.data?.let {
            if (!it.isNullOrEmpty()) {
                val productData = it.first()
                productData.hasNotifyMe = getNotifyText(productData.notifyMe).isNotEmpty()
                dataItem.value = productData
                setProductStockWording(productData)
                lastQuantity = productData.quantity
                productCardModelLiveData.value =
                    DiscoveryDataMapper().mapDataItemToProductCardModel(
                        productData,
                        components.name,
                        components.properties?.cardType
                    )
            }
        }
    }

    private fun setProductStockWording(dataItem: DataItem) {
        if (dataItem.stockWording == null || dataItem.stockWording?.title.isNullOrEmpty()) {
            dataItem.stockWording = getStockWord(dataItem)
        } else if (dataItem.stockWording?.color.isNullOrEmpty()) {
            dataItem.stockWording?.let {
                it.color = getStockColor(unifyprinciplesR.color.Unify_NN950_20)
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

    fun getUserID(): String? {
        return UserSession(application).userId
    }

    fun getDataItemValue() = dataItem
    fun getProductModelValue() = productCardModelLiveData

    fun sendTopAdsClick() {
        dataItem.value?.let {
            val topAdsClickUrl = it.topadsClickUrl
            if (it.isTopads == true && topAdsClickUrl != null) {
                discoveryTopAdsTrackingUseCase?.hitClick(
                    this::class.qualifiedName,
                    topAdsClickUrl,
                    it.productId
                        ?: "",
                    it.name ?: "",
                    it.imageUrl ?: ""
                )
            }
        }
    }

    fun sendTopAdsView() {
        dataItem.value?.let {
            val topAdsViewUrl = it.topadsViewUrl
            if (it.isTopads == true && topAdsViewUrl != null && !components.topAdsTrackingStatus) {
                discoveryTopAdsTrackingUseCase?.hitImpressions(
                    this::class.qualifiedName,
                    topAdsViewUrl,
                    it.productId
                        ?: "",
                    it.name ?: "",
                    it.imageUrl ?: ""
                )
                components.topAdsTrackingStatus = true
            }
        }
    }

    fun getProductCardOptionsModel(): ProductCardOptionsModel {
        return components.data?.firstOrNull()?.let {
            ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = it.isWishList,
                productId = it.id.toString(),
                isTopAds = it.isTopads ?: false,
                topAdsWishlistUrl = it.wishlistUrl ?: "",
                productPosition = position
            )
        } ?: ProductCardOptionsModel()
    }

    fun getThreeDotsWishlistOptionsModel(): ProductCardOptionsModel {
        return components.data?.firstOrNull()?.let {
            ProductCardOptionsModel(
                hasWishlist = true,
                hasVisitShop = true,
                hasShareProduct = true,
                isWishlisted = it.isWishList,
                productId = it.productId.toString(),
                isTopAds = it.isTopads ?: false,
                topAdsWishlistUrl = it.wishlistUrl ?: "",
                productPosition = position,
                shop = ProductCardOptionsModel.Shop(
                    it.shopId ?: "",
                    it.shopName ?: "",
                    it.shopURLDesktop ?: ""
                ),
                productName = it.name ?: "",
                productImageUrl = it.imageUrlMobile ?: "",
                productUrl = it.productURLDesktop ?: "",
                formattedPrice = it.price ?: ""

            )
        } ?: ProductCardOptionsModel()
    }

    fun getTemplateType() = components.properties?.template ?: GRID
    fun getShowLoginData(): LiveData<Boolean> = showLoginLiveData
    fun notifyMeCurrentStatus(): LiveData<Boolean> = notifyMeCurrentStatus
    fun showNotifyToastMessage(): LiveData<Pair<Boolean, String?>> = showNotifyToast
    fun getScrollSimilarProductComponentID(): LiveData<String> = scrollToSimilarProductComponentID

    fun subscribeUser() {
        if (isUserLoggedIn()) {
            dataItem.value?.let { productItemData ->
                productItemData.notifyMe?.let {
                    launchCatchError(block = {
                        val campaignNotifyResponse = campaignNotifyUserCase?.subscribeToCampaignNotifyMe(getNotifyRequestBundle(productItemData))
                        campaignNotifyResponse?.checkCampaignNotifyMeResponse?.let { campaignResponse ->
                            if (campaignResponse.success == true) {
                                productItemData.notifyMe = !it
                                dataItem.value = productItemData
                                notifyMeCurrentStatus.value = productItemData.notifyMe
                                showNotifyToast.value = Pair(false, campaignResponse.message)
                                this@MasterProductCardItemViewModel.syncData.value = productCardItemUseCase?.notifyProductComponentUpdate(components.parentComponentId, components.pageEndPoint)
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
        campaignNotifyMeRequest.campaignID = dataItem.campaignId.toLongOrZero()
        campaignNotifyMeRequest.productID = dataItem.productId.toLongOrZero()
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
        val stockWordData = StockWording(title = "")
        var stockWordTitleColour = getStockColor(unifyprinciplesR.color.Unify_NN950_20)
        var stockWordTitle = ""
        var stockAvailableCount: String? = ""
        dataItem.let {
            val campaignSoldCount = it.campaignSoldCount
            val threshold: Int? = it.threshold?.toIntOrNull()
            val customStock: Int? = it.customStock?.toIntOrNull()
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
                            stockWordTitleColour = getStockColor(unifyprinciplesR.color.Unify_RN500)
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
        return stockWordData
    }

    private fun getStockColor(colorID: Int): String {
        return try {
            application.resources.getString(colorID)
        } catch (exception: Resources.NotFoundException) {
            application.resources.getString(R.string.discovery_product_stock_color)
        }
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
            return if (notifyStatus) {
                application.applicationContext
                    .resources.getString(R.string.product_card_module_label_un_subscribe)
            } else {
                application.applicationContext.resources.getString(R.string.product_card_module_label_subscribe)
            }
        }
        return ""
    }

    fun updateProductQuantity(quantity: Int) {
        components.data?.firstOrNull()?.quantity = quantity
    }

    fun getProductDataItem(): DataItem? {
        return components.data?.firstOrNull()
    }

    fun handleATCFailed() {
        components.data?.firstOrNull()?.quantity = lastQuantity
        onAttachToViewHolder()
    }

    fun getParentPositionForCarousel(): Int {
        return if (
            components.name == ComponentNames.ProductCardSprintSaleCarouselItem.componentName ||
            components.name == ComponentNames.ProductCardCarouselItem.componentName ||
            components.name == ComponentNames.ShopOfferHeroBrandProductItem.componentName
        ) {
            getComponent(components.parentComponentId, components.pageEndPoint)?.let {
                if (!it.parentSectionId.isNullOrEmpty() && it.isBackgroundPresent) {
                    it.parentComponentPosition
                } else {
                    it.position
                }
            } ?: CAROUSEL_NOT_FOUND
        } else {
            CAROUSEL_NOT_FOUND
        }
    }

    fun saveProductCardComponent() {
        components.data?.firstOrNull()?.productId?.let { prodId ->
            setComponent(prodId, components.pageEndPoint, components)
        }
    }

    fun scrollToTargetSimilarProducts() {
        components.data?.firstOrNull()?.targetComponentId?.let { targetCompId ->
            if (targetCompId.isNotEmpty()) {
                scrollToSimilarProductComponentID.value = targetCompId
            }
        }
    }

    fun getProductCardType(): String {
        return components.properties?.cardType ?: "v1"
    }
}
