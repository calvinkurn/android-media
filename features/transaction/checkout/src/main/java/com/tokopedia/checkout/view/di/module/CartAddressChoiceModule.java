package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.checkout.view.di.scope.CartAddressChoiceScope;
import com.tokopedia.checkout.view.view.addressoptions.CartAddressChoiceFragment;
import com.tokopedia.checkout.view.view.addressoptions.CartAddressChoicePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {PeopleAddressModule.class})
public class CartAddressChoiceModule {

    private final ShipmentAddressListAdapter.ActionListener actionListener;

    public CartAddressChoiceModule(CartAddressChoiceFragment cartAddressChoiceFragment) {
        actionListener = cartAddressChoiceFragment;
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

}