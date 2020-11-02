package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class YouTubeViewViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val videoData = MutableLiveData<DataItem>()


    fun getVideoId(): MutableLiveData<DataItem> {
        videoData.value = components.data?.get(0)
        return videoData
    }
}