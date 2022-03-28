package com.tokopedia.tokomember_seller_dashboard.di.component

import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberActivityContextModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberDashModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberViewmodelModule
import com.tokopedia.tokomember_seller_dashboard.di.scope.TokomemberDashScope
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashCreateCardFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashIntroFragment
import dagger.Component

@TokomemberDashScope
@Component(modules = [TokomemberViewmodelModule::class, TokomemberActivityContextModule::class, TokomemberDashModule::class])
interface TokomemberDashComponent {
    fun inject(tokomemberDashIntroFragment: TokomemberDashIntroFragment)
    fun inject(tokomemberDashCreateCardFragment: TokomemberDashCreateCardFragment)

}