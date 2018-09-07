package com.tokopedia.mitra.homepage.di;

import com.tokopedia.mitra.common.di.MitraComponent;
import com.tokopedia.mitra.homepage.fragment.MitraHomepageFragment;

import dagger.Component;

@MitraHomepageScope
@Component(modules = MitraHomepageModule.class, dependencies = MitraComponent.class)
public interface TestMitraHomepageComponent extends MitraHomepageComponent {
    void inject(MitraHomepageFragment fragment);
}
