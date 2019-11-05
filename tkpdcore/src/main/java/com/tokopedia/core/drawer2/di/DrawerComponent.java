package com.tokopedia.core.drawer2.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.drawer2.service.DrawerGetNotificationService;

import dagger.Component;

@DrawerScope
@Component(modules = DrawerModule.class, dependencies = AppComponent.class)
public interface DrawerComponent {
    void inject(DrawerGetNotificationService drawerGetNotificationService);
}
