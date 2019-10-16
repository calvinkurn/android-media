package com.tokopedia.purchase_platform.features.express_checkout.view.profile.di

import com.tokopedia.purchase_platform.features.express_checkout.view.profile.CheckoutProfileContract
import com.tokopedia.purchase_platform.features.express_checkout.view.profile.CheckoutProfilePresenter
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.analytics.ExpressCheckoutAnalyticsTracker
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

@Module
class CheckoutProfileModule {

    @CheckoutProfileScope
    @Provides
    fun providePresenter(presenter: CheckoutProfilePresenter): CheckoutProfileContract.Presenter = presenter


    @CheckoutProfileScope
    @Provides
    fun provideAnalytics(): ExpressCheckoutAnalyticsTracker = ExpressCheckoutAnalyticsTracker()

}