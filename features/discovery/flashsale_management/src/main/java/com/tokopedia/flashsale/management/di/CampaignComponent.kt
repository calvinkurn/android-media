package com.tokopedia.flashsale.management.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.flashsale.management.product.view.FlashSaleProductDetailFragment
import com.tokopedia.flashsale.management.product.view.FlashSaleProductListFragment
import com.tokopedia.flashsale.management.view.activity.CampaignDetailActivity
import com.tokopedia.flashsale.management.view.fragment.BaseCampaignFragment
import com.tokopedia.flashsale.management.view.fragment.FlashSaleInfoFragment
import dagger.Component

@CampaignScope
@Component(modules = [CampaignModule::class], dependencies = [BaseAppComponent::class])
interface CampaignComponent {

    fun inject(fragment: BaseCampaignFragment)
    fun inject(fragment: FlashSaleInfoFragment)
    fun inject(fragment: FlashSaleProductListFragment)
    fun inject(fragment: FlashSaleProductDetailFragment)
    fun inject(activity: CampaignDetailActivity)
}
