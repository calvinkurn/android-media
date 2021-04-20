package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.emptystate.EmptyStateModel
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.usecase.emptystateusecase.EmptyStateUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import javax.inject.Inject

class EmptyStateViewModel(val application: Application,
                          val components: ComponentsItem, val position: Int)
    : DiscoveryBaseViewModel() {
    @Inject
    lateinit var emptyStateRepository: EmptyStateRepository
    @Inject
    lateinit var emptyStateUseCase: EmptyStateUseCase


    fun getEmptyStateData(): EmptyStateModel {
        return emptyStateRepository.getEmptyStateData(components)
    }
    fun handleEmptyStateReset(){
        this@EmptyStateViewModel.syncData.value = emptyStateUseCase.resetChildComponents(components)
    }
}