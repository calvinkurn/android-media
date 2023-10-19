package com.tokopedia.stories.creation.di.product

import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.content.common.util.bottomsheet.ContentDialogCustomizer
import com.tokopedia.content.common.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.content.product.picker.seller.analytic.ContentPinnedProductAnalytic
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import com.tokopedia.stories.creation.analytic.product.seller.StoriesCreationPinnedProductAnalytic
import com.tokopedia.stories.creation.analytic.product.seller.StoriesCreationProductSellerAnalytic
import com.tokopedia.stories.creation.analytic.sender.StoriesCreationAnalyticSender
import com.tokopedia.stories.creation.analytic.sender.StoriesCreationAnalyticSenderImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on October 19, 2023
 */
@Module
abstract class ProductPickerBindTestModule {

    @Binds
    @ProductPickerTestScope
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @ProductPickerTestScope
    abstract fun bindContentProductPickerSellerAnalytic(storiesCreationProductSellerAnalytic: StoriesCreationProductSellerAnalytic): ContentProductPickerSellerAnalytic

    @Binds
    @ProductPickerTestScope
    abstract fun bindContentPinnedProductAnalytic(storiesCreationPinnedProductAnalytic: StoriesCreationPinnedProductAnalytic): ContentPinnedProductAnalytic

    @Binds
    @ProductPickerTestScope
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): ContentDialogCustomizer

    @Binds
    abstract fun bindStoriesCreationAnalyticSender(storiesCreationAnalyticSender: StoriesCreationAnalyticSenderImpl): StoriesCreationAnalyticSender
}
