package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard

import android.app.Application
import android.content.Context
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.contentCardUseCase.ContentCardUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ContentCardViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @JvmField
    @Inject
    var contentCardUseCase: ContentCardUseCase? = null
    private var isDarkMode: Boolean = false

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        launchCatchError(block = {
            this@ContentCardViewModel.syncData.value = contentCardUseCase?.loadFirstPageComponents(components.id, components.pageEndPoint, isDarkMode = isDarkMode)
        }, onError = {
                getComponent(components.id, components.pageEndPoint)?.verticalProductFailState = true
                this@ContentCardViewModel.syncData.value = true
            })
    }

    fun checkForDarkMode(context: Context?) {
        if (context != null) {
            isDarkMode = context.isDarkMode()
        }
    }
}
