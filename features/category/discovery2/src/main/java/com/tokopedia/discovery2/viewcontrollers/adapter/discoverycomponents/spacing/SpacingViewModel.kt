package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero

class SpacingViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val spacingComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val viewHeight: MutableLiveData<Int> = MutableLiveData()
    private val viewBackgroundColor: MutableLiveData<Int> = MutableLiveData()

    init {
        spacingComponentData.value = components
    }


    fun getComponentData(): LiveData<ComponentsItem> = spacingComponentData
    fun getViewHeight(): LiveData<Int> = viewHeight
    fun getViewBackgroundColor(): LiveData<Int> = viewBackgroundColor

    fun setupSpacingView() {
        val spacingSize = components.data?.get(0)?.sizeMobile

        if (!spacingSize.isNullOrEmpty()) {
            viewHeight.value = spacingSize.toIntOrZero()
            val spacingBackgroundColor = components.data?.get(0)?.background
            if (spacingBackgroundColor.isNullOrEmpty()) {
                viewBackgroundColor.value = Color.WHITE
            } else {
                try {
                    viewBackgroundColor.value = Color.parseColor(spacingBackgroundColor)
                } catch (exception: IllegalArgumentException) {
                    viewBackgroundColor.value = Color.WHITE
                }
            }
        }
    }
}