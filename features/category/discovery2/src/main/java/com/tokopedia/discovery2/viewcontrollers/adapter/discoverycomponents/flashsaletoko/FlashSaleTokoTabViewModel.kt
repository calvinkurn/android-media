package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.flashsaletoko

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.flashsaletokousecase.FlashSaleTokoUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FlashSaleTokoTabViewModel(
    val application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val tabs: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    @JvmField
    @Inject
    var useCase: FlashSaleTokoUseCase? = null

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchData()
    }

    private fun fetchData() {
        launchCatchError(block = {
            useCase?.getData(component.id, component.pageEndPoint).run {
                val componentsItems = component.getComponentsItem() as ArrayList<ComponentsItem>
                tabs.value = componentsItems

                this@FlashSaleTokoTabViewModel.syncData.value = this
            }
        }, onError = {
                Timber.e(it)
            })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getTabLiveData(): LiveData<ArrayList<ComponentsItem>> = tabs

    fun onTabClick(selectedFilterValue: String) {
        this.syncData.value = true

        component.data?.forEach {
            it.isSelected = it.filterValue == selectedFilterValue
        }

        component.reInitComponentItems()

        notifyFestiveSection()
    }

    private fun notifyFestiveSection() {
        component.parentSectionId?.let {
            if (!component.isBackgroundPresent) return@let
            val section = getComponent(it, component.pageEndPoint)
            section?.shouldRefreshComponent = true
        }
    }
}
