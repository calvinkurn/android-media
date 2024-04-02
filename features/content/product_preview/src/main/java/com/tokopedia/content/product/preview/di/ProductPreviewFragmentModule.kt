package com.tokopedia.content.product.preview.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.content.product.preview.view.fragment.ProductFragment
import com.tokopedia.content.product.preview.view.fragment.ProductPreviewFragment
import com.tokopedia.content.product.preview.view.fragment.ReviewFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ProductPreviewFragmentModule {

    @Binds
    fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ProductPreviewFragment::class)
    fun bindProductPreviewFragment(fragment: ProductPreviewFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductFragment::class)
    fun bindProductFragment(fragment: ProductFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ReviewFragment::class)
    fun bindReviewFragment(fragment: ReviewFragment): Fragment

}
