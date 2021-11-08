package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import androidx.lifecycle.MutableLiveData

class AutoPlayController {
    val videoIdSet: LinkedHashSet<String> = LinkedHashSet()
    var isAutoPlayEnabled = true
    var currentlyAutoPlaying:MutableLiveData<String> = MutableLiveData()

    constructor(componentId: String){
        currentlyAutoPlaying.value = componentId
        videoIdSet.add(componentId)
    }

    fun autoPlayNext(componentId: String) {
        if (!isAutoPlayEnabled)
            return
        var valueFound = false
        var count = 0
        videoIdSet.forEach { key ->
            count++
            if (valueFound) {
                currentlyAutoPlaying.value = key
                return
            }
            if (key == componentId) {
                if (count == videoIdSet.size) {
                    isAutoPlayEnabled = false
                }
                valueFound = true
            }
        }
    }

    fun pauseAutoPlayedVideo(currentId: String):Boolean {
        return if(isAutoPlayEnabled && currentId != currentlyAutoPlaying.value){
            currentlyAutoPlaying.value = currentId
            true
        }else
            false
    }

    fun shouldAutoPlay(currentId: String):Boolean{
        return (isAutoPlayEnabled && currentId == currentlyAutoPlaying.value)
    }

    fun shouldPause(currentId: String):Boolean{
        return (isAutoPlayEnabled && currentId != currentlyAutoPlaying.value)
    }



    companion object{
        const val AUTOPLAY_ID = "autoplay"
    }

}