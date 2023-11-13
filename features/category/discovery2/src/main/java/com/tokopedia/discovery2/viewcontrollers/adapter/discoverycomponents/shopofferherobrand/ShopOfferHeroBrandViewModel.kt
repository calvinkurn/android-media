package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
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
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class ShopOfferHeroBrandViewModel(
    val application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {
    companion object {
        private const val PRODUCT_IMAGE_WIDTH = 165
        private const val RESET_HEIGHT = 0

        const val ERROR_MESSAGE_EMPTY_DATA = "empty data"
        const val PRODUCT_PER_PAGE = 10
    }

    private val _header: MutableLiveData<Properties.Header?> = MutableLiveData()
    private val _productList: MutableLiveData<Result<ArrayList<ComponentsItem>>> = MutableLiveData()
    private val _productMaxHeight: MutableLiveData<Int> = MutableLiveData()
    private val _tierChange: MutableLiveData<TierData> = MutableLiveData()

    private var isLoading = false

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
        handleHeader()
        loadFirstPageProductCarousel()
    }

    private suspend fun setProductsList() {
        getProductList()?.let {
            if (it.isNotEmpty()) {
                getMaxHeightProductCard(it)
                _productList.value = Success(addLoadMore(it))
                syncData.value = true
            } else {
                _productMaxHeight.value = RESET_HEIGHT
                _productList.value = Fail(Throwable(ERROR_MESSAGE_EMPTY_DATA))
            }
        }
    }

    private fun handleHeader() {
        _header.value = component.getPropertyHeader()
    }

    fun loadFirstPageProductCarousel() {
        launchCatchError(
            block = {
                productCardsUseCase?.loadFirstPageComponents(component.id, component.pageEndPoint, PRODUCT_PER_PAGE)
                component.shouldRefreshComponent = null
                setProductsList()
            }, onError = { throwable ->
                component.noOfPagesLoaded = 1
                component.verticalProductFailState = true
                component.shouldRefreshComponent = null
                _productList.value = Fail(throwable)
            }
        )
    }

    fun resetComponent() {
        component.noOfPagesLoaded = 0
        component.pageLoadedCounter = 1
    }

    private suspend fun getMaxHeightProductCard(productList: List<ComponentsItem>) {
        val productCardModels = ArrayList<ProductCardModel>()
        productList.forEach { item ->
            item.data?.firstOrNull()?.let { dataItem ->
                dataItem.hasNotifyMe = dataItem.notifyMe != null
                productCardModels.add(DiscoveryDataMapper().mapDataItemToProductCardModel(dataItem, component.name))
            }
        }
        _productMaxHeight.value = productCardModels.getMaxHeightForGridView(
            context = application.applicationContext,
            coroutineDispatcher = Dispatchers.Default,
            productImageWidth = PRODUCT_IMAGE_WIDTH.toPx()
        )
    }

    fun fetchCarouselPaginatedProducts() {
        isLoading = true
        launchCatchError(block = {
            if (productCardsUseCase?.getCarouselPaginatedData(component.id, component.pageEndPoint, PRODUCT_PER_PAGE) == true) {
                getProductList()?.let {
                    isLoading = false
                    getMaxHeightProductCard(it)
                    _productList.value = Success(addLoadMore(it))
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
        component.horizontalProductFailState = true
        isLoading = false
        getProductList()?.let {
            getMaxHeightProductCard(it)
            _productList.value = Success(addErrorReLoadView(it))
            syncData.value = true
        }
    }

    private fun addLoadMore(productDataList: List<ComponentsItem>): ArrayList<ComponentsItem> {
        val productLoadState: ArrayList<ComponentsItem> = ArrayList()
        productLoadState.addAll(productDataList)

        val nextPageComponent = if (Utils.nextPageAvailable(component, PRODUCT_PER_PAGE)) {
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
            pageEndPoint = component.pageEndPoint
            parentComponentId = component.id
            id = ComponentNames.LoadMore.componentName
            loadForHorizontal = true
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
        }

    private fun constructViewAllCard() =
        ComponentsItem(name = ComponentNames.ViewAllCardCarousel.componentName).apply {
            id = ComponentNames.ViewAllCardCarousel.componentName
            val element = with(component.compAdditionalInfo?.redirection!!) {
                DataItem(
                    title = this.bodyText,
                    action = this.ctaText,
                    applinks = this.applink
                )
            }

            data = listOf(element)
        }

    private fun addErrorReLoadView(productDataList: List<ComponentsItem>): ArrayList<ComponentsItem> {
        val productLoadState: ArrayList<ComponentsItem> = ArrayList()
        productLoadState.addAll(productDataList)
        productLoadState.add(
            ComponentsItem(name = ComponentNames.CarouselErrorLoad.componentName).apply {
                pageEndPoint = component.pageEndPoint
                parentComponentId = component.id
                id = ComponentNames.CarouselErrorLoad.componentName
                parentComponentPosition = component.position
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            }
        )
        return productLoadState
    }

    fun hasNextPage(): Boolean = Utils.nextPageAvailable(component, PRODUCT_PER_PAGE)

    fun shouldShowViewAllCard(): Boolean {
        return !component.compAdditionalInfo?.redirection?.applink.isNullOrEmpty()
    }

    fun isLoadingData() = isLoading

    fun getProductList(): List<ComponentsItem>? = component.getComponentsItem()

    fun getPageSize() = PRODUCT_PER_PAGE

    override fun refreshProductCarouselError() {
        getProductList()?.let {
            isLoading = false
            syncData.value = true
        }
    }

    fun areFiltersApplied(): Boolean {
        return (
            (component.selectedSort != null && component.selectedFilters != null) &&
                (
                    component.selectedSort?.isNotEmpty() == true ||
                        component.selectedFilters?.isNotEmpty() == true
                    )
            )
    }

    fun getErrorStateComponent(): ComponentsItem {
        return ComponentsItem(name = ComponentNames.ProductListEmptyState.componentName).apply {
            pageEndPoint = component.pageEndPoint
            parentComponentId = component.id
            id = ComponentNames.ProductListEmptyState.componentName
        }
    }

    fun changeTier(
        isShimmerShown: Boolean,
        tierWording: String = ""
    ) {
        if (component.getPropertyHeader() == null) return

        _tierChange.value = TierData(
            isProgressBarShown = isShimmerShown || tierWording.isNotBlank(),
            isShimmerShown = isShimmerShown,
            tierWording = tierWording
        )
    }
}
