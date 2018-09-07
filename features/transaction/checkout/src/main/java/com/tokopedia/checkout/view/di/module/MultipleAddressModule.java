package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartMultipleAddressListUseCase;
import com.tokopedia.checkout.view.di.scope.MultipleAddressScope;
import com.tokopedia.checkout.view.feature.multipleaddressform.IMultipleAddressPresenter;
import com.tokopedia.checkout.view.feature.multipleaddressform.IMultipleAddressView;
import com.tokopedia.checkout.view.feature.multipleaddressform.MultipleAddressPresenter;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;

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

    @MultipleAddressScope
    @Provides
    ChangeShippingAddressUseCase provideMultipleAddressUseCase(ICartRepository repository) {
        return new ChangeShippingAddressUseCase(repository);
    }

    @MultipleAddressScope
    @Provides
    IMultipleAddressPresenter providePresenter(ChangeShippingAddressUseCase changeShippingAddressUseCase,
                                               GetCartMultipleAddressListUseCase getCartMultipleAddressListUseCase,
                                               CartApiRequestParamGenerator cartApiRequestParamGenerator) {
        return new MultipleAddressPresenter(view, getCartMultipleAddressListUseCase, changeShippingAddressUseCase, cartApiRequestParamGenerator);
    }
}
