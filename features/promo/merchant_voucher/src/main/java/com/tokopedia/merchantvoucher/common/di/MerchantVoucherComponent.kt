package com.tokopedia.merchantvoucher.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.shop.common.di.ShopCommonModule
import dagger.Component

@MerchantVoucherScope
@Component(modules = arrayOf(MerchantVoucherModule::class, ShopCommonModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface MerchantVoucherComponent {
    fun inject(merchantVoucherListFragment: MerchantVoucherListFragment)
    fun inject(merchantVoucherDetailActivity: MerchantVoucherDetailActivity)
}