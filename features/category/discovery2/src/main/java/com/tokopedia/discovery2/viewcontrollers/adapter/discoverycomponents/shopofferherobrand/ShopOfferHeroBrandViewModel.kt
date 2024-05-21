package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.isOldProductCardType
import com.tokopedia.discovery2.Utils.Companion.isReimagineProductCardInBackground
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardPaginationLoadState
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase.Companion.PRODUCT_PER_PAGE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.ShopOfferHeroBrandComponentExtension.addLoadMore
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.ShopOfferHeroBrandComponentExtension.addReload
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.model.BmGmTierData
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand.model.TierData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ShopOfferHeroBrandViewModel(
    val application: Application,
    component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(component), CoroutineScope {
    companion object {
        private const val PRODUCT_IMAGE_WIDTH = 165

        const val ERROR_MESSAGE_EMPTY_DATA = "empty data"
        private const val HEADER_OFFER_TYPE_PD = "PD"
    }

    private val _header: MutableLiveData<Properties.Header?> = MutableLiveData()
    private val _productList: MutableLiveData<Result<ArrayList<ComponentsItem>>> = MutableLiveData()
    private val _productMaxHeight: MutableLiveData<Int> = MutableLiveData()
    private val _tierChange: MutableLiveData<TierData> = MutableLiveData()

    var isLoading = false
        private set

    val header: LiveData<Properties.Header?>
        get() = _header
    val productList: LiveData<Result<ArrayList<ComponentsItem>>>
        get() = _productList
    val productMaxHeight: LiveData<Int>
        get() = _productMaxHeight
    val tierChange: LiveData<TierData>
        get() = _tierChange

    @JvmField
    @Inject
    var productCardsUseCase: ProductCardsUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        component.shouldRefreshComponent = null
        loadFirstPageProductCarousel()
    }

    override fun refreshProductCarouselError() {
        getProductList()?.let {
            isLoading = false
            syncData.value = true
        }
    }

    private suspend fun setProductsList(
        onEmptyListener: () -> Unit,
    ) {
        isLoading = false
        val productList = getProductList()

        if (!productList.isNullOrEmpty()) {
            setMaxHeightProductCard(productList)
            _productList.value = Success(ArrayList(addLoadMore(productList)))
        } else {
            onEmptyListener.invoke()
        }
    }

    private suspend fun setMaxHeightProductCard(productList: List<ComponentsItem>) {
        val productCardModels = ArrayList<ProductCardModel>()
        productList.forEach { item ->
            item.data?.firstOrNull()?.let { dataItem ->
                dataItem.hasNotifyMe = dataItem.notifyMe != null
                productCardModels.add(
                    DiscoveryDataMapper().mapDataItemToProductCardModel(
                        dataItem,
                        component.name,
                        component.properties.isReimagineProductCardInBackground()
                    )
                )
            }
        }
        this._productMaxHeight.value = productCardModels.getMaxHeightForGridView(
            context = application.applicationContext,
            coroutineDispatcher = Dispatchers.Default,
            productImageWidth = PRODUCT_IMAGE_WIDTH.toPx(),
            isReimagine = !component.properties.isOldProductCardType(),
            useCompatPadding = true
        )
    }

    private suspend fun handleErrorPagination() {
        isLoading = false
        component.horizontalProductFailState = true

        val productList = getProductList()
        if (!productList.isNullOrEmpty()) {
            setMaxHeightProductCard(productList)
            _productList.value = Success(ArrayList(addReload(productList)))
        }
    }

    fun getHeader() {
        _header.value = component.getComponentsItem()?.firstOrNull()?.getPropertyHeader()
    }

    fun loadFirstPageProductCarousel() {
        isLoading = true
        launchCatchError(
            block = {
                productCardsUseCase?.loadFirstPageComponents(component.id, component.pageEndPoint)
                component.shouldRefreshComponent = null
                setProductsList(
                    onEmptyListener = {
                        _productList.value = Fail(Throwable(ERROR_MESSAGE_EMPTY_DATA))
                    }
                )
            },
            onError = { throwable ->
                component.noOfPagesLoaded = 1
                component.verticalProductFailState = true
                component.shouldRefreshComponent = null
                _productList.value = Fail(throwable)
                isLoading = false
            }
        )
    }

    fun loadMore() {
        isLoading = true
        launchCatchError(block = {
            when (productCardsUseCase?.getCarouselPaginatedData(
                component.id,
                component.pageEndPoint
            )) {
                ProductCardPaginationLoadState.FAILED -> {
                    handleErrorPagination()
                }

                else -> {
                    setProductsList(
                        onEmptyListener = { /* nothing to do */ }
                    )
                }
            }
        }, onError = {
            handleErrorPagination()
        })
    }

    fun resetComponent() {
        component.noOfPagesLoaded = 0
        component.pageLoadedCounter = 1
    }

    private fun addLoadMore(productDataList: List<ComponentsItem>): ArrayList<ComponentsItem> {
        val productList: ArrayList<ComponentsItem> = ArrayList()
        productList.addAll(productDataList)

        return if (hasNextPage()) {
            productList.addLoadMore(component)
            productList
        } else {
            productList
        }
    }

    private fun addReload(productDataList: List<ComponentsItem>): ArrayList<ComponentsItem> {
        val productLoadState: ArrayList<ComponentsItem> = ArrayList()
        productLoadState.addAll(productDataList)
        productLoadState.addReload(component)
        return productLoadState
    }

    fun hasNextPage(): Boolean = Utils.nextPageAvailable(component, PRODUCT_PER_PAGE)

    fun hasHeader(): Boolean = component.getPropertyHeader() != null

    fun getProductList(): List<ComponentsItem>? = component.getComponentsItem()

    fun areFiltersApplied(): Boolean =
        ((component.selectedSort != null && component.selectedFilters != null) && (component.selectedSort?.isNotEmpty() == true || component.selectedFilters?.isNotEmpty() == true))

    fun isGwp(): Boolean = header.value?.offerType.equals(HEADER_OFFER_TYPE_PD, true).not()

    fun changeTier(
        isShimmerShown: Boolean,
        bmGmTierData: BmGmTierData? = null,
    ) {
        if (!hasHeader()) return
        _tierChange.value = TierData(
            isProgressBarShown = isShimmerShown || bmGmTierData != null,
            isShimmerShown = isShimmerShown,
            offerMessages = bmGmTierData?.offerMessages,
            flipTierWording = bmGmTierData?.flipTierWording.orEmpty(),
            flipTierImage = bmGmTierData?.flipTierImage.orEmpty()
        )
    }

    fun syncData() {
        val productList = getProductList()
        if (productList != null) {
            _productList.value = Success(ArrayList(addLoadMore(productList)))
        } else {
            _productList.value = Success(ArrayList())
        }
    }
}
