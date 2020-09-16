package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.tabsusecase.TabsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
    lateinit var tabsUseCase: TabsUseCase


    init {
        initDaggerInject()
    }

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

    private fun fetchDynamicTabData() {
        components.properties?.let {
            if (components.getComponentsItem()?.size == 0 && it.dynamic) {
                launchCatchError(block = {
                    tabsUseCase.getTabData(components.id, components.pageEndPoint).run {
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

    fun clearDynamicTabData() {
        if (components.properties?.dynamic == true) {
            components.getComponentsItem()?.let {
                if (it.isNotEmpty()) {
                    it[0].getComponentsItem()?.let { tabchildComponent ->
                        if (tabchildComponent.isNotEmpty()) {
                            getComponent(tabchildComponent[0].id, tabchildComponent[0].pageEndPoint)?.run {
                                this.setComponentsItem(null)
                                this.noOfPagesLoaded = 0
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

}