package com.tokopedia.tkpdcontent.feature.profile.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpdcontent.common.di.KolComponent;
import com.tokopedia.tkpdcontent.common.di.KolModule;

import dagger.Component;

/**
 * @author by milhamj on 12/02/18.
 */

@KolProfileScope
@Component(modules = KolProfileModule.class, dependencies = KolComponent.class)
public interface KolProfileComponent {
}
