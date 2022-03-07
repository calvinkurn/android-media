package com.tokopedia.sellerorder.detail.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment
import com.tokopedia.sellerorder.orderextension.di.SomOrderExtensionViewModelModule
import dagger.Component

/**
 * Created by fwidjaja on 2019-09-30.
 */

@SomDetailScope
@Component(
    modules = [
        SomDetailViewModelModule::class,
        SomOrderExtensionViewModelModule::class
    ],
    dependencies = [SomComponent::class]
)
interface SomDetailComponent {
    fun inject(somDetailActivity: SomDetailActivity)
    fun inject(somDetailFragment: SomDetailFragment)
}