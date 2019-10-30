package com.tokopedia.promotionstarget.di.components

import com.tokopedia.promotionstarget.di.modules.GqlModule
import com.tokopedia.promotionstarget.ui.TargetPromotionsDialog
import com.tokopedia.promotionstarget.di.modules.PromoTargetModule
import com.tokopedia.promotionstarget.di.modules.ViewModelModule
import com.tokopedia.promotionstarget.di.scopes.PromoTargetScope
import com.tokopedia.promotionstarget.subscriber.GratificationSubscriber
import dagger.Component

@PromoTargetScope
@Component(modules = [PromoTargetModule::class, AppModule::class, ViewModelModule::class, GqlModule::class])
interface PromoTargetComponent {

    fun inject(dialog: TargetPromotionsDialog)
    fun inject(dialogManager: GratificationSubscriber)
}