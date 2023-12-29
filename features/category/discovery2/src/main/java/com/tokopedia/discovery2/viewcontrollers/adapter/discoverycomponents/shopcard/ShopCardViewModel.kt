package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.areFiltersApplied
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.ErrorState
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val SHOP_PER_PAGE = 10

class ShopCardViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val shopCardBackgroundData: MutableLiveData<ComponentsItem?> = MutableLiveData()
    private val shopCardList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val _hideShimmer = SingleLiveEvent<Boolean>()
    private val shopCardLoadError: MutableLiveData<Boolean> = MutableLiveData()
    private var isLoading = false

    @JvmField
    @Inject
    var shopCardUseCase: ShopCardUseCase? = null

    fun getShopCardItemsListData(): LiveData<ArrayList<ComponentsItem>> = shopCardList
    fun getShopCardBackgroundData(): LiveData<ComponentsItem?> = shopCardBackgroundData
    fun getShopLoadState(): LiveData<Boolean> = shopCardLoadError
    fun hideShimmer(): LiveData<Boolean> = _hideShimmer

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        handleShopCardBackground()
        handleErrorState()
        fetchShopCardData()
    }

    private fun handleErrorState() {
        if (components.verticalProductFailState) {
            shopCardLoadError.value = true
        }
    }

    fun fetchShopCardData() {
        launchCatchError(block = {
            val shouldSync = shopCardUseCase?.loadFirstPageComponents(
                components.id,
                components.pageEndPoint,
                SHOP_PER_PAGE
            )
            if (shouldSync == true) {
                getComponent(components.id, components.pageEndPoint)?.let {
                    if (it.getComponentsItem().isNullOrEmpty() && !it.areFiltersApplied()) {
                        it.verticalProductFailState = true
                        it.errorState = ErrorState.EmptyComponentState
                        components.shouldRefreshComponent = null
                    } else {
                        it.verticalProductFailState = false
                    }
                }
            }
            setShopList()
        }, onError = {
                components.noOfPagesLoaded = 1
                components.verticalProductFailState = true
                components.shouldRefreshComponent = null
                shopCardLoadError.value = true
                _hideShimmer.value = true
            })
    }

    private fun handleShopCardBackground() {
        shopCardBackgroundData.value = components
    }

    fun shouldShowShimmer(): Boolean {
        return components.properties?.dynamic == true && components.noOfPagesLoaded != 1 && !components.verticalProductFailState
    }

    fun resetComponent() {
        components.noOfPagesLoaded = 0
        components.pageLoadedCounter = 1
    }

    private fun setShopList() {
        getShopList()?.let {
            if (it.isNotEmpty()) {
                shopCardLoadError.value = false
                shopCardList.value = addLoadMore(it)
                syncData.value = true
            } else {
                shopCardLoadError.value = true
            }
        }
    }

    fun fetchShopCardPaginatedData() {
        isLoading = true
        launchCatchError(block = {
            if (shopCardUseCase?.getShopCardPaginatedData(components.id, components.pageEndPoint, SHOP_PER_PAGE) == true) {
                getShopList()?.let {
                    isLoading = false
                    shopCardList.value = addLoadMore(it)
                    syncData.value = true
                }
            } else {
                paginatedErrorData()
            }
        }, onError = {
                paginatedErrorData()
            })
    }

    private fun paginatedErrorData() {
        components.horizontalProductFailState = true
        getShopList()?.let {
            isLoading = false
            shopCardList.value = addErrorReLoadView(it)
            syncData.value = true
        }
    }

    private fun addLoadMore(shopCardDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val shopCardDataLoadState: ArrayList<ComponentsItem> = ArrayList()
        shopCardDataLoadState.addAll(shopCardDataList)
        if (Utils.nextPageAvailable(components, SHOP_PER_PAGE)) {
            shopCardDataLoadState.add(
                ComponentsItem(name = ComponentNames.LoadMore.componentName).apply {
                    pageEndPoint = components.pageEndPoint
                    parentComponentId = components.id
                    id = ComponentNames.LoadMore.componentName
                    loadForHorizontal = true
                    discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
                }
            )
        }
        return shopCardDataLoadState
    }

    private fun addErrorReLoadView(shopCardDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val shopCardDataLoadState: ArrayList<ComponentsItem> = ArrayList()
        shopCardDataLoadState.addAll(shopCardDataList)
        shopCardDataLoadState.add(
            ComponentsItem(name = ComponentNames.CarouselErrorLoad.componentName).apply {
                pageEndPoint = components.pageEndPoint
                parentComponentId = components.id
                parentComponentName = components.name
                id = ComponentNames.CarouselErrorLoad.componentName
                parentComponentPosition = components.position
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            }
        )
        return shopCardDataLoadState
    }

    fun isLastPage(): Boolean {
        return !Utils.nextPageAvailable(components, SHOP_PER_PAGE)
    }

    fun isLoadingData() = isLoading

    fun getShopList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { shopList ->
            return shopList as ArrayList<ComponentsItem>
        }
        return null
    }

    override fun refreshProductCarouselError() {
        getShopList()?.let {
            isLoading = false
            shopCardList.value = it
            syncData.value = true
        }
    }
}
