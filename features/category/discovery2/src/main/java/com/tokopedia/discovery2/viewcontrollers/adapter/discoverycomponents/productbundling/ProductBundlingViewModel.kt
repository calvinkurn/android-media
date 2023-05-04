package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.productbundlingusecase.ProductBundlingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val PRODUCT_PER_PAGE = 10

class ProductBundlingViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val bundledProductData: MutableLiveData<ArrayList<BundleUiModel>> = MutableLiveData()
    private val paginationHandlingList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val _emptyBundleData: MutableLiveData<Boolean> = MutableLiveData()
    private val _showErrorState = SingleLiveEvent<Boolean>()
    var lastSentPosition: Int = 0
    var hasScrolled = false
    private var isLoading = false

    @JvmField
    @Inject
    var productBundlingUseCase: ProductBundlingUseCase? = null

    @JvmField
    @Inject
    var coroutineDispatchers: CoroutineDispatchers? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getBundledProductDataList(): LiveData<ArrayList<BundleUiModel>> = bundledProductData
    fun paginationListData(): LiveData<ArrayList<ComponentsItem>> = paginationHandlingList
    fun getEmptyBundleData(): LiveData<Boolean> = _emptyBundleData
    fun showErrorState(): LiveData<Boolean> = _showErrorState

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchProductBundlingData()
    }

    fun fetchProductBundlingData() {
        launchCatchError(block = {
            productBundlingUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint)
            if (!components.data.isNullOrEmpty()) {
                mapListToBundleProductList()
                _emptyBundleData.value = false
            } else {
                _emptyBundleData.value = true
            }
        }, onError = {
            _showErrorState.value = true
        })
    }

    fun fetchProductBundlingPaginatedData() {
        isLoading = true
        launchCatchError(block = {
            if (productBundlingUseCase?.getProductBundlingPaginatedData(components.id, components.pageEndPoint, PRODUCT_PER_PAGE) == true) {
                if (!components.data.isNullOrEmpty()) {
                    isLoading = false
                    mapListToBundleProductList()
                    setShopList()
                    _emptyBundleData.value = false
                }
            } else {
                paginationHandlingList.value = arrayListOf()
            }
        }, onError = {
            paginatedErrorData()
        })
    }

    private fun setShopList() {
        if (!components.data.isNullOrEmpty()) {
            _showErrorState.value = false
            paginationHandlingList.value = addLoadMore()
        } else {
            _showErrorState.value = true
        }
    }

    private fun paginatedErrorData() {
        components.horizontalProductFailState = true
        if (!components.data.isNullOrEmpty()) {
            isLoading = false
            paginationHandlingList.value = addErrorReLoadView()
        }
    }

    private fun addLoadMore(): ArrayList<ComponentsItem> {
        val productBundlingDataLoadState: ArrayList<ComponentsItem> = ArrayList()
        if (Utils.nextPageAvailable(components, PRODUCT_PER_PAGE)) {
            productBundlingDataLoadState.add(
                ComponentsItem(name = ComponentNames.LoadMore.componentName).apply {
                    pageEndPoint = components.pageEndPoint
                    parentComponentId = components.id
                    id = ComponentNames.LoadMore.componentName
                    loadForHorizontal = true
                    discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
                }
            )
        }
        return productBundlingDataLoadState
    }

    private fun addErrorReLoadView(): ArrayList<ComponentsItem> {
        val productBundlingDataLoadState: ArrayList<ComponentsItem> = ArrayList()
        productBundlingDataLoadState.add(
            ComponentsItem(name = ComponentNames.CarouselErrorLoad.componentName).apply {
                pageEndPoint = components.pageEndPoint
                parentComponentId = components.id
                parentComponentName = components.name
                id = ComponentNames.CarouselErrorLoad.componentName
                parentComponentPosition = components.position
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            }
        )
        return productBundlingDataLoadState
    }

    fun isLoadingData() = isLoading

    fun isLastPage(): Boolean {
        return !Utils.nextPageAvailable(components, PRODUCT_PER_PAGE)
    }

    fun resetComponent() {
        components.noOfPagesLoaded = 0
        components.pageLoadedCounter = 1
    }

    override fun refreshProductCarouselError() {
        if (!components.data.isNullOrEmpty()) {
            isLoading = false
            mapBundleList()
            paginationHandlingList.value = arrayListOf()
        }
    }

    private fun mapBundleList() {
        launchCatchError(block = {
            if (!components.data.isNullOrEmpty()) {
                mapListToBundleProductList()
                _emptyBundleData.value = false
            }
        }, onError = {
            _showErrorState.value = true
        })
    }

    private suspend fun mapListToBundleProductList() {
        bundledProductData.value = components.data?.let {
            val dispatcher = coroutineDispatchers?.default ?: Dispatchers.Default
            withContext(dispatcher) {
                DiscoveryDataMapper().mapListToBundleProductList(it)
            }
        }
    }
}
