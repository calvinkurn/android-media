package com.tokopedia.app.common.di;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.module.AppModule;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.app.common.MainApplication;
import com.tokopedia.core.base.di.module.UtilModule;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.gcm.GCMHandler;

import dagger.Component;

@ApplicationScope // [Misael] ada ApplicationScope lagi yang dari core ^
@Component(
        modules = {
                UtilModule.class
        },
        dependencies = {
                BaseAppComponent.class
        }
)
public interface CommonAppComponent {

    // [Misael] kita ngga bisa pakai object yg di tulis di BaseAppComponent aja apa ya?
    @ApplicationContext
    Context context();

    Gson gson();

    GCMHandler gcmHandler();

    void inject(MainApplication mainApplication);

    // [Misael] kita coba langsung provide ke masing" component (OmsComponent)
//    ChuckerInterceptor ChuckerInterceptor();
}
