package com.tokopedia.tkpd.flight.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.di.SessionComponent;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.shop.open.data.model.UploadShopImageModel;
import com.tokopedia.tkpd.flight.FlightGetProfileInfoData;
import com.tokopedia.tkpd.flight.di.module.FlightConsumerModule;

import dagger.Component;

/**
 * @author by alvarisi on 1/24/18.
 */
@FlightConsumerScope
@Component(modules = FlightConsumerModule.class, dependencies = AppComponent.class)
public interface FlightConsumerComponent {
    UploadImageUseCase<UploadShopImageModel> uploadImageUseCase();

    void inject(FlightGetProfileInfoData flightGetProfileInfoData);
}
