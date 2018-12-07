package com.tokopedia.checkout.view.di.module;

import com.tokopedia.transaction.common.data.shipping.RecipientAddressModel;
import com.tokopedia.checkout.view.di.scope.AddShipmentAddressScope;
import com.tokopedia.checkout.view.feature.multipleaddressform.AddShipmentAddressPresenter;
import com.tokopedia.checkout.view.feature.multipleaddressform.IAddShipmentAddressPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

@Module(includes = TrackingAnalyticsModule.class)
public class AddShipmentAddressModule {

    public AddShipmentAddressModule() {
    }

    @Provides
    @AddShipmentAddressScope
    RecipientAddressModel provideAddressEditableModel() {
        return new RecipientAddressModel();
    }


    @Provides
    @AddShipmentAddressScope
    IAddShipmentAddressPresenter providePresenter(@AddShipmentAddressScope RecipientAddressModel recipientAddressModel) {
        return new AddShipmentAddressPresenter(recipientAddressModel);
    }

}
