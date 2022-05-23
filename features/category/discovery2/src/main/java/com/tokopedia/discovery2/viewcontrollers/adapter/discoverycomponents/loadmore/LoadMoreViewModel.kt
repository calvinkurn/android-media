package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore

import android.app.Application
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.bannerinfiniteusecase.BannerInfiniteUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoadMoreViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var productCardUseCase: ProductCardsUseCase
    @Inject
    lateinit var merchantVoucherUseCase: MerchantVoucherUseCase
    @Inject
    lateinit var bannerInfiniteUseCase: BannerInfiniteUseCase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getViewOrientation() = components.loadForHorizontal

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            if (!getViewOrientation()) {
                syncData.value = when(components.parentComponentName){
                    ComponentNames.MerchantVoucherList.componentName ->
                        merchantVoucherUseCase.getPaginatedData(components.id,components.pageEndPoint)
                    ComponentNames.BannerInfinite.componentName ->
                        bannerInfiniteUseCase.getBannerUseCase(components.id,components.pageEndPoint )
                    else -> productCardUseCase.getProductCardsUseCase(components.id, components.pageEndPoint)
                }
            }
        }, onError = {
            getComponent(
                components.parentComponentId,
                components.pageEndPoint
            )?.verticalProductFailState = true
            syncData.value = true
        })
    }
}
