package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ExplicitWidgetViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val explicitWidgetData: SingleLiveEvent<ComponentsItem> = SingleLiveEvent()
    @Inject
    lateinit var explicitViewContract: ExplicitViewModel

    fun getComponentData(): SingleLiveEvent<ComponentsItem> = explicitWidgetData

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        explicitWidgetData.value = components
    }

    fun setWidgetHiddenState(state: Boolean) {
        components.isExplicitWidgetHidden = state
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

}