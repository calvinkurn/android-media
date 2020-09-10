package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class ProductCardCarouselViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselHeaderData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

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
            val lihatSemuaDataItem = DataItem(title = header, subtitle = subheader, btnApplink = applink,
                    creativeName = components.creativeName)
            val lihatSemuaComponentData = ComponentsItem(name = ComponentsList.ProductCardCarousel.componentName, data = listOf(lihatSemuaDataItem))
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
                    productCarouselList.value = it
                    syncData.value = true
                }
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }


    private fun getProductList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { productList ->
            return productList as ArrayList<ComponentsItem>
        }
        return null
    }

    fun getProductCardHeaderData(): LiveData<ComponentsItem> = productCarouselHeaderData

}