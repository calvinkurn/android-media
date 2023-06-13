package com.tokopedia.privacycenter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalViewModel
import com.tokopedia.privacycenter.ui.dsar.DsarViewModel
import com.tokopedia.privacycenter.ui.dsar.addemail.DsarAddEmailViewModel
import com.tokopedia.privacycenter.ui.main.PrivacyCenterViewModel
import com.tokopedia.privacycenter.ui.main.section.accountlinking.AccountLinkingViewModel
import com.tokopedia.privacycenter.ui.main.section.consentwithdrawal.ConsentWithdrawalSectionViewModel
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicySectionViewModel
import com.tokopedia.privacycenter.ui.main.section.recommendation.RecommendationViewModel
import com.tokopedia.privacycenter.ui.searchhistory.SearchHistoryViewModel
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistSharedViewModel
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PrivacyCenterViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyCenterViewModel::class)
    internal abstract fun bindPrivacyCenterViewModel(viewModel: PrivacyCenterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountLinkingViewModel::class)
    internal abstract fun bindAccountLinkingViewModel(viewModel: AccountLinkingViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(ConsentWithdrawalSectionViewModel::class)
    abstract fun provideConsentWithdrawalSectionViewModel(viewModel: ConsentWithdrawalSectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(RecommendationViewModel::class)
    abstract fun provideRecommendationViewModel(viewModel: RecommendationViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(ConsentWithdrawalViewModel::class)
    abstract fun provideConsentWithdrawalViewModel(viewModel: ConsentWithdrawalViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(PrivacyPolicySectionViewModel::class)
    abstract fun providePrivacyPolicySectionViewModel(viewModel: PrivacyPolicySectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(DsarViewModel::class)
    abstract fun provideDsarViewModel(viewModel: DsarViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(DsarAddEmailViewModel::class)
    abstract fun provideDsarAddEmailViewModel(viewModel: DsarAddEmailViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SearchHistoryViewModel::class)
    abstract fun provideSearchHistoryViewModel(viewModel: SearchHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SharingWishlistViewModel::class)
    abstract fun provideSharingWishlistViewModel(viewModel: SharingWishlistViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SharingWishlistSharedViewModel::class)
    abstract fun provideSharingWishlistSharedViewModel(viewModel: SharingWishlistSharedViewModel): ViewModel
}
