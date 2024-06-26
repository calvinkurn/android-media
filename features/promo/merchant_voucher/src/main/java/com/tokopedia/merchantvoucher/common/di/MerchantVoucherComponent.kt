package com.tokopedia.merchantvoucher.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailFragment
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.shop.common.di.ShopCommonModule
import dagger.Component

@MerchantVoucherScope
@Component(modules = [ShopCommonModule::class, MerchantVoucherModule::class], dependencies = [BaseAppComponent::class])
interface MerchantVoucherComponent {
    fun inject(merchantVoucherListFragment: MerchantVoucherListFragment)
    fun inject(merchantVoucherDetailFragment: MerchantVoucherDetailFragment)

    fun merchantVoucherListPresenter(): MerchantVoucherListPresenter
}