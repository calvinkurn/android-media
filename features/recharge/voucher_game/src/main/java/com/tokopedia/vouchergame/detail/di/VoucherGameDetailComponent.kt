package com.tokopedia.vouchergame.detail.di

import com.tokopedia.common_digital.common.di.DigitalCacheEnablerQualifier
import com.tokopedia.vouchergame.common.di.VoucherGameComponent
import com.tokopedia.vouchergame.detail.view.fragment.VoucherGameDetailFragment
import dagger.Component

/**
 * @author by resakemal on 13/08/19
 */

@VoucherGameDetailScope
@Component(modules = [VoucherGameDetailModule::class, VoucherGameDetailViewModelModule::class], dependencies = [VoucherGameComponent::class])
interface VoucherGameDetailComponent {

    @DigitalCacheEnablerQualifier
    fun isEnableGqlCache(): Boolean

    fun inject(voucherGameDetailFragment: VoucherGameDetailFragment)

}
