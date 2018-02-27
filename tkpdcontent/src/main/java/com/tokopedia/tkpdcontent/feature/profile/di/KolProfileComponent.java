package com.tokopedia.tkpdcontent.feature.profile.di;

import com.tokopedia.tkpdcontent.common.di.KolComponent;
import com.tokopedia.tkpdcontent.feature.profile.view.fragment.KolPostFragment;

import dagger.Component;

/**
 * @author by milhamj on 12/02/18.
 */

@KolProfileScope
@Component(modules = KolProfileModule.class, dependencies = KolComponent.class)
public interface KolProfileComponent {
    void inject(KolPostFragment kolPostFragment);
}
