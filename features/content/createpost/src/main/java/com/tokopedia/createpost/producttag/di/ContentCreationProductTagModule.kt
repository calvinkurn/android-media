package com.tokopedia.createpost.producttag.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.createpost.producttag.di.key.FragmentKey
import com.tokopedia.createpost.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.createpost.producttag.view.fragment.factory.ProductTagFragmentFactory
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
@Module
abstract class ContentCreationProductTagModule {

    /** Fragment */
    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: ProductTagFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ProductTagParentFragment::class)
    abstract fun bindProductTagParentFragment(fragment: ProductTagParentFragment): Fragment

    /** View Model */
    @Binds
    @IntoMap
    @ViewModelKey(ProductTagViewModel::class)
    abstract fun bindProductTagViewModel(viewModel: ProductTagViewModel): ViewModel
}