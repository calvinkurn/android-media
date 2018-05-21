package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.domain.usecase.GetRatesUseCase;
import com.tokopedia.checkout.view.adapter.CourierChoiceAdapter;
import com.tokopedia.checkout.view.di.scope.ShipmentDetailScope;
import com.tokopedia.checkout.view.view.shippingoptions_old.IShipmentDetailPresenter;
import com.tokopedia.checkout.view.view.shippingoptions_old.ShipmentDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */
@Module
public class ShipmentDetailModule {

    private static final int RETRY_COUNT = 0;

    public ShipmentDetailModule() {
    }

    @Provides
    @ShipmentDetailScope
    IShipmentDetailPresenter provideShipmentDetailPresenter(GetRatesUseCase getRatesUseCase) {
        return new ShipmentDetailPresenter(getRatesUseCase);
    }

    @Provides
    @ShipmentDetailScope
    CourierChoiceAdapter provideCourierChoiceAdapter() {
        return new CourierChoiceAdapter();
    }
}
