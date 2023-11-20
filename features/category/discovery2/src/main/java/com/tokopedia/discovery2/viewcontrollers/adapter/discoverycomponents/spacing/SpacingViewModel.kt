package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.spacing

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifyprinciples.UnifyColorRef
import com.tokopedia.unifyprinciples.stringToUnifyColor
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

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

    fun setupSpacingView(context: Context?) {
        val spacingSize = components.data?.get(0)?.sizeMobile
        var convertedColor: UnifyColorRef? = null
        if (!spacingSize.isNullOrEmpty()) {
            viewHeight.value = spacingSize.toIntOrZero()
            val spacingBackgroundColor = components.data?.getOrNull(0)?.background
            if (context != null && spacingBackgroundColor != null) {
                convertedColor = stringToUnifyColor(context, spacingBackgroundColor)
            }
            if (spacingBackgroundColor.isNullOrEmpty()) {
                viewBackgroundColor.value = unifyprinciplesR.color.Unify_NN0
            } else {
                try {
                    viewBackgroundColor.value = convertedColor?.unifyColor ?: unifyprinciplesR.color.Unify_NN0
                } catch (exception: IllegalArgumentException) {
                    viewBackgroundColor.value = unifyprinciplesR.color.Unify_NN0
                }
            }
        }
    }
}
