package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite

import android.app.Application
import android.content.Context
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.bannerinfiniteusecase.BannerInfiniteUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ShopBannerInfiniteViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(components), CoroutineScope {

    @JvmField
    @Inject
    var bannerInfiniteUseCase: BannerInfiniteUseCase? = null
    private var isDarkMode: Boolean = false

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            this@ShopBannerInfiniteViewModel.syncData.value = bannerInfiniteUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint, isDarkMode = isDarkMode)
        }, onError = {
                getComponent(components.id, components.pageEndPoint)?.verticalProductFailState = true
                this@ShopBannerInfiniteViewModel.syncData.value = true
            })
    }

    fun checkForDarkMode(context: Context?) {
        if (context != null) {
            isDarkMode = context.isDarkMode()
        }
    }
}
