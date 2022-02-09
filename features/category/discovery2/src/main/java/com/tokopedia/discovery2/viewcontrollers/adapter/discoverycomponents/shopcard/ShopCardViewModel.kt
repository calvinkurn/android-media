package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val SHOP_PER_PAGE = 10

class ShopCardViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val shopCardBackgroundData: MutableLiveData<ComponentsItem?> = MutableLiveData()
    private val shopCardList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val shopCardLoadError: MutableLiveData<Boolean> = MutableLiveData()
    private var isLoading = false

    @Inject
    lateinit var shopCardUseCase: ShopCardUseCase

    fun getShopCardItemsListData(): LiveData<ArrayList<ComponentsItem>> = shopCardList
    fun getShopCardBackgroundData(): LiveData<ComponentsItem?> = shopCardBackgroundData

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

    private fun fetchShopCardData() {
        launchCatchError(block = {
            shopCardUseCase.loadFirstPageComponents(components.id, components.pageEndPoint, SHOP_PER_PAGE)
            setProductsList()
        }, onError = {
            components.noOfPagesLoaded = 1
            components.verticalProductFailState = true
            shopCardLoadError.value = true
        })
    }

    private fun handleShopCardBackground() {
        shopCardBackgroundData.value = components
    }

    fun resetComponent() {
        components.noOfPagesLoaded = 0
        components.pageLoadedCounter = 1
    }

    private fun setProductsList() {
        getProductList()?.let {
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
            if (shopCardUseCase.getShopCardPaginatedData(components.id, components.pageEndPoint, SHOP_PER_PAGE)) {
                getProductList()?.let {
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
        getProductList()?.let {
            isLoading = false
            shopCardList.value = addErrorReLoadView(it)
            syncData.value = true
        }
    }

    private fun addLoadMore(shopCardDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val shopCardDataLoadState: ArrayList<ComponentsItem> = ArrayList()
        shopCardDataLoadState.addAll(shopCardDataList)
        if (Utils.nextPageAvailable(components, SHOP_PER_PAGE)) {
            shopCardDataLoadState.add(ComponentsItem(name = ComponentNames.LoadMore.componentName).apply {
                pageEndPoint = components.pageEndPoint
                parentComponentId = components.id
                id = ComponentNames.LoadMore.componentName
                loadForHorizontal = true
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            })
        }
        return shopCardDataLoadState
    }

    private fun addErrorReLoadView(shopCardDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val shopCardDataLoadState: ArrayList<ComponentsItem> = ArrayList()
        shopCardDataLoadState.addAll(shopCardDataList)
        shopCardDataLoadState.add(ComponentsItem(name = ComponentNames.CarouselErrorLoad.componentName).apply {
            pageEndPoint = components.pageEndPoint
            parentComponentId = components.id
            id = ComponentNames.CarouselErrorLoad.componentName
            parentComponentPosition = components.position
            discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
        })
        return shopCardDataLoadState
    }

    fun isUserLoggedIn() = UserSession(application)

    fun isLastPage(): Boolean {
        return !Utils.nextPageAvailable(components, SHOP_PER_PAGE)
    }

    fun isLoadingData() = isLoading

    fun getProductList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { productList ->
            return productList as ArrayList<ComponentsItem>
        }
        return null
    }

    fun getPageSize() = SHOP_PER_PAGE

    override fun refreshProductCarouselError() {
        getProductList()?.let {
            isLoading = false
            shopCardList.value = it
            syncData.value = true
        }
    }
}