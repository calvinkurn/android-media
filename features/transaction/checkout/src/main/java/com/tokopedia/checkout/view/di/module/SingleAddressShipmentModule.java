package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.checkout.view.di.scope.SingleAddressShipmentScope;
import com.tokopedia.checkout.view.mapper.ShipmentDataRequestConverter;
import com.tokopedia.checkout.view.view.shipmentform.ICartSingleAddressView;
import com.tokopedia.checkout.view.view.shipmentform.SingleAddressShipmentFragment;
import com.tokopedia.checkout.view.view.shipmentform.SingleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {ConverterDataModule.class, PeopleAddressModule.class})
public class SingleAddressShipmentModule {

    private SingleAddressShipmentAdapter.ActionListener adapterActionListener;
    private ICartSingleAddressView viewListener;

    public SingleAddressShipmentModule(SingleAddressShipmentFragment singleAddressShipmentFragment) {
        adapterActionListener = singleAddressShipmentFragment;
        viewListener = singleAddressShipmentFragment;
    }

    @Provides
    @SingleAddressShipmentScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @SingleAddressShipmentScope
    SingleAddressShipmentPresenter provideCartSingleAddressPresenter(
            CompositeSubscription compositeSubscription,
            GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
            CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase
    ) {
        return new SingleAddressShipmentPresenter(viewListener, compositeSubscription,
                getShipmentAddressFormUseCase, checkPromoCodeCartListUseCase);
    }

    @Provides
    @SingleAddressShipmentScope
    ShipmentDataRequestConverter provideShipmentDataRequestConverter() {
        return new ShipmentDataRequestConverter();
    }

    @Provides
    @SingleAddressShipmentScope
    SingleAddressShipmentAdapter provideCartSingleAddressAdapter
            (ShipmentDataRequestConverter shipmentDataRequestConverter) {
        return new SingleAddressShipmentAdapter(adapterActionListener, shipmentDataRequestConverter);
    }

}