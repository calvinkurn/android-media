package com.tokopedia.gamification.giftbox.data.di.modules

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.gamification.giftbox.data.di.scope.GiftBoxScope
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxDailyViewModel
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxImageDownloadViewModel
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxTapTapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @GiftBoxScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @GiftBoxScope
    @Binds
    @IntoMap
    @ViewModelKey(GiftBoxDailyViewModel::class)
    abstract fun giftBoxViewModel(viewModel: GiftBoxDailyViewModel): ViewModel

    @GiftBoxScope
    @Binds
    @IntoMap
    @ViewModelKey(GiftBoxTapTapViewModel::class)
    abstract fun giftBoxTapTapViewModel(viewModel: GiftBoxTapTapViewModel): ViewModel

    @GiftBoxScope
    @Binds
    @IntoMap
    @ViewModelKey(GiftBoxImageDownloadViewModel::class)
    abstract fun giftBoxImageDownloadViewModel(viewModel: GiftBoxImageDownloadViewModel): ViewModel
}