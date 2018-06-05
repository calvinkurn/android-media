package com.tokopedia.checkout.view.di.module;

import android.app.Activity;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.checkout.view.di.scope.CartAddressChoiceScope;
import com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceFragment;
import com.tokopedia.checkout.view.view.addressoptions.CartAddressChoicePresenter;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCartPage;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {PeopleAddressModule.class})
public class CartAddressChoiceModule {

    private final ShipmentAddressListAdapter.ActionListener actionListener;

    private final Activity activity;

    public CartAddressChoiceModule(Activity activity, CartAddressChoiceFragment cartAddressChoiceFragment) {
        actionListener = cartAddressChoiceFragment;
        this.activity = activity;
    }

    @Provides
    @CartAddressChoiceScope
    CartAddressChoicePresenter provideCartAddressChoicePresenter(GetPeopleAddressUseCase getDefaultAddressUseCase) {
        return new CartAddressChoicePresenter(getDefaultAddressUseCase);
    }

    @Provides
    @CartAddressChoiceScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }

    @Provides
    @CartAddressChoiceScope
    GetPeopleAddressUseCase provideGetAddressListUseCase(PeopleAddressRepository peopleAddressRepository) {
        return new GetPeopleAddressUseCase(peopleAddressRepository);
    }

    @Provides
    @CartAddressChoiceScope
    CheckoutAnalyticsCartPage provideCheckoutAnalyticCartPage() {
        AnalyticTracker analyticTracker = null;
        if (activity.getApplication() instanceof AbstractionRouter) {
            analyticTracker = ((AbstractionRouter) activity.getApplication()).getAnalyticTracker();
        }
        return new CheckoutAnalyticsCartPage(analyticTracker);
    }

}