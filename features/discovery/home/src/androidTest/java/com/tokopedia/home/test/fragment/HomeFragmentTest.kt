package com.tokopedia.home.test.fragment

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.test.di.module.HomeDatabaseModuleTest

class HomeFragmentTest() : HomeFragment(){

    override fun initHomePageFlows(): Boolean {
//        viewModelFactory = mockViewModelFactory
//        Log.d("testLukas", viewModelFactory.toString())
        return super.initHomePageFlows()
    }

    override fun initBuilderComponent(): DaggerBerandaComponent.Builder {
        return super.initBuilderComponent().homeDatabaseModule(HomeDatabaseModuleTest())

    }
}