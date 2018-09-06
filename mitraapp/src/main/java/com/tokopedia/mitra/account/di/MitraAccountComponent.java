package com.tokopedia.mitra.account.di;

import com.tokopedia.mitra.account.fragment.MitraAccountFragment;
import com.tokopedia.mitra.common.di.MitraComponent;
import com.tokopedia.mitra.common.di.MitraModule;

import dagger.Component;

@MitraAccountScope
@Component(modules = MitraAccountModule.class, dependencies = MitraComponent.class)
public interface MitraAccountComponent {
    void inject(MitraAccountFragment mitraAccountFragment);
}
