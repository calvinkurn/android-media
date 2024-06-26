package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils.Companion.isOldProductCardType
import com.tokopedia.discovery2.Utils.Companion.isReimagineProductCardInBackground
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val PRODUCT_PER_PAGE = 10

class CategoryBestSellerViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(components), CoroutineScope {

    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productLoadError: MutableLiveData<Boolean> = MutableLiveData()
    private val maxHeightProductCard: MutableLiveData<Int> = MutableLiveData()
    private val backgroundImageUrl: MutableLiveData<String> = MutableLiveData()

    @JvmField
    @Inject
    var productCardsUseCase: ProductCardsUseCase? = null

    fun getComponentData(): LiveData<ComponentsItem> = componentData
    fun getProductCarouselItemsListData(): LiveData<ArrayList<ComponentsItem>> = productCarouselList
    fun getProductCardMaxHeight(): LiveData<Int> = maxHeightProductCard
    fun getProductLoadState(): LiveData<Boolean> = productLoadError
    fun getBackgroundImage(): LiveData<String> = backgroundImageUrl

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        updateComponent()
        fetchProductCarouselData()
        setupBackground()
    }

    private fun updateComponent() {
        componentData.value = components
    }

    private fun setupBackground() {
        backgroundImageUrl.value = components.properties?.backgroundImageUrl ?: ""
    }

    private fun fetchProductCarouselData() {
        launchCatchError(block = {
            productCardsUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint, PRODUCT_PER_PAGE)
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
                productCardModelArray.add(
                    DiscoveryDataMapper().mapDataItemToProductCardModel(
                        dataItem,
                        components.name,
                        components.properties.isReimagineProductCardInBackground()
                    )
                )
            }
        }
        val productImageWidth = application.applicationContext.resources.getDimensionPixelSize(R.dimen.disco_product_card_width)
        val maxHeightForGridView = productCardModelArray.getMaxHeightForGridView(
            context = application.applicationContext,
            coroutineDispatcher = Dispatchers.Default,
            productImageWidth = productImageWidth,
            isReimagine = !components.properties.isOldProductCardType(),
            useCompatPadding = true
        )
        maxHeightProductCard.postValue(maxHeightForGridView)
    }

    private fun getProductList(): ArrayList<ComponentsItem>? {
        components.getComponentsItem()?.let { productList ->
            return productList as ArrayList<ComponentsItem>
        }
        return null
    }
}
