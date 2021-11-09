package com.tokopedia.homecredit.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory;
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey;
import com.tokopedia.homecredit.viewModel.HomeCreditViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HomeCreditViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(HomeCreditViewModel.class)
    abstract ViewModel bindsPayLaterViewModel(HomeCreditViewModel homeCreditViewModel);


}
