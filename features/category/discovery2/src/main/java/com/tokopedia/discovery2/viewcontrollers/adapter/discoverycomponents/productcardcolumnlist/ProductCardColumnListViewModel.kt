package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase.Companion.NO_PRODUCT_PER_PAGE
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist.ProductCardColumnListMapper.mapToCarouselPagingGroupProductModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductCardColumnListViewModel(
    val application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components), CoroutineScope {

    private val _carouselPagingGroupProductModel: MutableLiveData<CarouselPagingGroupProductModel> =
        MutableLiveData()
    private val _errorState: MutableLiveData<Unit> = MutableLiveData()

    val carouselPagingGroupProductModel: LiveData<CarouselPagingGroupProductModel>
        get() = _carouselPagingGroupProductModel
    val errorState: LiveData<Unit>
        get() = _errorState

    @JvmField
    @Inject
    var productCardsUseCase: ProductCardsUseCase? = null

    @JvmField
    @Inject
    var topAdsTrackingUseCase: TopAdsTrackingUseCase? = null

    @JvmField
    @Inject
    var userSession: UserSessionInterface? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchProductCarouselData()
    }

    private fun fetchProductCarouselData() {
        launchCatchError(block = {
            productCardsUseCase?.loadFirstPageComponents(
                components.id,
                components.pageEndPoint,
                NO_PRODUCT_PER_PAGE
            )
            setProductList()
        }, onError = {
                _errorState.postValue(Unit)
            })
    }

    private fun setProductList() {
        val componentItems = components.getComponentsItem()
        if (!componentItems.isNullOrEmpty()) {
            _carouselPagingGroupProductModel.postValue(componentItems.mapToCarouselPagingGroupProductModel())
        } else {
            _errorState.postValue(Unit)
        }
    }

    fun getProduct(position: Int): DataItem? =
        components.getComponentItem(position)?.data?.firstOrNull()

    fun getItemPerPage(): Int =
        if (components.getComponentsItemSize() < components.getPropertyRows()) components.getComponentsItemSize() else components.getPropertyRows()

    fun isLoggedIn(): Boolean = userSession?.isLoggedIn.orFalse()

    fun trackTopAdsImpression(position: Int) {
        val dataItem = getProduct(position) ?: return

        with(dataItem) {
            if (isTopads == false || components.topAdsTrackingStatus) return@with

            topadsViewUrl?.let {
                topAdsTrackingUseCase?.hitImpressions(
                    this@ProductCardColumnListViewModel::class.qualifiedName,
                    it,
                    productId.orEmpty(),
                    name.orEmpty(),
                    imageUrlMobile.orEmpty()
                )
                components.topAdsTrackingStatus = true
            }
        }
    }

    fun trackTopAdsClick(position: Int) {
        val dataItem = getProduct(position) ?: return

        with(dataItem) {
            if (isTopads == false) return@with

            topadsClickUrl?.let {
                topAdsTrackingUseCase?.hitClick(
                    this@ProductCardColumnListViewModel::class.qualifiedName,
                    it,
                    productId.orEmpty(),
                    name.orEmpty(),
                    imageUrlMobile.orEmpty()
                )
            }
        }
    }
}
