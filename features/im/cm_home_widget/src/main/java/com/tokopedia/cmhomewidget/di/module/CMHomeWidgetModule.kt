package com.tokopedia.cmhomewidget.di.module

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.di.qualifier.CMHomeWidgetUserSession
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetPaymentCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener
import com.tokopedia.cmhomewidget.presentation.customview.CMHomeWidget
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module
class CMHomeWidgetModule(private val cmHomeWidget: CMHomeWidget) {

    @Provides
    fun provideCMHomeWidgetProductListener(): CMHomeWidgetProductCardListener {
        return cmHomeWidget
    }

    @Provides
    fun provideCMHomeWidgetPaymentListener(): CMHomeWidgetPaymentCardListener {
        return cmHomeWidget
    }

    @Provides
    fun provideCMHomeWidgetCardListener(): CMHomeWidgetViewAllCardListener {
        return cmHomeWidget
    }

    @Provides
    @CMHomeWidgetScope
    @CMHomeWidgetUserSession
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @CMHomeWidgetScope
    fun provideCMHomeWidgetLayoutBinding(): LayoutCmHomeWidgetBinding {
        return LayoutCmHomeWidgetBinding.inflate(
            LayoutInflater.from(cmHomeWidget.context),
            cmHomeWidget,
            true
        )
    }
}