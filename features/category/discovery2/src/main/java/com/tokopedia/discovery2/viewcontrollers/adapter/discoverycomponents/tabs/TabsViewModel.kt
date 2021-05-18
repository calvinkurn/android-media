package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.tabsusecase.DynamicTabsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val TAB_DEFAULT_BACKGROUND = "plain"

class TabsViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val setColorTabs: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val setUnifyTabs: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    @Inject
    lateinit var dynamicTabsUseCase: DynamicTabsUseCase


   override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchDynamicTabData()
        updateTabItems()
    }

    private fun updateTabItems() {
        components.getComponentsItem()?.let {
            it as ArrayList<ComponentsItem>
            if (components.properties?.background == TAB_DEFAULT_BACKGROUND) {
                setUnifyTabs.value = it
            } else {
                setColorTabs.value = it
            }
        }
    }

    fun fetchDynamicTabData() {
        components.properties?.let {
            val items = components.getComponentsItem()
            if ((items == null || items.isEmpty()) && it.dynamic) {
                launchCatchError(block = {
                    dynamicTabsUseCase.getTabData(components.id, components.pageEndPoint).run {
                        updateTabItems()
                        this@TabsViewModel.syncData.value = this
                    }
                }, onError = {
                    it.printStackTrace()
                })
            }
        }
    }


    fun getColorTabComponentLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return setColorTabs
    }

    fun getUnifyTabLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return setUnifyTabs
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun setSelectedState(position: Int, selection: Boolean): Boolean {
        if (components.getComponentsItem()?.isNotEmpty() == true) {
            val itemData = components.getComponentsItem()?.get(position)
            if (itemData?.data?.isNotEmpty() == true) {
                if (itemData.data?.get(0)?.isSelected == selection) return false
                itemData.data?.get(0)?.isSelected = selection
            }
        }
        return true
    }

    fun onTabClick() {
        this.syncData.value = true
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun getTabItemData(position : Int): DataItem? {
        components.data?.let {
            if (it.isNotEmpty() && position >= 0 && position < it.size) {
                return it[position]
            }
        }
        return null
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }

    fun reInitTabComponentData() {
        components.reInitComponentItems()
    }

    fun reInitTabTargetComponents() {
        dynamicTabsUseCase.updateTargetProductComponent(components.id, components.pageEndPoint)
    }

    fun getArrowVisibilityStatus() = components.properties?.categoryDetail ?: false

}