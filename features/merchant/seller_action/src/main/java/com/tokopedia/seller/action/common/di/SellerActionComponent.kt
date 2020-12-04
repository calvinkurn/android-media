package com.tokopedia.seller.action.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.seller.action.SellerActionActivity
import com.tokopedia.seller.action.SellerActionSliceProvider
import dagger.Component

@SellerActionScope
@Component(
        modules = [SellerActionModule::class],
        dependencies = [BaseAppComponent::class])
interface SellerActionComponent {

    fun inject(sellerActionSliceProvider: SellerActionSliceProvider)
    fun inject(sellerActionActivity: SellerActionActivity)

}