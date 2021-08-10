package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class YouTubeViewViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val videoData = MutableLiveData<DataItem>()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
    }

    fun getVideoId(): MutableLiveData<DataItem> {
        videoData.value = components.data?.get(0)
        return videoData
    }

    fun shouldAutoPlay(): Boolean {
        return components.autoPlayController?.shouldAutoPlay(components.id) ?: false
    }

    fun shouldPause(): Boolean {
        return components.autoPlayController?.shouldPause(components.id) ?: false
    }

    fun autoPlayNext() {
        components.autoPlayController?.autoPlayNext(components.id)
    }

    fun isAutoPlayEnabled():Boolean{
        return components.autoPlayController?.isAutoPlayEnabled?:false
    }

    fun disableAutoplay(){
        components.autoPlayController?.isAutoPlayEnabled = false
    }

    fun pauseOtherVideos() {
        components.autoPlayController?.pauseAutoPlayedVideo(components.id)
    }

    fun currentlyAutoPlaying():LiveData<String>?{
        return components.autoPlayController?.currentlyAutoPlaying
    }
}