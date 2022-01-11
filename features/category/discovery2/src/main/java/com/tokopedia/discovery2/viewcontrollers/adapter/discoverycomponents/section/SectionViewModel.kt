package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.SectionUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SectionViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    @Inject
    lateinit var sectionUseCase: SectionUseCase

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchChildComponents()
    }

    private fun fetchChildComponents() {
        launchCatchError(block = {
                syncData.value = sectionUseCase.getChildComponents(components.id, components.pageEndPoint)

        }, onError = {
//            Todo:: Error Handling
        })
    }

}