package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.view.adapter.ShipmentChoiceAdapter;
import com.tokopedia.checkout.view.di.scope.ShipmentChoiceScope;
import com.tokopedia.checkout.view.view.shippingoptions.IShipmentChoicePresenter;
import com.tokopedia.checkout.view.view.shippingoptions.ShipmentChoicePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */

@Module
public class ShipmentChoiceModule {

    @Provides
    @ShipmentChoiceScope
    ShipmentChoiceAdapter provideShipmentChoiceadapter() {
        return new ShipmentChoiceAdapter();
    }

    @Provides
    @ShipmentChoiceScope
    IShipmentChoicePresenter provideShipmentChoicePresenter() {
        return new ShipmentChoicePresenter();
    }
}
