package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.tabsusecase.TabsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.loadImageWithCallback
import com.tokopedia.utils.image.ImageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class TabsItemViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private var tabsUseCase: TabsUseCase = TabsUseCase()

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val compositeComponentList: MutableLiveData<List<ComponentsItem>> = MutableLiveData()
    private val imageLoaded: MutableLiveData<Boolean> = MutableLiveData()

    init {
        componentData.value = components
    }

    fun getComponentLiveData(): LiveData<ComponentsItem> {
        return componentData
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun initDaggerInject() {

    }

    fun getCompositeComponentLiveData(): LiveData<List<ComponentsItem>> {
        return compositeComponentList
    }

    fun populateTabCompositeComponents(selectedTabData: DataItem) {
        compositeComponentList.value = tabsUseCase.getComponentsWithID(selectedTabData)
    }

    fun getImageLoadedLiveData(): LiveData<Boolean> {
        return imageLoaded
    }

    fun loadImage(tabImageView: ImageView, url: String?) {
        tabImageView.loadImageWithCallback(url ?: "", object : ImageUtils.ImageLoaderStateListener {
            override fun successLoad() {
                imageLoaded.postValue(true)
            }

            override fun failedLoad() {
                imageLoaded.postValue(false)
            }
        })
    }
}