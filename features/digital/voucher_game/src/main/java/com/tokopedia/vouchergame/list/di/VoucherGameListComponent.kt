package com.tokopedia.vouchergame.list.di

import com.tokopedia.vouchergame.common.di.VoucherGameComponent
import com.tokopedia.vouchergame.list.view.fragment.VoucherGameListFragment
import dagger.Component

/**
 * @author by resakemal on 13/08/19
 */

@VoucherGameListScope
@Component(modules = [VoucherGameListModule::class, VoucherGameListViewModelModule::class], dependencies = [VoucherGameComponent::class])
interface VoucherGameListComponent {

    fun inject(voucherGameListFragment: VoucherGameListFragment)

}