package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsingle

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val PRODUCT_PER_PAGE = 1

class ProductCardSingleViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val productData: MutableLiveData<ComponentsItem?> = MutableLiveData()
    fun getProductData(): LiveData<ComponentsItem?> = productData

    private val _hideView = SingleLiveEvent<Boolean>()
    val hideView: LiveData<Boolean> = _hideView

    private val _showErrorState = SingleLiveEvent<Boolean>()
    val showErrorState: LiveData<Boolean> = _showErrorState

    private val mixLeftData: MutableLiveData<MixLeft?> = MutableLiveData()
    fun getMixLeftData(): LiveData<MixLeft?> = mixLeftData

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    @Inject
    lateinit var productCardsUseCase: ProductCardsUseCase

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
        handleErrorState()
        handleMixLeft()
        fetchProductData()
    }

    private fun handleErrorState() {
        _showErrorState.value = components.verticalProductFailState
    }

    private fun handleMixLeft() {
        mixLeftData.value = components.properties?.mixLeft
    }

    private fun fetchProductData() {
        launchCatchError(block = {
            productCardsUseCase.loadFirstPageComponents(
                components.id,
                components.pageEndPoint,
                PRODUCT_PER_PAGE
            )
            components.shouldRefreshComponent = null
            val prodComponentsItem = components.getComponentsItem()?.firstOrNull()
            prodComponentsItem?.let {
                if (it.properties == null) {
                    it.properties = Properties(template = Constant.ProductTemplate.LIST)
                } else {
                    it.properties?.template = Constant.ProductTemplate.LIST
                }
            }
            prodComponentsItem?.data?.firstOrNull()?.let {
                it.hasThreeDotsWishlist = (it.show3Dots == true)
                if (it.atcButtonCTA == Constant.ATCButtonCTATypes.GENERAL_CART && it.isActiveProductCard == true) {
                    it.hasATCWishlist = true
                    it.hasSimilarProductWishlist = false
                }else if(it.isActiveProductCard != true && it.targetComponentId?.isNotEmpty() == true){
                    it.hasATCWishlist = false
                    it.hasSimilarProductWishlist = true
                }
            }
            productData.value = prodComponentsItem

        }, onError = {
            components.noOfPagesLoaded = 1
            components.shouldRefreshComponent = null
            if (it is UnknownHostException || it is SocketTimeoutException) {
                components.verticalProductFailState = true
                _showErrorState.value = true
            } else {
                _hideView.value = true
            }
        })
    }

    fun reload() {
        components.noOfPagesLoaded = 0
        fetchProductData()
    }


}