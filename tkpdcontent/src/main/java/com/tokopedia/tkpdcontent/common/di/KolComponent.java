package com.tokopedia.tkpdcontent.common.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;

import dagger.Component;

/**
 * @author by milhamj on 06/02/18.
 */

@KolScope
@Component(modules = KolModule.class, dependencies = BaseAppComponent.class)
public interface KolComponent {
}
