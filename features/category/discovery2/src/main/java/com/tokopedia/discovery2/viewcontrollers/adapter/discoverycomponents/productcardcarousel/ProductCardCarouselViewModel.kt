package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID
import com.tokopedia.discovery2.Constant.ProductTemplate.LIST
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.utils.getMaxHeightForListView
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.Date
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

const val PRODUCT_PER_PAGE = 10
private const val RESET_HEIGHT = 0

class ProductCardCarouselViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselHeaderData: MutableLiveData<ComponentsItem?> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val maxHeightProductCard: MutableLiveData<Int> = MutableLiveData()
    private val productLoadError: MutableLiveData<Boolean> = MutableLiveData()
    private val mixLeftData: MutableLiveData<MixLeft> = MutableLiveData()
    private val saleEndDate: MutableLiveData<Date> = MutableLiveData()
    private val _atcFailed = SingleLiveEvent<Int>()
    private var isLoading = false
    private var isReimagine = !components.properties?.cardType.equals("V1", true)
    private val mixLeftComponentsItem: ComponentsItem by lazy { ComponentsItem() }

    @JvmField
    @Inject
    var productCardsUseCase: ProductCardsUseCase? = null

    fun getProductCarouselItemsListData(): LiveData<ArrayList<ComponentsItem>> = productCarouselList
    fun getProductCardMaxHeight(): LiveData<Int> = maxHeightProductCard
    fun getProductCardHeaderData(): LiveData<ComponentsItem?> = productCarouselHeaderData
    fun getProductLoadState(): LiveData<Boolean> = productLoadError
    fun getMixLeftData(): LiveData<MixLeft> = mixLeftData
    val atcFailed: LiveData<Int> = _atcFailed

    fun getSaleEndDate(): LiveData<Date> = saleEndDate

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
        handleLihatSemuaHeader()
        handleMixLeftData()
        handleErrorState()
        fetchProductCarouselData()
    }

    private fun handleMixLeftData() {
        mixLeftData.value = components.properties?.mixLeft
        if (isMixLeftBannerPresent()) {
            setupMixLeftComponent()
        }
    }

    private fun handleErrorState() {
        if (components.verticalProductFailState) {
            productLoadError.value = true
        }
    }

    private fun handleLihatSemuaHeader() {
        var lihatSemuaComponentData: ComponentsItem? = null
        components.lihatSemua?.let {
//            we don't add header component in case after when query is hit but no list of products were found.
            if (!(components.noOfPagesLoaded == 1 && components.getComponentsItem()
                    .isNullOrEmpty())
            ) {
                it.run {
                    val lihatSemuaDataItem = DataItem(
                        title = header,
                        subtitle = subheader,
                        btnApplink = applink
                    )
                    lihatSemuaComponentData = ComponentsItem(
                        name = ComponentsList.ProductCardCarousel.componentName,
                        data = listOf(lihatSemuaDataItem),
                        creativeName = components.creativeName
                    )
                }
            }
        }
        productCarouselHeaderData.value = lihatSemuaComponentData
    }

    fun fetchProductCarouselData() {
        launchCatchError(block = {
            productCardsUseCase?.loadFirstPageComponents(
                components.id,
                components.pageEndPoint,
                PRODUCT_PER_PAGE
            )
            components.shouldRefreshComponent = null
            setProductsList()
            setTimer()
        }, onError = {
            components.noOfPagesLoaded = 1
            components.verticalProductFailState = true
            components.shouldRefreshComponent = null
            productLoadError.value = true
        })
    }

    fun resetComponent() {
        components.noOfPagesLoaded = 0
        components.pageLoadedCounter = 1
    }

    private suspend fun setProductsList() {
        getProductList()?.let {
            if (it.isNotEmpty()) {
                productLoadError.value = false
                reSyncProductCardHeight(it)
                productCarouselList.value = addLoadMore(it)
                syncData.value = true
            } else {
                maxHeightProductCard.value = RESET_HEIGHT
                productLoadError.value = true
            }
        }
    }

    private fun setTimer() {
        val endTime = components.compAdditionalInfo?.timer?.endTime
        saleEndDate.postValue(Utils.parseData(endTime))
    }

    private suspend fun reSyncProductCardHeight(list: java.util.ArrayList<ComponentsItem>) {
        if (components.name == ComponentsList.ProductCardCarousel.componentName ||
            components.name == ComponentsList.ProductCardSprintSaleCarousel.componentName ||
            components.name == ComponentNames.ProductCardCarouselItemReimagine.componentName ||
            components.name == ComponentNames.ProductCardSprintSaleCarouselItemReimagine.componentName
        ) {
            getMaxHeightProductCard(list)
        }
    }

    private suspend fun getMaxHeightProductCard(list: java.util.ArrayList<ComponentsItem>) {
        val productCardModelArray : ArrayList<ProductCardModel> = arrayListOf()
        var templateType = GRID
        list.forEach {
            if (it.properties?.template == LIST) {
                templateType = LIST
            }
            it.data?.firstOrNull()?.let { dataItem ->
                dataItem.hasNotifyMe = (dataItem.notifyMe != null)
                productCardModelArray.add(
                    DiscoveryDataMapper().mapDataItemToProductCardModel(
                        dataItem,
                        components.name
                    )
                )
            }
        }
        if (templateType == LIST) {
            maxHeightProductCard.value = productCardModelArray.getMaxHeightForListView(
                application.applicationContext,
                Dispatchers.Default,
                isReimagine,
                useCompatPadding = true
            )
        } else {
            val mixLeftPadding =
                if (isMixLeftBannerPresent()) application.applicationContext.resources.getDimensionPixelSize(
                    R.dimen.dp_10
                ) else 0
            val productImageWidth =
                if (isReimagine) application.applicationContext.resources
                    .getDimensionPixelSize(R.dimen.disco_carousel_product_card_grid_width)
                else application.applicationContext.resources.getDimensionPixelSize(
                    R.dimen.disco_product_card_width
                )

            maxHeightProductCard.value = (productCardModelArray.getMaxHeightForGridView(
                application.applicationContext,
                Dispatchers.Default,
                productImageWidth,
                isReimagine,
                useCompatPadding = true
            ) + mixLeftPadding)
        }
    }

    fun fetchCarouselPaginatedProducts() {
        isLoading = true
        launchCatchError(block = {
            if (productCardsUseCase?.getCarouselPaginatedData(
                    components.id,
                    components.pageEndPoint,
                    PRODUCT_PER_PAGE
                ) == true
            ) {
                getProductList()?.let {
                    isLoading = false
                    reSyncProductCardHeight(it)
                    productCarouselList.value = addLoadMore(it)
                    syncData.value = true
                }
            } else {
                paginatedErrorData()
            }
        }, onError = {
            paginatedErrorData()
        })
    }

    private suspend fun paginatedErrorData() {
        components.horizontalProductFailState = true
        getProductList()?.let {
            isLoading = false
            reSyncProductCardHeight(it)
            productCarouselList.value = addErrorReLoadView(it)
            syncData.value = true
        }
    }

    private fun addLoadMore(productDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val productLoadState: ArrayList<ComponentsItem> = ArrayList()
        if (isMixLeftBannerPresent()) {
            productLoadState.add(mixLeftComponentsItem)
        }
        productLoadState.addAll(productDataList)

        val nextPageComponent = if (Utils.nextPageAvailable(components, PRODUCT_PER_PAGE)) {
            constructLoadMoreComponent()
        } else if (shouldShowViewAllCard()) {
            constructViewAllCard()
        } else {
            return productLoadState
        }

        productLoadState.add(nextPageComponent)

        return productLoadState
    }

    private fun constructLoadMoreComponent() =
        ComponentsItem(name = ComponentNames.LoadMore.componentName).apply {
            pageEndPoint = components.pageEndPoint
            parentComponentId = components.id
            id = ComponentNames.LoadMore.componentName
            loadForHorizontal = true
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
        }

    private fun constructViewAllCard() =
        ComponentsItem(name = ComponentNames.ViewAllCardCarousel.componentName).apply {
            id = ComponentNames.ViewAllCardCarousel.componentName
            val element = with(components.compAdditionalInfo?.redirection!!) {
                DataItem(
                    title = this.bodyText,
                    action = this.ctaText,
                    applinks = this.applink
                )
            }

            data = listOf(element)
        }

    private fun addErrorReLoadView(productDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val productLoadState: ArrayList<ComponentsItem> = ArrayList()
        if (isMixLeftBannerPresent()) {
            productLoadState.add(mixLeftComponentsItem)
        }
        productLoadState.addAll(productDataList)
        productLoadState.add(
            ComponentsItem(name = ComponentNames.CarouselErrorLoad.componentName).apply {
                pageEndPoint = components.pageEndPoint
                parentComponentId = components.id
                id = ComponentNames.CarouselErrorLoad.componentName
                parentComponentPosition = components.position
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            }
        )
        return productLoadState
    }

    fun isUserLoggedIn() = UserSession(application).isLoggedIn

    fun isLastPage(): Boolean {
        return !Utils.nextPageAvailable(components, PRODUCT_PER_PAGE)
    }

    fun shouldShowViewAllCard(): Boolean {
        return !components.compAdditionalInfo?.redirection?.applink.isNullOrEmpty()
    }

    fun isLoadingData() = isLoading

    fun getProductList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { productList ->
            return productList as ArrayList<ComponentsItem>
        }
        return null
    }

    fun getPageSize() = PRODUCT_PER_PAGE

    override fun refreshProductCarouselError() {
        getProductList()?.let {
            isLoading = false
            productCarouselList.value = addMixLeftIfPresent(it)
            syncData.value = true
        }
    }

    private fun addMixLeftIfPresent(productList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        if (isMixLeftBannerPresent()) {
            val arrayList = ArrayList<ComponentsItem>()
            arrayList.add(mixLeftComponentsItem)
            arrayList.addAll(productList)
            return arrayList
        }
        return productList
    }

    fun areFiltersApplied(): Boolean {
        return (
            (components.selectedSort != null && components.selectedFilters != null) &&
                (
                    components.selectedSort?.isNotEmpty() == true ||
                        components.selectedFilters?.isNotEmpty() == true
                    )
            )
    }

    fun getErrorStateComponent(): ComponentsItem {
        return ComponentsItem(name = ComponentNames.ProductListEmptyState.componentName).apply {
            pageEndPoint = components.pageEndPoint
            parentComponentId = components.id
            id = ComponentNames.ProductListEmptyState.componentName
        }
    }

    fun handleAtcFailed(position: Int) {
        _atcFailed.value = position
    }

    fun containsTokoNowProducts(): Boolean {
        return (components.properties?.tokonowATCActive == true)
    }

    private fun setupMixLeftComponent() {
        mixLeftComponentsItem.position = 0
        mixLeftComponentsItem.name = ComponentNames.MixLeftEmptyItem.componentName
        mixLeftComponentsItem.properties = components.properties
        mixLeftComponentsItem.parentComponentId = components.id
        mixLeftComponentsItem.parentComponentPosition = components.position
    }

    private fun isMixLeftBannerPresent(): Boolean {
        return !components.properties?.mixLeft?.bannerImageUrlMobile.isNullOrEmpty()
    }
}
