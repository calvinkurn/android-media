package com.tokopedia.privacycenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.ui.accountlinking.LinkAccountWebviewFragment
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalFragment
import com.tokopedia.privacycenter.ui.dsar.DsarFragment
import com.tokopedia.privacycenter.ui.dsar.addemail.DsarAddEmailFragment
import com.tokopedia.privacycenter.ui.main.PrivacyCenterFragment
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicySectionBottomSheet
import com.tokopedia.privacycenter.ui.searchhistory.SearchHistoryFragment
import com.tokopedia.privacycenter.ui.sharingwishlist.collection.SharingWishlistBottomSheet
import com.tokopedia.privacycenter.ui.sharingwishlist.collection.SharingWishlistPageFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        RecommendationModule::class,
        PrivacyCenterModule::class,
        PrivacyCenterViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface PrivacyCenterComponent {
    fun inject(fragment: LinkAccountWebviewFragment)
    fun inject(fragment: PrivacyCenterFragment)
    fun inject(fragment: ConsentWithdrawalFragment)
    fun inject(fragment: DsarFragment)
    fun inject(fragment: DsarAddEmailFragment)
    fun inject(fragment: SearchHistoryFragment)
    fun inject(bottomSheet: PrivacyPolicySectionBottomSheet)
    fun inject(fragment: SharingWishlistPageFragment)
    fun inject(bottomSheet: SharingWishlistBottomSheet)
}
