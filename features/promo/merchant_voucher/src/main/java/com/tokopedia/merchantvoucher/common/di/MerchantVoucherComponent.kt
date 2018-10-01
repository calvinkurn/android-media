package com.tokopedia.merchantvoucher.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import dagger.Component

@MerchantVoucherScope
@Component(modules = arrayOf(MerchantVoucherModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface MerchantVoucherComponent {
    fun inject(merchantVoucherListActivity: MerchantVoucherListActivity)
    fun inject(merchantVoucherDetailActivity: MerchantVoucherDetailActivity)
}