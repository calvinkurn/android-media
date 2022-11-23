package com.tokopedia.privacycenter.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.accountlinking.LinkAccountWebviewFragment
import com.tokopedia.privacycenter.consentwithdrawal.ui.ConsentWithdrawalFragment
import com.tokopedia.privacycenter.dsar.ui.DsarAddEmailFragment
import com.tokopedia.privacycenter.dsar.ui.DsarFragment
import com.tokopedia.privacycenter.main.PrivacyCenterFragment
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicySectionBottomSheet
import com.tokopedia.privacycenter.searchhistory.SearchHistoryFragment
import com.tokopedia.privacycenter.sharingwishlist.ui.collection.SharingWishlistBottomSheet
import com.tokopedia.privacycenter.sharingwishlist.ui.collection.SharingWishlistPageFragment
import dagger.Component

@ActivityScope
@Component(modules = [
    PrivacyCenterModule::class,
    AccountLinkingModule::class,
    RecommendationModule::class,
    DsarModule::class,
    PrivacyCenterViewModelModule::class
], dependencies = [BaseAppComponent::class])
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
