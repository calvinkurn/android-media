package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.view.di.scope.MultipleAddressShipmentScope;
import com.tokopedia.checkout.view.view.shipmentform.IMultipleAddressShipmentPresenter;
import com.tokopedia.checkout.view.view.shipmentform.IMultipleAddressShipmentView;
import com.tokopedia.checkout.view.view.shipmentform.MultipleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@Module
public class MultipleAddressShipmentModule {

    private IMultipleAddressShipmentView view;

    public MultipleAddressShipmentModule(IMultipleAddressShipmentView view) {
        this.view = view;
    }

    @Provides
    @MultipleAddressShipmentScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @MultipleAddressShipmentScope
    @Provides
    IMultipleAddressShipmentPresenter providePresenter(CompositeSubscription compositeSubscription,
                                                       GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                                                       CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase) {
        return new MultipleAddressShipmentPresenter(view, compositeSubscription,
                getShipmentAddressFormUseCase, checkPromoCodeCartListUseCase);
    }

}
