package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.productbundlingusecase.ProductBundlingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class ProductBundlingViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val bundledProductData: MutableLiveData<ArrayList<BundleUiModel>> = MutableLiveData()
    private val _emptyBundleData: MutableLiveData<Boolean> = MutableLiveData()
    private val _showErrorState = SingleLiveEvent<Boolean>()
    var lastSentPosition: Int = 0
    var hasScrolled = false

    @JvmField
    @Inject
    var productBundlingUseCase: ProductBundlingUseCase? = null

    @JvmField
    @Inject
    var coroutineDispatchers: CoroutineDispatchers? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getBundledProductDataList(): LiveData<ArrayList<BundleUiModel>> = bundledProductData
    fun getEmptyBundleData(): LiveData<Boolean> = _emptyBundleData
    fun showErrorState(): LiveData<Boolean> = _showErrorState

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchProductBundlingData()
    }

    fun fetchProductBundlingData() {
        launchCatchError(block = {
            productBundlingUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint)
            if (!components.data.isNullOrEmpty()) {
                bundledProductData.value = components.data?.let {
                    val dispatcher = coroutineDispatchers?.default ?: Dispatchers.Default
                    withContext(dispatcher) {
                        DiscoveryDataMapper().mapListToBundleProductList(it)
                    }
                }
                _emptyBundleData.value = false
            } else {
                _emptyBundleData.value = true
            }
        }, onError = {
            _showErrorState.value = true
        })
    }

    fun resetComponent() {
        components.noOfPagesLoaded = 0
        components.pageLoadedCounter = 1
    }
}
