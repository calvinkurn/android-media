package com.tokopedia.promotionstarget.di.components

import com.tokopedia.promotionstarget.DialogManager
import com.tokopedia.promotionstarget.TargetPromotionsDialog
import com.tokopedia.promotionstarget.di.modules.PromoTargetModule
import com.tokopedia.promotionstarget.di.modules.ViewModelModule
import com.tokopedia.promotionstarget.di.scopes.PromoTargetScope
import com.tokopedia.promotionstarget.viewmodel.TargetPromotionsDialogVM
import dagger.Component

@PromoTargetScope
@Component(modules = [PromoTargetModule::class, AppModule::class, ViewModelModule::class])
interface PromoTargetComponent {

    fun inject(dialog: TargetPromotionsDialog)
    fun inject(dialogManager: DialogManager)
}