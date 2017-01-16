package com.tokopedia.sellerapp;

import com.tokopedia.sellerapp.daggerModules.AppModule;
import com.tokopedia.sellerapp.daggerModules.NetworkModules;
import com.tokopedia.sellerapp.daggerModules.UtilModules;
import com.tokopedia.sellerapp.gmstat.modules.GMStatModules;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by normansyahputa on 8/26/16.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModules.class,
        UtilModules.class,
        GMStatModules.class
})
public interface ApplicationComponent extends BaseComponent{
}
