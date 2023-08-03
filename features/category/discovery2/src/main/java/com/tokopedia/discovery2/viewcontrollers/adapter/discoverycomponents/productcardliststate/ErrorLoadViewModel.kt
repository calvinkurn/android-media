package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.bannerinfiniteusecase.BannerInfiniteUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ErrorLoadViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) :
    DiscoveryBaseViewModel(), CoroutineScope {

    private val showLoader: MutableLiveData<Boolean> = MutableLiveData()

    @JvmField
    @Inject
    var productCardUseCase: ProductCardsUseCase? = null

    @JvmField
    @Inject
    var merchantVoucherUseCase: MerchantVoucherUseCase? = null

    @JvmField
    @Inject
    var bannerInfiniteUseCase: BannerInfiniteUseCase? = null

    @JvmField
    @Inject
    var shopCardInfiniteUseCase: ShopCardUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getShowLoaderStatus(): LiveData<Boolean> = showLoader

    fun reloadComponentData() {
        showLoader.value = true
        launchCatchError(block = {
            getComponent(components.parentComponentId, components.pageEndPoint)?.let {
                if (it.noOfPagesLoaded == 0) {
                    syncData.value = when (components.parentComponentName) {
                        ComponentNames.MerchantVoucherList.componentName ->
                            hitMerchantVoucherFirstPageCall(it)
                        ComponentNames.BannerInfinite.componentName ->
                            bannerInfiniteUseCase?.loadFirstPageComponents(
                                components.parentComponentId,
                                components.pageEndPoint
                            )
                        ComponentNames.ShopCardInfinite.componentName ->
                            shopCardInfiniteUseCase?.loadFirstPageComponents(
                                components.parentComponentId,
                                components.pageEndPoint
                            )
                        else ->
                            productCardUseCase?.loadFirstPageComponents(
                                components.parentComponentId,
                                components.pageEndPoint
                            )
                    }
                } else {
                    syncData.value =
                        when (components.parentComponentName) {
                            ComponentNames.MerchantVoucherList.componentName -> {
                                if (it.getComponentsItem().isNullOrEmpty()) {
                                    hitMerchantVoucherFirstPageCall(it)
                                } else {
                                    merchantVoucherUseCase?.getVoucherUseCase(
                                        components.id,
                                        components.pageEndPoint
                                    )
                                }
                            }

                            ComponentNames.BannerInfinite.componentName ->
                                bannerInfiniteUseCase?.getBannerUseCase(
                                    components.id,
                                    components.pageEndPoint
                                )

                            ComponentNames.ShopCardInfinite.componentName ->
                                shopCardInfiniteUseCase?.getShopCardUseCase(
                                    components.id,
                                    components.pageEndPoint
                                )

                            else ->
                                productCardUseCase?.getProductCardsUseCase(
                                    components.id,
                                    components.pageEndPoint
                                )
                        }
                }
            }
        }, onError = {
                showLoader.value = false
            })
    }

    private suspend fun hitMerchantVoucherFirstPageCall(components: ComponentsItem): Boolean {
        components.noOfPagesLoaded = 0
        val shouldSync =
            merchantVoucherUseCase?.loadFirstPageComponents(
                components.id,
                components.pageEndPoint
            )
        return if (components.getComponentsItem().isNullOrEmpty()) {
            showLoader.value = false
            false
        } else {
            shouldSync == true
        }
    }
}
