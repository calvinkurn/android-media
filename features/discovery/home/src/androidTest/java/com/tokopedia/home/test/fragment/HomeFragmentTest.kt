package com.tokopedia.home.test.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.test.di.module.HomeDatabaseModuleTest

class HomeFragmentTest(
    private val mockViewModelFactory: ViewModelProvider.Factory
) : HomeFragment(){

    override fun initViewModel() {
        viewModelFactory = mockViewModelFactory
        super.initViewModel()
    }

    override fun initBuilderComponent(): DaggerBerandaComponent.Builder {
        return super.initBuilderComponent().homeDatabaseModule(HomeDatabaseModuleTest())

    }
}