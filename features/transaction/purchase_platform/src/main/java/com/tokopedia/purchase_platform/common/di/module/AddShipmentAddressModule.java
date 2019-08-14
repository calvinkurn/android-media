package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.purchase_platform.common.di.scope.AddShipmentAddressScope;
import com.tokopedia.purchase_platform.checkout.subfeature.multiple_address.view.AddShipmentAddressPresenter;
import com.tokopedia.purchase_platform.checkout.subfeature.multiple_address.view.IAddShipmentAddressPresenter;

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
