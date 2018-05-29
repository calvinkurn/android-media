package com.tokopedia.core.drawer2.data.factory;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.source.CloudAttrDataSource;
import com.tokopedia.core.network.apiservices.drawer.DrawerService;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public class UserAttributesFactory {

    private final LocalCacheHandler drawerCache;
    private DrawerService drawerService;

    public UserAttributesFactory(DrawerService drawerService, LocalCacheHandler drawerCache) {
        this.drawerService = drawerService;
        this.drawerCache = drawerCache;
    }

    public CloudAttrDataSource createCloudAttrDataSource() {
        return new CloudAttrDataSource(drawerService, drawerCache);
    }
}
