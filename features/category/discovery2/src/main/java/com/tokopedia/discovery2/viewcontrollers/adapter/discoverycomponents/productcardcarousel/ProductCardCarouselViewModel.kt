package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class ProductCardCarouselViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselHeaderData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private var isLoading = false

    @Inject
    lateinit var productCardsUseCase: ProductCardsUseCase


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    init {
        initDaggerInject()
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.lihatSemua?.run {
            val lihatSemuaDataItem = DataItem(title = header, subtitle = subheader, btnApplink = applink)
            val lihatSemuaComponentData = ComponentsItem(name = ComponentsList.ProductCardCarousel.componentName, data = listOf(lihatSemuaDataItem),
                    creativeName = components.creativeName)
            productCarouselHeaderData.value = lihatSemuaComponentData
        }
        getProductList()?.let {
            productCarouselList.value = it
        }
        fetchProductCarouselData()
    }

    fun getProductCarouselItemsListData(): LiveData<ArrayList<ComponentsItem>> = productCarouselList

    private fun fetchProductCarouselData() {
        launchCatchError(block = {
            if (productCardsUseCase.loadFirstPageComponents(components.id, components.pageEndPoint, components.rpc_PinnedProduct)) {
                getProductList()?.let {
                    productCarouselList.value = addLoadMore(it)
                    syncData.value = true
                }
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    fun fetchPaginatedProducts() {
        isLoading = true
        launchCatchError(block = {
            if (productCardsUseCase.getCarouselPaginatedData(components.id, components.pageEndPoint)) {
                getProductList()?.let {
                    isLoading = false
                    productCarouselList.value = addLoadMore(it)
                    syncData.value = true
                }
            }
        }, onError = {
            getProductList()?.let {
                isLoading = false
                productCarouselList.value = it
                syncData.value = true
            }
        })
    }

    private fun addLoadMore(productDataList: ArrayList<ComponentsItem>): ArrayList<ComponentsItem> {
        val productLoadState: ArrayList<ComponentsItem> = ArrayList()
        productLoadState.addAll(productDataList)
        if (productDataList.size.isMoreThanZero() && productDataList.size.rem(components.componentsPerPage) == 0) {
            productLoadState.add(ComponentsItem(name = ComponentNames.LoadMore.componentName).apply {
                pageEndPoint = components.pageEndPoint
                parentComponentId = components.id
                id = ComponentNames.LoadMore.componentName
                loadForHorizontal = true
                discoveryPageData[this.pageEndPoint]?.componentMap?.set(this.id, this)
            })
        }
        return productLoadState
    }

    fun isUserLoggedIn() = UserSession(application).isLoggedIn

    fun isLastPage(): Boolean {
        getProductList()?.let {
            if (it.size.isMoreThanZero() && it.size.rem(components.componentsPerPage) == 0) return false
        }
        return true
    }

    fun isLoadingData() = isLoading


    private fun getProductList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { productList ->
            return productList as ArrayList<ComponentsItem>
        }
        return null
    }

    fun getProductCardHeaderData(): LiveData<ComponentsItem> = productCarouselHeaderData

}