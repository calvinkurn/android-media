package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val PRODUCT_PER_PAGE = 10

class CategoryBestSellerViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val productLoadError: MutableLiveData<Boolean> = MutableLiveData()
    private val maxHeightProductCard: MutableLiveData<Int> = MutableLiveData()

    @Inject
    lateinit var productCardsUseCase: ProductCardsUseCase

    fun getProductCarouselItemsListData(): LiveData<ArrayList<ComponentsItem>> = productCarouselList
    fun getProductCardMaxHeight(): LiveData<Int> = maxHeightProductCard
    fun getProductLoadState(): LiveData<Boolean> = productLoadError

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchProductCarouselData()
    }

    private fun fetchProductCarouselData() {
        launchCatchError(block = {
            productCardsUseCase.loadFirstPageComponents(components.id, components.pageEndPoint, PRODUCT_PER_PAGE)
            setProductsList()
        }, onError = {
            productLoadError.value = true
            it.printStackTrace()
        })
    }

    private suspend fun setProductsList() {
        getProductList()?.let {
            if (it.isNotEmpty()) {
                productLoadError.value = false
                reSyncProductCardHeight(it)
                productCarouselList.value = it
                syncData.value = true
            } else {
                productLoadError.value = true
            }
        }
    }

    private suspend fun reSyncProductCardHeight(list: java.util.ArrayList<ComponentsItem>) {
        getMaxHeightProductCard(list)
    }

    private suspend fun getMaxHeightProductCard(list: java.util.ArrayList<ComponentsItem>) {
        val productCardModelArray = ArrayList<ProductCardModel>()
        list.forEach {
            it.data?.firstOrNull()?.let { dataItem ->
                dataItem.hasNotifyMe = (dataItem.notifyMe != null)
                productCardModelArray.add(DiscoveryDataMapper().mapDataItemToProductCardModel(dataItem, components.name))
            }
        }
        val productImageWidth = application.applicationContext.resources.getDimensionPixelSize(R.dimen.disco_product_card_width)
        maxHeightProductCard.value = productCardModelArray.getMaxHeightForGridView(application.applicationContext, Dispatchers.Default, productImageWidth)
    }

    private fun getProductList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { productList ->
            return productList as ArrayList<ComponentsItem>
        }
        return null
    }

}