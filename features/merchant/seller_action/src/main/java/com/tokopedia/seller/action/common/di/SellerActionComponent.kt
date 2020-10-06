package com.tokopedia.seller.action.common.di

import com.tokopedia.seller.action.slices.SellerActionSliceProvider
import dagger.Component

@SellerActionScope
@Component(modules = [SellerActionModule::class])
interface SellerActionComponent {

    fun inject(sellerActionSliceProvider: SellerActionSliceProvider)

}