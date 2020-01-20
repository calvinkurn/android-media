package com.tokopedia.promotionstarget.data.di.components

import com.tokopedia.promotionstarget.data.di.modules.GqlModule
import com.tokopedia.promotionstarget.presentation.ui.dialog.TargetPromotionsDialog
import com.tokopedia.promotionstarget.data.di.modules.PromoTargetModule
import com.tokopedia.promotionstarget.data.di.modules.ViewModelModule
import com.tokopedia.promotionstarget.data.di.scopes.PromoTargetScope
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber
import dagger.Component

@PromoTargetScope
@Component(modules = [PromoTargetModule::class, AppModule::class, ViewModelModule::class, GqlModule::class])
interface PromoTargetComponent {

    fun inject(dialog: TargetPromotionsDialog)
    fun inject(dialogManager: GratificationSubscriber)
}