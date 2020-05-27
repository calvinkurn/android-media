package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class SpacingViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val spacingComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val viewHeight: MutableLiveData<Int> = MutableLiveData()
    private val viewBackgroundColor: MutableLiveData<String> = MutableLiveData()

    init {
        spacingComponentData.value = components
    }

    override fun initDaggerInject() {
    }

    fun getComponentData(): LiveData<ComponentsItem> = spacingComponentData
    fun getViewHeight(): LiveData<Int> = viewHeight
    fun getViewBackgroundColor(): LiveData<String> = viewBackgroundColor

    fun setupSpacingView() {
        if (!components.data?.get(0)?.sizeMobile.isNullOrEmpty()) {
            viewHeight.value = components.data?.get(0)?.sizeMobile?.toInt()!!

            if (!components.data?.get(0)?.background.isNullOrEmpty()) {
                viewBackgroundColor.value = components.data?.get(0)?.background
            } else {
                viewBackgroundColor.value = Color.WHITE.toString()
            }
        }
    }
}