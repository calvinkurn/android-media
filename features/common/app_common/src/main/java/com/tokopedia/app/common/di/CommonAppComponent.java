package com.tokopedia.app.common.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.app.common.scope.ApplicationScope;
import com.tokopedia.app.common.MainApplication;

import dagger.Component;

@ApplicationScope
@Component(
        dependencies = {
                BaseAppComponent.class
        }
)
public interface CommonAppComponent {

    @ApplicationContext
    Context context();

    Gson gson();

    void inject(MainApplication mainApplication);

}
