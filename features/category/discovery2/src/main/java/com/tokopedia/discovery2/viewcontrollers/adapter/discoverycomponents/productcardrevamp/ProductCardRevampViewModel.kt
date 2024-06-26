package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductCardRevampViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(components), CoroutineScope {
    private val productCarouselHeaderData: MutableLiveData<ComponentsItem> = MutableLiveData()

    @JvmField
    @Inject
    var productCardsUseCase: ProductCardsUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        components.lihatSemua?.run {
            val lihatSemuaDataItem = DataItem(title = header, subtitle = subheader, btnApplink = applink)
            val lihatSemuaComponentData = ComponentsItem(
                name = ComponentsList.ProductCardCarousel.componentName,
                data = listOf(lihatSemuaDataItem),
                creativeName = components.creativeName
            )
            productCarouselHeaderData.value = lihatSemuaComponentData
        }
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            this@ProductCardRevampViewModel.syncData.value = productCardsUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint)
        }, onError = {
                getComponent(components.id, components.pageEndPoint)?.verticalProductFailState = true
                this@ProductCardRevampViewModel.syncData.value = true
            })
    }

    fun getProductCarouselHeaderData(): LiveData<ComponentsItem> = productCarouselHeaderData
}
