package com.tokopedia.shop.flashsale.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.shop.flashsale.di.module.ShopFlashSaleModule
import com.tokopedia.shop.flashsale.di.module.ShopFlashSaleViewModelModule
import com.tokopedia.shop.flashsale.di.scope.ShopFlashSaleScope
import com.tokopedia.shop.flashsale.presentation.creation.highlight.ManageHighlightedProductFragment
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationActivity
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationFragment
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.CampaignDatePickerBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.VpsPackageBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.manage.ChooseProductFragment
import com.tokopedia.shop.flashsale.presentation.creation.manage.ManageProductActivity
import com.tokopedia.shop.flashsale.presentation.creation.manage.ManageProductFragment
import com.tokopedia.shop.flashsale.presentation.creation.manage.bottomsheet.EditProductInfoBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.CampaignRuleActivity
import com.tokopedia.shop.flashsale.presentation.creation.rule.CampaignRuleFragment
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.MerchantCampaignTNCBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.ChooseRelatedCampaignBottomSheet
import com.tokopedia.shop.flashsale.presentation.detail.CampaignDetailActivity
import com.tokopedia.shop.flashsale.presentation.detail.CampaignDetailFragment
import com.tokopedia.shop.flashsale.presentation.draft.bottomsheet.DraftDeleteBottomSheet
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListActivity
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListContainerFragment
import com.tokopedia.shop.flashsale.presentation.list.list.CampaignListFragment
import com.tokopedia.shop.flashsale.presentation.list.quotamonitoring.QuotaMonitoringFragment
import dagger.Component

@ShopFlashSaleScope
@Component(
    modules = [ShopFlashSaleModule::class, ShopFlashSaleViewModelModule::class, ShopCommonModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ShopFlashSaleComponent {
    fun inject(activity: CampaignListActivity)
    fun inject(fragment: CampaignListContainerFragment)
    fun inject(fragment: CampaignListFragment)
    fun inject(fragment: QuotaMonitoringFragment)

    fun inject(activity: CampaignInformationActivity)
    fun inject(fragment: CampaignInformationFragment)
    fun inject(fragment: VpsPackageBottomSheet)


    fun inject(bottomSheet: DraftDeleteBottomSheet)
    fun inject(bottomSheet: CampaignDatePickerBottomSheet)
    fun inject(fragment: MerchantCampaignTNCBottomSheet)

    fun inject(activity: CampaignRuleActivity)
    fun inject(fragment: CampaignRuleFragment)
    fun inject(bottomSheet: ChooseRelatedCampaignBottomSheet)

    fun inject(fragment: ChooseProductFragment)

    fun inject(activity: ManageProductActivity)
    fun inject(fragment: ManageProductFragment)

    fun inject(fragment: ManageHighlightedProductFragment)

    fun inject(fragment: EditProductInfoBottomSheet)

    fun inject(activity: CampaignDetailActivity)
    fun inject(fragment: CampaignDetailFragment)
}