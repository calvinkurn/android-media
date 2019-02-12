package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.domain.usecase.GetAddressWithCornerUseCase;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.checkout.view.di.scope.ShipmentAddressListScope;
import com.tokopedia.checkout.view.feature.addressoptions.adapter.ShipmentAddressListAdapter;
import com.tokopedia.checkout.view.feature.addressoptions.ShipmentAddressListFragment;
import com.tokopedia.checkout.view.feature.addressoptions.ShipmentAddressListPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {TrackingAnalyticsModule.class})
public class ShipmentAddressListModule {

    private final ShipmentAddressListAdapter.ActionListener actionListener;
    private Context context;

    public ShipmentAddressListModule(Context context, ShipmentAddressListFragment shipmentAddressListFragment) {
        this.context = context;
        actionListener = shipmentAddressListFragment;
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListPresenter provideCartAddressListPresenter(GetPeopleAddressUseCase getPeopleAddressUseCase) {
        return new ShipmentAddressListPresenter(getPeopleAddressUseCase);
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }

    @Provides
    @ShipmentAddressListScope
    UserSessionInterface provideUserSessionInterface() {
        return new UserSession(context);
    }

    @Provides
    @ShipmentAddressListScope
    GetPeopleAddressUseCase provideGetAddressListUseCase(PeopleAddressRepository peopleAddressRepository,
                                                         UserSessionInterface userSessionInterface) {
        return new GetPeopleAddressUseCase(peopleAddressRepository, userSessionInterface);
    }

}