package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent

class YouTubeViewViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val videoData = MutableLiveData<DataItem>()
    private val _shouldResync = SingleLiveEvent<Boolean>()

    val shouldResync: LiveData<Boolean>
            get() = _shouldResync

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
    }

    fun getVideoId(): MutableLiveData<DataItem> {
        videoData.value = components.data?.get(0)
        return videoData
    }

    fun shouldAutoPlay(): Boolean {
        return (components.autoPlayController?.isAutoPlayEnabled ?: false &&
                components.autoPlayController?.currentlyAutoPlaying == components.id)
    }

    fun autoPlayNext() {
        _shouldResync.value =
            components.autoPlayController?.autoPlayNext(components.id, components.pageEndPoint)
                ?: false
    }
    fun isAutoPlayEnabled():Boolean{
        return components.autoPlayController?.isAutoPlayEnabled?:false
    }

    fun disableAutoplay(){
        components.autoPlayController?.isAutoPlayEnabled = false
    }

    fun pauseOtherVideos() {
        if (components.autoPlayController?.currentlyAutoPlaying != components.id) {
            _shouldResync.value =
                components.autoPlayController?.pauseAutoPlayedVideo(components.pageEndPoint)
                    ?: false
        }
    }
}