package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.supportingbrand.SupportingBrandUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ShopOfferSupportingBrandViewModel(
    application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val layout: ArrayList<ComponentsItem> = arrayListOf()
    private var isLoading = true

    private val _brands: MutableLiveData<Result<ArrayList<ComponentsItem>>> = MutableLiveData()

    val brands: LiveData<Result<ArrayList<ComponentsItem>>>
        get() = _brands

    @JvmField
    @Inject
    var useCase: SupportingBrandUseCase? = null

    init {
        layout.addShimmer()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + SupervisorJob()

    fun loadFirstPageBrand() {
        _brands.value = Success(ArrayList(layout))

        launchCatchError(
            block = {
                val isFirstLoad = useCase?.loadFirstPageComponents(
                    componentId = component.id,
                    pageEndPoint = component.pageEndPoint
                )

                if (isFirstLoad == true) {
                    setSupportingBrandList()
                }
                isLoading = false
            },
            onError = {
                Timber.e(it)
                _brands.value = Fail(it)
                isLoading = false
            }
        )
    }

    private fun setSupportingBrandList() {
        val components = component.getComponentsItem()

        if (!components.isNullOrEmpty()) {
            layout.addBrandList(components)
            _brands.value = Success(ArrayList(layout))
        } else {
            _brands.value = Fail(Throwable("Empty Data"))
        }
    }

    private fun ArrayList<ComponentsItem>.addShimmer() {
        val shimmerComponent = ComponentsItem(
            name = ComponentNames.BannerCarouselShimmer.componentName
        )

        add(shimmerComponent)
        add(shimmerComponent)
        add(shimmerComponent)
        add(shimmerComponent)
    }

    private fun ArrayList<ComponentsItem>.addBrandList(brandList: List<ComponentsItem>) {
        clear()
        addAll(brandList)
    }
}
