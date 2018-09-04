package com.tokopedia.mitra.common.di;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;

import dagger.Component;

@MitraScope
@Component(modules = MitraModule.class, dependencies = BaseAppComponent.class)
public interface MitraComponent {
    UserSession userSession();
}
