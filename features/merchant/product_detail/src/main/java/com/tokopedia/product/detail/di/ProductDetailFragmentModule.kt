package com.tokopedia.product.detail.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewBottomSheet
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductDetailFragmentModule {

    @ProductDetailScope
    @Binds
    internal abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(ViewToViewBottomSheet::class)
    abstract fun getViewToViewBottomSheet(fragment: ViewToViewBottomSheet): Fragment
}
