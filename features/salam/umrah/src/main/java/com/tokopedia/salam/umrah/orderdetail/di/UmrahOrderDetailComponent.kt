package com.tokopedia.salam.umrah.orderdetail.di

import com.tokopedia.salam.umrah.common.di.UmrahComponent
import com.tokopedia.salam.umrah.orderdetail.presentation.activity.UmrahOrderDetailActivity
import com.tokopedia.salam.umrah.orderdetail.presentation.fragment.UmrahOrderDetailFragment
import dagger.Component

/**
 * @author by furqan on 08/10/2019
 */
@UmrahOrderDetailScope
@Component(modules = [UmrahOrderDetailModule::class, UmrahOrderDetailViewModelModule::class],
        dependencies = [UmrahComponent::class])
interface UmrahOrderDetailComponent {

    fun inject(umrahOrderFragment: UmrahOrderDetailFragment)

    fun inject(umrahOrderDetailActivity: UmrahOrderDetailActivity)

}