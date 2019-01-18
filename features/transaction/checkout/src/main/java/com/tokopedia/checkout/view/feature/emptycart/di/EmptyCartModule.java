package com.tokopedia.checkout.view.feature.emptycart.di;

import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.checkout.view.di.module.ConverterDataModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartContract;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartFragment;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartPresenter;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 17/09/18.
 */

@Module(includes = {ConverterDataModule.class, TrackingAnalyticsModule.class})
public class EmptyCartModule {

    private EmptyCartFragment emptyCartFragment;

    public EmptyCartModule(EmptyCartFragment emptyCartFragment) {
        this.emptyCartFragment = emptyCartFragment;
    }

    @Provides
    @EmptyCartScope
    GetWishlistUseCase providesGetWishListUseCase() {
        return new GetWishlistUseCase(emptyCartFragment.getContext());
    }

    @Provides
    @EmptyCartScope
    GetRecentViewUseCase providegetRecentViewUseCase() {
        return new GetRecentViewUseCase(emptyCartFragment.getContext());
    }

    @Provides
    @EmptyCartScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @EmptyCartScope
    UserSessionInterface provideUserSessionInterface() {
        return new UserSession(emptyCartFragment.getContext());
    }

    @Provides
    @EmptyCartScope
    EmptyCartContract.Presenter provideShipmentPresenter(GetCartListUseCase getCartListUseCase,
                                                         GetWishlistUseCase getWishlistUseCase,
                                                         GetRecentViewUseCase getRecentViewUseCase,
                                                         CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                                                         CartApiRequestParamGenerator cartApiRequestParamGenerator,
                                                         CompositeSubscription compositeSubscription,
                                                         UserSessionInterface userSessionInterface) {
        return new EmptyCartPresenter(getCartListUseCase, getWishlistUseCase, getRecentViewUseCase,
                cancelAutoApplyCouponUseCase, cartApiRequestParamGenerator, compositeSubscription,
                userSessionInterface);
    }

}
