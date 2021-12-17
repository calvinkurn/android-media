package com.tokopedia.imagepicker_insta.mediacapture

import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object MediaRepository {

    private var mediaChangeLiveDataList : ArrayList<MediaLiveData<Uri>> = arrayListOf()
    private var mediaChangeFlowList : ArrayList<MutableSharedFlow<Uri>> = arrayListOf()

    suspend fun mediaAdded(uri:Uri){
        mediaChangeLiveDataList.forEach {
            it.value = uri
        }
        mediaChangeFlowList.forEach {
            it.emit(uri)
        }
    }

    fun getMediaChangeLiveData():LiveData<Uri>{
        val liveData = MediaLiveData<Uri>()
        liveData.onLifecycleOwnerDestroy = {
            mediaChangeLiveDataList.remove(liveData)
        }
        mediaChangeLiveDataList.add(liveData)
        return liveData
    }

    fun getMediaChangeFlow():SharedFlow<Uri>{
        val mutableSharedFlow = MutableSharedFlow<Uri>()
        mediaChangeFlowList.add(mutableSharedFlow)
        return mutableSharedFlow
    }
}

class MediaLiveData<T>(): MutableLiveData<T>() {
    var onLifecycleOwnerDestroy: (() -> Boolean)? =null

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        owner.lifecycle.addObserver(object :LifecycleEventObserver{
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if(event == Lifecycle.Event.ON_DESTROY){
                    onLifecycleOwnerDestroy?.invoke()
                }
            }
        })
    }
}