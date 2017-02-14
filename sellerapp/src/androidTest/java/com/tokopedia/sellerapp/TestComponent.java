package com.tokopedia.sellerapp;

import com.tokopedia.sellerapp.activities.MockGooglePlayServiceModule;
import com.tokopedia.sellerapp.activities.gmstat.GMStatTest;
import com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest;
import com.tokopedia.sellerapp.daggerModules.AppModule;
import com.tokopedia.sellerapp.daggerModules.NetworkModules;
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
        TestUtilModules.class,
        MockGooglePlayServiceModule.class,
        GMStatModules.class
})
public interface TestComponent extends BaseComponent {
    void inject(SellerHomeTest sellerHomeTest);
    void inject(GMStatTest gmStatTest);
}
