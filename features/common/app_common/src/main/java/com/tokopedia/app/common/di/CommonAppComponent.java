package com.tokopedia.app.common.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.app.common.MainApplication;
import com.tokopedia.core.base.di.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope // [Misael] mungkin nnti ini perlu bikin scope baru, atau pindahin aja
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

    // [Misael] kita coba langsung provide ke masing" component (OmsComponent)
//    ChuckerInterceptor ChuckerInterceptor();
}
