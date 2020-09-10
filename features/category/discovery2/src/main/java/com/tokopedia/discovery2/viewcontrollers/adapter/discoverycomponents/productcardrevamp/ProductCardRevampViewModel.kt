package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class ProductCardRevampViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselHeaderData: MutableLiveData<ComponentsItem> = MutableLiveData()

    @Inject
    lateinit var productCardsUseCase: ProductCardsUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    init {
        initDaggerInject()
        components.lihatSemua?.run {
            val lihatSemuaDataItem = DataItem(title = header, subtitle = subheader, btnApplink = applink,
                    creativeName = components.creativeName)
            val lihatSemuaComponentData = ComponentsItem(name = ComponentsList.ProductCardCarousel.componentName, data = listOf(lihatSemuaDataItem))
            productCarouselHeaderData.value = lihatSemuaComponentData
        }
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            this@ProductCardRevampViewModel.syncData.value = productCardsUseCase.loadFirstPageComponents(components.id, components.pageEndPoint, components.rpc_PinnedProduct)
        }, onError = {
            it.printStackTrace()
        })
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun getProductCarouselHeaderData():LiveData<ComponentsItem> = productCarouselHeaderData

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }
}