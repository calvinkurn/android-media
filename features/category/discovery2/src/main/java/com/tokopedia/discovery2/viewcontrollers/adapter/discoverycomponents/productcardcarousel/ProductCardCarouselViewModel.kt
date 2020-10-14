package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class ProductCardCarouselViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselHeaderData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val maxHeightProductCard: MutableLiveData<Int> = MutableLiveData()

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
        fetchProductCarouselData()
    }

    fun getProductCarouselItemsListData(): LiveData<ArrayList<ComponentsItem>> = productCarouselList

    fun getProductCardMaxHeight(): LiveData<Int> = maxHeightProductCard

    private fun fetchProductCarouselData() {
        launchCatchError(block = {
            if (productCardsUseCase.loadFirstPageComponents(components.id, components.pageEndPoint, components.rpc_discoQuery)) {
                setData()
            } else {
                setData()
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private suspend fun setData() {
        getProductList()?.let {
            if (components.name == ComponentsList.ProductCardCarousel.componentName) {
                getMaxHeightProductCard(it)
            }
            productCarouselList.value = it
            syncData.value = true
        }
    }

    private suspend fun getMaxHeightProductCard(list: java.util.ArrayList<ComponentsItem>) {
        val productCardModelArray = ArrayList<ProductCardModel>()
        list.forEach {
            it.data?.firstOrNull()?.let { dataItem ->
                productCardModelArray.add(DiscoveryDataMapper().mapDataItemToProductCardModel(dataItem))
            }
        }
        val productImageWidth = application.applicationContext.resources.getDimensionPixelSize(R.dimen.disco_product_card_width)
        maxHeightProductCard.value = productCardModelArray.getMaxHeightForGridView(application.applicationContext, Dispatchers.Default, productImageWidth)
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