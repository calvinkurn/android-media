package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.usecase.SubmitMultipleAddressUseCase;
import com.tokopedia.checkout.view.di.scope.MultipleAddressScope;
import com.tokopedia.checkout.view.view.multipleaddressform.IMultipleAddressPresenter;
import com.tokopedia.checkout.view.view.multipleaddressform.IMultipleAddressView;
import com.tokopedia.checkout.view.view.multipleaddressform.MultipleAddressPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@Module
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
    SubmitMultipleAddressUseCase provideMultipleAddressUseCase(ICartRepository repository) {
        return new SubmitMultipleAddressUseCase(repository);
    }

    @MultipleAddressScope
    @Provides
    IMultipleAddressPresenter providePresenter(SubmitMultipleAddressUseCase useCase) {
        return new MultipleAddressPresenter(view, useCase);
    }

}
