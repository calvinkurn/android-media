package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.supportingbrand.SupportingBrandUseCase
import com.tokopedia.discovery2.usecase.supportingbrand.SupportingBrandUseCase.Companion.BRAND_PER_PAGE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand.ShopOfferSupportingBrandComponentExtension.addLoadMore
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand.ShopOfferSupportingBrandComponentExtension.addReload
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ShopOfferSupportingBrandViewModel(
    application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val layout: ArrayList<ComponentsItem> = arrayListOf()

    private val _brands: MutableLiveData<Result<ArrayList<ComponentsItem>>> = MutableLiveData()

    val brands: LiveData<Result<ArrayList<ComponentsItem>>>
        get() = _brands

    @JvmField
    @Inject
    var useCase: SupportingBrandUseCase? = null

    init {
        layout.addShimmer()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + SupervisorJob()

    var isLoading = false
        private set

    fun hasNextPage(): Boolean = Utils.nextPageAvailable(
        component,
        BRAND_PER_PAGE
    )

    fun hasHeader(): Boolean = component.getPropertyHeader() != null

    fun getProductList(): List<ComponentsItem>? = component.getComponentsItem()

    fun loadPageBrand() {
        launchCatchError(
            block = {
                useCase?.loadPageComponents(
                    componentId = component.id,
                    pageEndPoint = component.pageEndPoint
                )
                component.shouldRefreshComponent = null
                setSupportingBrandList {
                    _brands.value = Fail(Throwable(EMPTY_DATA_MESSAGE))
                }
            },
            onError = {
                component.noOfPagesLoaded = 1
                component.verticalProductFailState = true
                component.shouldRefreshComponent = null
                Timber.e(it)
                _brands.value = Fail(it)
                isLoading = false
            }
        )
    }

    fun loadMore() {
        isLoading = true
        launchCatchError(block = {
            when {
                useCase?.loadPageComponents(component.id, component.pageEndPoint) == true -> {
                    setSupportingBrandList {}
                }

                hasNextPage().not() -> {
                    setSupportingBrandList {}
                }

                else -> {
                    handleErrorPagination()
                }
            }
        }, onError = {
            handleErrorPagination()
        })
    }

    fun resetComponent() {
        component.apply {
            noOfPagesLoaded = 0
            pageLoadedCounter = 1
        }
    }

    private fun setSupportingBrandList(
        onEmptyListener: () -> Unit
    ) {
        isLoading = false
        val productList = getProductList()

        if (!productList.isNullOrEmpty()) {
            _brands.value = Success(ArrayList(addLoadMore(productList)))
        } else {
            onEmptyListener.invoke()
        }
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

    private fun handleErrorPagination() {
        isLoading = false
        component.horizontalProductFailState = true

        val productList = getProductList()
        if (!productList.isNullOrEmpty()) {
            _brands.value = Success(ArrayList(addReload(productList)))
        }
    }

    private fun ArrayList<ComponentsItem>.addShimmer() {
        val shimmerComponent = ComponentsItem(
            name = ComponentNames.BannerCarouselShimmer.componentName
        )

        add(shimmerComponent)
        add(shimmerComponent)
        add(shimmerComponent)
        add(shimmerComponent)
    }

    private fun ArrayList<ComponentsItem>.addBrandList(brandList: List<ComponentsItem>) {
        clear()
        addAll(brandList)
    }

    companion object {
        private const val EMPTY_DATA_MESSAGE = "Empty Data"
    }
}
