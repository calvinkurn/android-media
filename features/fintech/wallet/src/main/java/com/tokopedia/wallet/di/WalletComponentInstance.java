package com.tokopedia.wallet.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;

/**
 * Created by nabillasabbaha on 09/05/18.
 */
public class WalletComponentInstance {

    private static WalletComponent walletComponent;

    public static WalletComponent getComponent(Application application) {
        if (walletComponent == null) {
            walletComponent = DaggerWalletComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application)
                            .getBaseAppComponent())
                    .build();
        }
        return walletComponent;
    }
}
