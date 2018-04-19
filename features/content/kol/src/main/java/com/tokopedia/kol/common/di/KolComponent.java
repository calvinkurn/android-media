package com.tokopedia.kol.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.kol.common.data.source.api.KolApi;

import dagger.Component;

/**
 * @author by milhamj on 06/02/18.
 */

@KolScope
@Component(modules = KolModule.class, dependencies = BaseAppComponent.class)
public interface KolComponent {
    @ApplicationContext
    Context context();

    KolApi kolApi();

    UserSession userSession();
}
