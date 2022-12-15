package com.tokopedia.tkpd.flashsale.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tkpd.flashsale.di.module.TokopediaFlashSaleModule
import com.tokopedia.tkpd.flashsale.di.module.TokopediaFlashSaleViewModelModule
import com.tokopedia.tkpd.flashsale.di.scope.TokopediaFlashSaleScope
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.fragment.ChooseProductFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.CampaignDetailFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.bottomsheet.CampaignDetailBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignCriteriaFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignProductCriteriaFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.fragment.CampaignTimelineFragment
import com.tokopedia.tkpd.flashsale.presentation.ineligibleaccess.IneligibleAccessFragment
import com.tokopedia.tkpd.flashsale.presentation.list.child.FlashSaleListFragment
import com.tokopedia.tkpd.flashsale.presentation.list.container.FlashSaleContainerFragment
import com.tokopedia.tkpd.flashsale.presentation.list.container.FlashSaleListActivity
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant.ManageProductNonVariantFragment
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant.ManageProductNonVariantMultilocFragment
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.ManageProductMultiLocationVariantFragment
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.ManageProductVariantFragment
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.FlashSaleManageProductListFragment
import dagger.Component

@TokopediaFlashSaleScope
@Component(
    modules = [TokopediaFlashSaleModule::class, TokopediaFlashSaleViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface TokopediaFlashSaleComponent {
    fun inject(activity: FlashSaleListActivity)
    fun inject(fragment: FlashSaleContainerFragment)
    fun inject(fragment: FlashSaleListFragment)

    fun inject(fragment: CampaignTimelineFragment)
    fun inject(fragment: CampaignCriteriaFragment)
    fun inject(fragment: CampaignProductCriteriaFragment)
    fun inject(bottomSheet: CampaignDetailBottomSheet)

    fun inject(fragment: CampaignDetailFragment)
    fun inject(fragment: IneligibleAccessFragment)

    fun inject(fragment: ChooseProductFragment)

    fun inject(fragment: ManageProductMultiLocationVariantFragment)

    fun inject(fragment: ManageProductNonVariantFragment)
    fun inject(fragment: ManageProductNonVariantMultilocFragment)
    fun inject(fragment: FlashSaleManageProductListFragment)
    fun inject(fragment: ManageProductVariantFragment)
}
