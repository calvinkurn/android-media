package com.tokopedia.expresscheckout.view.variant.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets
import com.tokopedia.expresscheckout.domain.mapper.atc.AtcDataMapper
import com.tokopedia.expresscheckout.domain.mapper.checkout.CheckoutDataMapper
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileBottomSheet
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantItemDecorator
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantPresenter
import com.tokopedia.expresscheckout.view.variant.subscriber.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.logisticcommon.utils.TkpdProgressDialog
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheet
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription

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
    fun provideProgressDialog(@ApplicationContext context: Context): TkpdProgressDialog {
        return TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS)
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
}