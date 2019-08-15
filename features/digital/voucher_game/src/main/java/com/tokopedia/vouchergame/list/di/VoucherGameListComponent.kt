package com.tokopedia.vouchergame.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.vouchergame.list.view.fragment.VoucherGameListFragment
import dagger.Component

/**
 * @author by resakemal on 13/08/19
 */

@VoucherGameListScope
@Component(modules = [VoucherGameListModule::class, VoucherGameListViewModelModule::class], dependencies = [BaseAppComponent::class])
interface VoucherGameListComponent {

    fun inject(voucherGameListFragment: VoucherGameListFragment)

}