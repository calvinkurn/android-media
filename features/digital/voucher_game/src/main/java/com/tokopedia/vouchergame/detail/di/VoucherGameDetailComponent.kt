package com.tokopedia.vouchergame.detail.di

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.vouchergame.detail.view.fragment.VoucherGameDetailFragment
import dagger.Component

/**
 * @author by resakemal on 13/08/19
 */

@VoucherGameDetailScope
@Component(modules = [VoucherGameDetailModule::class, VoucherGameDetailViewModelModule::class], dependencies = [CommonTopupBillsComponent::class])
interface VoucherGameDetailComponent {

    fun inject(voucherGameDetailFragment: VoucherGameDetailFragment)

}