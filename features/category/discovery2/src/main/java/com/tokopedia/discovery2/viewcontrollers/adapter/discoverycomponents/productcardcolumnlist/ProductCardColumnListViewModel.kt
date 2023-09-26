package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase.Companion.NO_PRODUCT_PER_PAGE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist.ProductCardColumnListMapper.mapToCarouselPagingGroupProductModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductCardColumnListViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val _carouselPagingGroupProductModel: MutableLiveData<CarouselPagingGroupProductModel> = MutableLiveData()
    private val _errorState: MutableLiveData<Unit> = MutableLiveData()

    val carouselPagingGroupProductModel: LiveData<CarouselPagingGroupProductModel>
        get() = _carouselPagingGroupProductModel
    val errorState: LiveData<Unit>
        get() = _errorState

    @JvmField
    @Inject
    var productCardsUseCase: ProductCardsUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchProductCarouselData()
    }

    fun fetchProductCarouselData() {
        launchCatchError(block = {
            productCardsUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint, NO_PRODUCT_PER_PAGE)
            setProductList()
        }, onError = {
            _errorState.value = Unit
        })
    }

    private fun setProductList() {
        val productList = getProductList()
        if (productList.isNotEmpty()) {
            _carouselPagingGroupProductModel.value = productList.mapToCarouselPagingGroupProductModel()
        } else {
            _errorState.value = Unit
        }
    }

    private fun getProductList(): List<ComponentsItem> {
        return components.getComponentsItem().orEmpty()
    }

    fun getProduct(position: Int): DataItem?  {
        return getProductList().getOrNull(position)?.data?.firstOrNull()
    }

    fun getPropertyRows(): Int {
        return components.properties?.rows.toIntSafely()
    }
}
