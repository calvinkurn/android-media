package com.tokopedia.homecredit.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class HomeCreditViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);


}
