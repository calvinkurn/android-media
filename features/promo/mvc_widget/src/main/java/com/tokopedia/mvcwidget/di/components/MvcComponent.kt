package com.tokopedia.mvcwidget.di.components

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.mvcwidget.di.module.MvcModule
import com.tokopedia.mvcwidget.di.module.ViewModelModule
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponFragment
import com.tokopedia.mvcwidget.views.MvcDetailView
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.mvcwidget.views.benefit.PromoBenefitBottomSheet
import com.tokopedia.tokomember.di.TokomemberDispatcherModule
import dagger.Component

@ActivityScope
@Component(
    dependencies = [BaseAppComponent::class],
    modules = [MvcModule::class, ViewModelModule::class]
)

interface MvcComponent {
    fun inject(view: MvcView)
    fun inject(view: MvcDetailView)
    fun inject(view: MerchantCouponFragment)
    fun inject(view: PromoBenefitBottomSheet)
}
