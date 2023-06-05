package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.emptystate.EmptyStateModel
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.usecase.emptystateusecase.EmptyStateUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import javax.inject.Inject

class EmptyStateViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) :
    DiscoveryBaseViewModel() {
    @JvmField
    @Inject
    var emptyStateRepository: EmptyStateRepository? = null

    @JvmField
    @Inject
    var emptyStateUseCase: EmptyStateUseCase? = null

    fun getEmptyStateData(): EmptyStateModel? {
        return emptyStateRepository?.getEmptyStateData(components)
    }
    fun handleEmptyStateReset() {
        this@EmptyStateViewModel.syncData.value = emptyStateUseCase?.resetChildComponents(components)
    }
}
