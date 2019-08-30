package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.purchase_platform.features.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.usecase.GetCartMultipleAddressListUseCase;
import com.tokopedia.purchase_platform.common.di.scope.MultipleAddressScope;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.IMultipleAddressPresenter;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.IMultipleAddressView;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.MultipleAddressPresenter;
import com.tokopedia.purchase_platform.common.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.common.utils.CartApiRequestParamGenerator;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@Module(includes = TrackingAnalyticsModule.class)
public class MultipleAddressModule {

    private final IMultipleAddressView view;

    public MultipleAddressModule(IMultipleAddressView view) {
        this.view = view;
    }

    @MultipleAddressScope
    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

//    @MultipleAddressScope
//    @Provides
//    ChangeShippingAddressUseCase provideMultipleAddressUseCase(ICartRepository repository) {
//        return new ChangeShippingAddressUseCase(repository);
//    }

    @MultipleAddressScope
    @Provides
    UserSessionInterface provideUserSessionInterface() {
        return new UserSession(view.getActivityContext());
    }

    @MultipleAddressScope
    @Provides
    IMultipleAddressPresenter providePresenter(ChangeShippingAddressUseCase changeShippingAddressUseCase,
                                               GetCartMultipleAddressListUseCase getCartMultipleAddressListUseCase,
                                               CartApiRequestParamGenerator cartApiRequestParamGenerator,
                                               UserSessionInterface userSessionInterface) {
        return new MultipleAddressPresenter(getCartMultipleAddressListUseCase,
                changeShippingAddressUseCase, cartApiRequestParamGenerator, userSessionInterface);
    }
}
