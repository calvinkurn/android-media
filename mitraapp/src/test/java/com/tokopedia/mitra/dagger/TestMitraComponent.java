package com.tokopedia.mitra.dagger;


import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.mitra.common.di.MitraComponent;
import com.tokopedia.mitra.common.di.MitraModule;
import com.tokopedia.mitra.common.di.MitraScope;

import dagger.Component;

@MitraScope
@Component(modules = MitraModule.class, dependencies = TestBaseAppComponent.class)
public interface TestMitraComponent extends MitraComponent{
}
