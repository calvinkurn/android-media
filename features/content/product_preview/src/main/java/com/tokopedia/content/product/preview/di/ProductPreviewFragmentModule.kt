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
abstract class ProductPreviewFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ProductPreviewFragment::class)
    abstract fun bindProductPreviewFragment(fragment: ProductPreviewFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductFragment::class)
    abstract fun bindProductFragment(fragment: ProductFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ReviewFragment::class)
    abstract fun bindReviewFragment(fragment: ReviewFragment): Fragment

}
