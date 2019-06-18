package com.tokopedia.expresscheckout.view.profile.di

import com.tokopedia.expresscheckout.view.profile.CheckoutProfileContract
import com.tokopedia.expresscheckout.view.profile.CheckoutProfilePresenter
import com.tokopedia.transactionanalytics.ExpressCheckoutAnalyticsTracker
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