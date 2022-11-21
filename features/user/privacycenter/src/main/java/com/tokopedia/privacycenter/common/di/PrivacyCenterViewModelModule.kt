package com.tokopedia.privacycenter.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.consentwithdrawal.viewmodel.ConsentWithdrawalViewModel
import com.tokopedia.privacycenter.main.PrivacyCenterViewModel
import com.tokopedia.privacycenter.main.section.accountlinking.AccountLinkingViewModel
import com.tokopedia.privacycenter.main.section.consentwithdrawal.ConsentWithdrawalSectionViewModel
import com.tokopedia.privacycenter.main.section.recommendation.RecommendationViewModel
import com.tokopedia.privacycenter.sharingwishlist.viewmodel.SharingWishlistSharedViewModel
import com.tokopedia.privacycenter.sharingwishlist.viewmodel.SharingWishlistViewModel
import com.tokopedia.privacycenter.searchhistory.SearchHistoryViewModel
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
