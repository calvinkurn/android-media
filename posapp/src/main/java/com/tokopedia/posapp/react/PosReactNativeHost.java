package com.tokopedia.posapp.react;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.microsoft.codepush.react.ReactInstanceHolder;

/**
 * @author okasurya on 4/10/18.
 */

public abstract class PosReactNativeHost extends ReactNativeHost implements ReactInstanceHolder {
    protected PosReactNativeHost(Application application) {
        super(application);
    }
}
