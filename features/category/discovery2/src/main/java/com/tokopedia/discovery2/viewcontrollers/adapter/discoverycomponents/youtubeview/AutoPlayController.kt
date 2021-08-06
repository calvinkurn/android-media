package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import com.tokopedia.discovery2.datamapper.getComponent

class AutoPlayController {
    val videoIdSet: LinkedHashSet<String> = LinkedHashSet()
    var isAutoPlayEnabled = true
    var currentlyAutoPlaying:String? = null

    constructor(componentId: String){
        currentlyAutoPlaying = componentId
        videoIdSet.add(componentId)
    }

    fun autoPlayNext(componentId: String, pageEndPoint: String): Boolean {
        if (!isAutoPlayEnabled)
            return false
        var valueFound = false
        var count = 0
        videoIdSet.forEach { key ->
            count++
            if (valueFound) {
                currentlyAutoPlaying = key
                getComponent(key, pageEndPoint)?.let {
                    it.shouldRefreshComponent = true
                }
                return true
            }
            if (key == componentId) {
                if (count == videoIdSet.size) {
                    isAutoPlayEnabled = false
                }
                valueFound = true
            }
        }
        return false
    }

    fun pauseAutoPlayedVideo(pageEndPoint: String) :Boolean {
        if(isAutoPlayEnabled){
            isAutoPlayEnabled = false
            currentlyAutoPlaying?.let { id ->
                getComponent(id, pageEndPoint)?.let{
                    it.shouldRefreshComponent = true
                    return true
                }
            }
        }
        return false
    }

    companion object{
        const val AUTOPLAY_ID = "autoplay"
    }

}