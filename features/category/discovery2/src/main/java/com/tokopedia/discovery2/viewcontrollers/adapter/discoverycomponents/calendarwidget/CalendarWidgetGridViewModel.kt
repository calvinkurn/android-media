package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.app.Application
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CalendarWidgetGridViewModel(
    application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    @JvmField
    @Inject
    var calenderWidgetUseCase: ProductCardsUseCase? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            if (components.properties?.calendarType == Constant.Calendar.DYNAMIC) {
                this@CalendarWidgetGridViewModel.syncData.value = calenderWidgetUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint)
            }
        }, onError = {
                getComponent(components.id, components.pageEndPoint)?.verticalProductFailState = true
                this@CalendarWidgetGridViewModel.syncData.value = true
            })
    }
}
