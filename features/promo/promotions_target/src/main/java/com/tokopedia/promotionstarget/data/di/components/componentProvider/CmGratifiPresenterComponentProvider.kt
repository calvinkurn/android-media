package com.tokopedia.promotionstarget.data.di.components.componentProvider

import android.content.Context
import com.tokopedia.promotionstarget.data.di.components.CmGratificationPresenterComponent
import com.tokopedia.promotionstarget.data.di.components.DaggerCmGratificationPresenterComponent
import com.tokopedia.promotionstarget.data.di.modules.AppModule

open class CmGratifiPresenterComponentProvider {
    open fun getComponent(context:Context): CmGratificationPresenterComponent {
        return DaggerCmGratificationPresenterComponent.builder()
                .appModule(AppModule(context))
                .build()
    }
}