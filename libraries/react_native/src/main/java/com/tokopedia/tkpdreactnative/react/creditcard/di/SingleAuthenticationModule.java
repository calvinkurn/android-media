package com.tokopedia.buyerorder.others;

import com.tokopedia.core.network.apiservices.transaction.CreditCardAuthService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.buyerorder.others.creditcardauthentication.UserInfoRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class SingleAuthenticationModule {

    public SingleAuthenticationModule() {

    }

    @Provides
    @SingleAuthenticationScope
    PeopleService providePeopleService() {
        return new PeopleService();
    }

    @Provides
    @SingleAuthenticationScope
    CreditCardAuthService authService() {
        return new CreditCardAuthService();
    }

    @Provides
    @SingleAuthenticationScope
    CreditCardListRepository provideCreditCardListRepository(CreditCardAuthService authService) {
        return new CreditCardListRepository(authService);
    }

    @Provides
    @SingleAuthenticationScope
    UserInfoRepository userInfoRepository(PeopleService peopleService) {
        return new UserInfoRepository(peopleService);
    }
}
