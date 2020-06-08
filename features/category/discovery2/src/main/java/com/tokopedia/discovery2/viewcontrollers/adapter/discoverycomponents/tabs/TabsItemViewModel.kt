package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.loadImageWithCallback
import com.tokopedia.utils.image.ImageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class TabsItemViewModel(val application: Application, var components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {


    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val imageLoaded: MutableLiveData<Boolean> = MutableLiveData()



    fun getComponentLiveData(): LiveData<ComponentsItem> {
        return componentData
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentData.value = components
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun initDaggerInject() {

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