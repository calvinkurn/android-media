package com.tokopedia.promotionstarget.data.di.components

import com.tokopedia.promotionstarget.data.di.modules.*
import com.tokopedia.promotionstarget.data.di.scopes.CmGratificationScope
import com.tokopedia.promotionstarget.presentation.ui.dialog.CmGratificationDialog
import dagger.Component

@CmGratificationScope
@Component(modules = [PromoTargetModule::class, AppModule::class, ViewModelModule::class, GqlModule::class, DispatcherModule::class])
interface CmGratificationComponent {

    fun inject(dialog: CmGratificationDialog)
}