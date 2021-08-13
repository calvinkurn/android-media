package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsprintsalecarousel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProductCardSprintSaleCarouselViewModel(val application: Application, var components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val productCarouselComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val productCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    @Inject
    lateinit var productCardsUseCase: ProductCardsUseCase


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()




    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        productCarouselComponentData.value = components
        components.getComponentsItem()?.let {
            productCarouselList.value = components.getComponentsItem() as ArrayList<ComponentsItem>?
        }
        fetchProductCarouselData()
    }



    fun getProductCarouselItemsListData() = productCarouselList


    private fun fetchProductCarouselData() {
        launchCatchError(block = {
            if (productCardsUseCase.loadFirstPageComponents(components.id, components.pageEndPoint)) {
                productCarouselList.value = components.getComponentsItem() as ArrayList<ComponentsItem>?
                syncData.value = true
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

}