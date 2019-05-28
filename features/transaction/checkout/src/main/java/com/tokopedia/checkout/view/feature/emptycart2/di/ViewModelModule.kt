package com.tokopedia.checkout.view.feature.emptycart2.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.checkout.view.feature.emptycart2.viewmodel.PromoViewModel
import com.tokopedia.checkout.view.feature.emptycart2.viewmodel.RecentViewViewModel
import com.tokopedia.checkout.view.feature.emptycart2.viewmodel.RecommendationViewModel
import com.tokopedia.checkout.view.feature.emptycart2.viewmodel.WishlistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

@Module
@EmptyCartScope
abstract class ViewModelModule {

    @EmptyCartScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PromoViewModel::class)
    internal abstract fun promoViewModel(viewModel: PromoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WishlistViewModel::class)
    internal abstract fun wishlistViewModel(viewModel: WishlistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecommendationViewModel::class)
    internal abstract fun recommendationViewModel(viewModel: RecommendationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecentViewViewModel::class)
    internal abstract fun recentViewViewModel(viewModel: RecentViewViewModel): ViewModel

}