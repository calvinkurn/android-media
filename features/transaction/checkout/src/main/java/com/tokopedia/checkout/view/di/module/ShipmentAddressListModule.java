package com.tokopedia.checkout.view.di.module;

import android.app.Activity;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.checkout.view.di.scope.CartAddressChoiceScope;
import com.tokopedia.checkout.view.di.scope.ShipmentAddressListScope;
import com.tokopedia.checkout.view.utils.PagingHandler;
import com.tokopedia.checkout.view.view.addressoptions.ShipmentAddressListFragment;
import com.tokopedia.checkout.view.view.addressoptions.ShipmentAddressListPresenter;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCartPage;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {PeopleAddressModule.class})
public class ShipmentAddressListModule {

    private final ShipmentAddressListAdapter.ActionListener actionListener;
    private final Activity activity;

    public ShipmentAddressListModule(Activity activity, ShipmentAddressListFragment shipmentAddressListFragment) {
        actionListener = shipmentAddressListFragment;
        this.activity = activity;
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListPresenter provideCartAddressListPresenter(GetPeopleAddressUseCase getPeopleAddressUseCase, PagingHandler pagingHandler) {
        return new ShipmentAddressListPresenter(getPeopleAddressUseCase, pagingHandler);
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }

    @Provides
    @ShipmentAddressListScope
    GetPeopleAddressUseCase provideGetAddressListUseCase(PeopleAddressRepository peopleAddressRepository) {
        return new GetPeopleAddressUseCase(peopleAddressRepository);
    }

    @Provides
    @ShipmentAddressListScope
    CheckoutAnalyticsCartPage provideCheckoutAnalyticCartPage() {
        AnalyticTracker analyticTracker = null;
        if (activity.getApplication() instanceof AbstractionRouter) {
            analyticTracker = ((AbstractionRouter) activity.getApplication()).getAnalyticTracker();
        }
        return new CheckoutAnalyticsCartPage(analyticTracker);
    }

}