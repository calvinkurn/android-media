package com.tokopedia.purchase_platform.common.di.module;

import android.content.Context;

import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.usecase.GetCornerList;
import com.tokopedia.purchase_platform.common.di.scope.ShipmentAddressListScope;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.AddressListContract;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.AddressListPresenter;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.ShipmentAddressListFragment;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.recyclerview.ShipmentAddressListAdapter;
import com.tokopedia.purchase_platform.features.checkout.subfeature.corner_list.CornerListPresenter;
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
    UserSessionInterface provideUserSessionInterface() {
        return new UserSession(context);
    }

}