package com.tokopedia.cmhomewidget.di.module

import android.view.LayoutInflater
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductListener
import com.tokopedia.cmhomewidget.presentation.customview.CMHomeWidget
import dagger.Module
import dagger.Provides


@Module
class CMHomeWidgetModule(private val cmHomeWidget: CMHomeWidget) {

    @Provides
    fun provideCMHomeWidgetProductListener(): CMHomeWidgetProductListener {
        return cmHomeWidget
    }

    @Provides
    fun provideCMHomeWidgetCardListener(): CMHomeWidgetCardListener {
        return cmHomeWidget
    }

    @Provides
    fun provideCMHomeWidgetLayoutBinding(): LayoutCmHomeWidgetBinding {
        return LayoutCmHomeWidgetBinding.inflate(
            LayoutInflater.from(cmHomeWidget.context),
            cmHomeWidget,
            true
        )
    }
}