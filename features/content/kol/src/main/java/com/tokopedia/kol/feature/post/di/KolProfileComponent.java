package com.tokopedia.kol.feature.post.di;

import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.kol.feature.post.view.fragment.KolPostFragment;

import dagger.Component;

/**
 * @author by milhamj on 12/02/18.
 */

@KolProfileScope
@Component(modules = KolProfileModule.class, dependencies = KolComponent.class)
public interface KolProfileComponent {
    void inject(KolPostFragment kolPostFragment);
}
