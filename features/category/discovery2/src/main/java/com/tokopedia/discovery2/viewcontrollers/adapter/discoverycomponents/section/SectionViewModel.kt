package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section

import android.app.Application
import androidx.lifecycle.LiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.sectionusecase.SectionUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section.model.NotifyPayload
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SectionViewModel(
    val application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private val _hideShimmer = SingleLiveEvent<Boolean>()
    val hideShimmerLD: LiveData<Boolean> = _hideShimmer

    private val _showErrorState = SingleLiveEvent<Boolean>()
    val showErrorState: LiveData<Boolean> = _showErrorState

    private val _hideSection = SingleLiveEvent<Boolean>()
    val hideSection: LiveData<Boolean> = _hideSection

    private val _notifyChild = SingleLiveEvent<NotifyPayload>()
    val notifyChild: LiveData<NotifyPayload> = _notifyChild

    @JvmField
    @Inject
    var sectionUseCase: SectionUseCase? = null

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = false
        fetchChildComponents()
    }

    private fun fetchChildComponents() {
        launchCatchError(block = {
            val shouldRefresh =
                sectionUseCase?.getChildComponents(components.id, components.pageEndPoint)
            if (shouldRefresh == true) {
                if (components.getComponentsItem().isNullOrEmpty()) {
                    _hideSection.value = true
                }
                syncData.value = true
                components.shouldRefreshComponent = true
            }
        }, onError = {
                components.noOfPagesLoaded = 1
                if (it is UnknownHostException || it is SocketTimeoutException) {
                    components.verticalProductFailState = true
                    _showErrorState.value = true
                } else {
                    _hideSection.value = true
                    _hideShimmer.value = true
                }
            })
    }

    fun shouldShowShimmer(): Boolean {
        return components.noOfPagesLoaded != 1 && !components.verticalProductFailState
    }

    fun reload() {
        components.noOfPagesLoaded = 0
        fetchChildComponents()
    }

    fun shouldShowError(): Boolean {
        return components.verticalProductFailState
    }

    fun getSectionID(): String {
        return components.sectionId
    }

    fun notifyChildViewModel(
        identifier: String,
        data: Any,
        type: ComponentsList
    ) {
        _notifyChild.value = NotifyPayload(identifier, type, data)
    }
}
