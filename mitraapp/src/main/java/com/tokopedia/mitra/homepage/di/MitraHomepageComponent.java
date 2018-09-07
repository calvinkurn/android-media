package com.tokopedia.mitra.homepage.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.mitra.common.di.MitraComponent;
import com.tokopedia.mitra.homepage.activity.MitraParentHomepageActivity;
import com.tokopedia.mitra.homepage.fragment.MitraHomepageFragment;

import dagger.Component;

@MitraHomepageScope
@Component(modules = MitraHomepageModule.class, dependencies = MitraComponent.class)
public interface MitraHomepageComponent {
    void inject(MitraHomepageFragment fragment);

    void inject(MitraParentHomepageActivity mitraParentHomepageActivity);
}
