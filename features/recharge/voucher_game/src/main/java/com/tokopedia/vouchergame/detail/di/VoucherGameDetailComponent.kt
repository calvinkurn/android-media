package com.tokopedia.vouchergame.detail.di

import com.tokopedia.vouchergame.common.di.VoucherGameComponent
import com.tokopedia.vouchergame.detail.view.fragment.VoucherGameDetailFragment
import dagger.Component

/**
 * @author by resakemal on 13/08/19
 */

@VoucherGameDetailScope
@Component(modules = [VoucherGameDetailModule::class, VoucherGameDetailViewModelModule::class], dependencies = [VoucherGameComponent::class])
interface VoucherGameDetailComponent {

    fun inject(voucherGameDetailFragment: VoucherGameDetailFragment)

}