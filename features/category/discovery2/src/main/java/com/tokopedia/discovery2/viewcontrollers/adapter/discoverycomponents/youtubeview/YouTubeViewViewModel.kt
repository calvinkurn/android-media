package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class YouTubeViewViewModel(val application: Application, private val components: ComponentsItem) : DiscoveryBaseViewModel() {
    override fun initDaggerInject() {

    }

    private val videoId = MutableLiveData<String>()


    fun getVideoId(): LiveData<String> {
        videoId.value = components.data?.getOrElse(0) { DataItem() }?.videoId ?: ""
        return videoId
    }
}