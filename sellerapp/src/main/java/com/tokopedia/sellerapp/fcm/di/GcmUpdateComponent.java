package com.tokopedia.sellerapp.fcm.di;

import com.tokopedia.fcmcommon.di.FcmComponent;
import com.tokopedia.sellerapp.SellerRouterApplication;

import dagger.Component;

@GcmUpdateScope
@Component(dependencies = FcmComponent.class)
public interface GcmUpdateComponent {
    void inject(SellerRouterApplication application);
}