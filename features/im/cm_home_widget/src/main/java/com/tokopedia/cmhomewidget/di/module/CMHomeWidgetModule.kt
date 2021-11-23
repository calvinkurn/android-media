package com.tokopedia.cmhomewidget.di.module

import android.view.LayoutInflater
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener
import com.tokopedia.cmhomewidget.presentation.customview.CMHomeWidget
import dagger.Module
import dagger.Provides


@Module
class CMHomeWidgetModule(private val cmHomeWidget: CMHomeWidget) {

    @Provides
    fun provideCMHomeWidgetProductListener(): CMHomeWidgetProductCardListener {
        return cmHomeWidget
    }

    @Provides
    fun provideCMHomeWidgetCardListener(): CMHomeWidgetViewAllCardListener {
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