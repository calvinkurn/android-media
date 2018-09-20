package com.tokopedia.checkout.view.feature.emptycart.di;

import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.view.di.module.ConverterDataModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartPresenter;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 17/09/18.
 */

@Module(includes = {ConverterDataModule.class, TrackingAnalyticsModule.class})
public class EmptyCartModule {

    @Provides
    @EmptyCartScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @EmptyCartScope
    EmptyCartContract.Presenter provideShipmentPresenter(GetCartListUseCase getCartListUseCase,
                                                         CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                                                         CartApiRequestParamGenerator cartApiRequestParamGenerator,
                                                         CompositeSubscription compositeSubscription) {
        return new EmptyCartPresenter(getCartListUseCase, cancelAutoApplyCouponUseCase, cartApiRequestParamGenerator, compositeSubscription);
    }

}
