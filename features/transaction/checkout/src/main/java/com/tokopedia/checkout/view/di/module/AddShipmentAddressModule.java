package com.tokopedia.checkout.view.di.module;

import android.app.Activity;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.di.scope.AddShipmentAddressScope;
import com.tokopedia.checkout.view.view.multipleaddressform.AddShipmentAddressPresenter;
import com.tokopedia.checkout.view.view.multipleaddressform.IAddShipmentAddressPresenter;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCartPage;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

@Module
public class AddShipmentAddressModule {

    private Activity activity;

    public AddShipmentAddressModule(Activity activity) {
        this.activity = activity;
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

    @Provides
    @AddShipmentAddressScope
    CheckoutAnalyticsCartPage provideCheckoutAnalyticsCartPage() {
        AnalyticTracker analyticTracker = null;
        if (activity.getApplication() instanceof AbstractionRouter) {
            analyticTracker = ((AbstractionRouter) activity.getApplication()).getAnalyticTracker();
        }
        return new CheckoutAnalyticsCartPage(analyticTracker);

    }


}
