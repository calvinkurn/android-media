package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.domain.usecase.GetCornerList;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.checkout.view.di.scope.ShipmentAddressListScope;
import com.tokopedia.checkout.view.feature.addressoptions.AddressListContract;
import com.tokopedia.checkout.view.feature.addressoptions.AddressListPresenter;
import com.tokopedia.checkout.view.feature.addressoptions.ShipmentAddressListFragment;
import com.tokopedia.checkout.view.feature.addressoptions.recyclerview.ShipmentAddressListAdapter;
import com.tokopedia.checkout.view.feature.cornerlist.CornerListPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {TrackingAnalyticsModule.class})
public class ShipmentAddressListModule {

    private ShipmentAddressListAdapter.ActionListener actionListener;
    private Context context;

    public ShipmentAddressListModule(Context context) {
        this.context = context;
    }

    public ShipmentAddressListModule(Context context, ShipmentAddressListFragment shipmentAddressListFragment) {
        this.context = context;
        actionListener = shipmentAddressListFragment;
    }

    @Provides
    @ShipmentAddressListScope
    AddressListContract.Presenter provideAddressListPresenter(AddressListPresenter presenter) {
        return presenter;
    }

    @Provides
    @ShipmentAddressListScope
    CornerListPresenter provideCornerPresenter(GetCornerList usecase) {
        return new CornerListPresenter(usecase);
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