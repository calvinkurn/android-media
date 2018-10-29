package com.tokopedia.changephonenumber.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.changephonenumber.analytics.ChangePhoneNumberAnalytics;
import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.user.session.UserSession;

import dagger.Component;

/**
 * @author by alvinatin on 25/09/18.
 */

@ChangePhoneNumberScope
@Component(modules = ChangePhoneNumberModule.class, dependencies = BaseAppComponent.class)
public interface ChangePhoneNumberComponent {

    UserSession provideUserSession();

    ChangePhoneNumberRepository provideRepository();

    ChangePhoneNumberAnalytics provideAnalytics();

    @ApplicationContext
    Context provideContext();
}
