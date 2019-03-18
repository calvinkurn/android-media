package com.tokopedia.withdraw.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

/**
 * @author by StevenFredian on 30/07/18.
 */

@WithdrawScope
@Module
public class WithdrawModule {

    @WithdrawScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
