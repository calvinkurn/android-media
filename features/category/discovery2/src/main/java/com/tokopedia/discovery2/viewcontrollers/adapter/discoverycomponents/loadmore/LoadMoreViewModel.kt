package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore

import android.app.Application
import android.content.Context
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.bannerinfiniteusecase.BannerInfiniteUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoadMoreViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

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

    private var isDarkMode: Boolean = false

    fun getViewOrientation() = components.loadForHorizontal

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            if (!getViewOrientation()) {
                syncData.value = when (components.parentComponentName) {
                    ComponentNames.MerchantVoucherList.componentName ->
                        merchantVoucherUseCase?.getPaginatedData(components.id, components.pageEndPoint)
                    ComponentNames.BannerInfinite.componentName ->
                        bannerInfiniteUseCase?.getBannerUseCase(components.id, components.pageEndPoint, isDarkMode = isDarkMode)
                    ComponentNames.ShopCardInfinite.componentName ->
                        shopCardInfiniteUseCase?.getShopCardUseCase(components.id, components.pageEndPoint)
                    else -> productCardUseCase?.getProductCardsUseCase(components.id, components.pageEndPoint)
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

    fun checkForDarkMode(context: Context?) {
        if (context != null) {
            isDarkMode = context.isDarkMode()
        }
    }
}
