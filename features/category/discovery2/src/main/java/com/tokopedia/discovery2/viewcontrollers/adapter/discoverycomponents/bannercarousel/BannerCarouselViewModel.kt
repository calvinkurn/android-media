package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.bannerusecase.BannerUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class BannerCarouselViewModel(val application: Application, val component: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val bannerCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var isDarkMode: Boolean = false

    @JvmField
    @Inject
    var bannerUseCase: BannerUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private val _hideShimmer = SingleLiveEvent<Boolean>()
    val hideShimmer: LiveData<Boolean> = _hideShimmer

    private val _showErrorState = SingleLiveEvent<Boolean>()
    val showErrorState: LiveData<Boolean> = _showErrorState

    init {
        componentData.value = component
        component.data?.let {
            if (it.isNotEmpty()) {
                bannerCarouselList.value = DiscoveryDataMapper.mapListToComponentList(
                    it,
                    ComponentNames.BannerCarouselItemView.componentName,
                    component.name,
                    position,
                    component.properties?.design
                        ?: ""
                )
            }
        }
        title.value = component.properties?.bannerTitle ?: ""
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchBannerData()
    }

    private fun fetchBannerData() {
        if (component.properties?.dynamic == true) {
            launchCatchError(block = {
                if (bannerUseCase?.loadFirstPageComponents(component.id, component.pageEndPoint, isDarkMode) == true) {
                    if (!component.data.isNullOrEmpty()) {
                        bannerCarouselList.value = DiscoveryDataMapper.mapListToComponentList(
                            component.data!!,
                            ComponentNames.BannerCarouselItemView.componentName,
                            component.name,
                            position,
                            component.properties?.design
                                ?: "",
                            properties = component.properties
                        )
                        title.value = component.properties?.bannerTitle ?: ""
                    } else {
                        _hideShimmer.value = true
                        title.value = ""
                    }
                    componentData.value = component
                }
            }, onError = {
                    component.noOfPagesLoaded = 1
                    if (it is UnknownHostException || it is SocketTimeoutException) {
                        component.verticalProductFailState = true
                        _showErrorState.value = true
                    } else {
                        _hideShimmer.value = true
                        title.value = ""
                    }
                })
        }
    }

    fun shouldShowShimmer(): Boolean {
        return component.properties?.dynamic == true && component.noOfPagesLoaded != 1 && !component.verticalProductFailState
    }

    fun getComponentData(): LiveData<ArrayList<ComponentsItem>> = bannerCarouselList
    fun getTitleLiveData(): LiveData<String> = title
    fun getComponents(): LiveData<ComponentsItem> = componentData

    fun getLihatUrl(): String {
        component.properties?.let {
            return it.ctaApp ?: ""
        }
        return ""
    }

    fun reload() {
        component.noOfPagesLoaded = 0
        fetchBannerData()
    }

    fun checkForDarkMode(context: Context?) {
        if (context != null) {
            isDarkMode = context.isDarkMode()
        }
    }
}
