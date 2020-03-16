package com.tokopedia.qrscanner;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.qrscanner.branchio.BranchIOModule;

import dagger.Component;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@QRModuleScope
@Component(modules = {QRModule.class, BranchIOModule.class}, dependencies = {BaseAppComponent.class})
public interface QRComponent {

    void inject(QrScannerActivity activity);
}