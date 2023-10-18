package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselerrorload

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.productbundlingusecase.ProductBundlingUseCase
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CarouselErrorLoadViewModel(
    application: Application,
    private val components: ComponentsItem,
    val position: Int
) :
    DiscoveryBaseViewModel(), CoroutineScope {

    private val showLoader: MutableLiveData<Boolean> = MutableLiveData()

    @JvmField
    @Inject
    var productCardUseCase: ProductCardsUseCase? = null

    @JvmField
    @Inject
    var shopCardUseCase: ShopCardUseCase? = null

    @JvmField
    @Inject
    var merchantVoucherUseCase: MerchantVoucherUseCase? = null

    @JvmField
    @Inject
    var productBundlingUseCase: ProductBundlingUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getParentComponentPosition() = Utils.getParentPosition(components)
    fun getShowLoaderStatus(): LiveData<Boolean> = showLoader

    fun loadData() {
        showLoader.value = true
        launchCatchError(block = {
            components.let {
                syncData.value = when (components.parentComponentName) {
                    ComponentNames.ShopCardView.componentName ->
                        shopCardUseCase?.getShopCardPaginatedData(
                            components.parentComponentId,
                            components.pageEndPoint
                        )
                    ComponentNames.MerchantVoucherCarousel.componentName ->
                        merchantVoucherUseCase?.getCarouselPaginatedData(
                            components.parentComponentId,
                            components.pageEndPoint
                        )
                    ComponentNames.ProductBundling.componentName ->
                        productBundlingUseCase?.getProductBundlingPaginatedData(
                            components.parentComponentId,
                            components.pageEndPoint
                        )

                    else -> productCardUseCase?.getCarouselPaginatedData(
                        components.parentComponentId,
                        components.pageEndPoint
                    )
                }
            }
        }, onError = {
                showLoader.value = false
            })
    }
}
