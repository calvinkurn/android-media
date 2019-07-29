package com.tokopedia.purchase_platform.express_checkout.view.variant.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.express_checkout.view.profile.CheckoutProfileBottomSheet
import com.tokopedia.purchase_platform.express_checkout.view.variant.CheckoutVariantContract
import com.tokopedia.purchase_platform.express_checkout.view.variant.CheckoutVariantItemDecorator
import com.tokopedia.purchase_platform.express_checkout.view.variant.CheckoutVariantPresenter
import com.tokopedia.purchase_platform.express_checkout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheet
import com.tokopedia.transactionanalytics.ExpressCheckoutAnalyticsTracker
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

@Module
class CheckoutVariantModule {

    @CheckoutVariantScope
    @Provides
    fun providePresenter(presenter: CheckoutVariantPresenter): CheckoutVariantContract.Presenter = presenter

    @CheckoutVariantScope
    @Provides
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @CheckoutVariantScope
    @Provides
    fun provideFragmentViewModel(): FragmentViewModel {
        return FragmentViewModel()
    }

    @CheckoutVariantScope
    @Provides
    fun provideItemDecorator(): CheckoutVariantItemDecorator {
        return CheckoutVariantItemDecorator()
    }

    @CheckoutVariantScope
    @Provides
    fun provideShippingDurationBottomsheet(): ShippingDurationBottomsheet {
        return ShippingDurationBottomsheet.newInstance()
    }

    @CheckoutVariantScope
    @Provides
    fun provideShippingCourierBottomsheet(): ShippingCourierBottomsheet {
        return ShippingCourierBottomsheet.newInstance()
    }

    @CheckoutVariantScope
    @Provides
    fun provideErrorBottomsheet(): ErrorBottomsheets {
        return ErrorBottomsheets()
    }

    @CheckoutVariantScope
    @Provides
    fun provideCheckoutProfileBottomsheet(): CheckoutProfileBottomSheet {
        return CheckoutProfileBottomSheet.newInstance()
    }

    @CheckoutVariantScope
    @Provides
    fun provideAnalytics(): ExpressCheckoutAnalyticsTracker = ExpressCheckoutAnalyticsTracker()

    @CheckoutVariantScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @Named("atcOcsMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart_one_click_shipment)
    }

}