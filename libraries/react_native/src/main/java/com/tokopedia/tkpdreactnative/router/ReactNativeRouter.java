package com.tokopedia.tkpdreactnative.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.usecase.UseCase;

import rx.Subscriber;

/**
 * Created by kris on 4/24/18. Tokopedia
 */

public interface ReactNativeRouter {

    UseCase<String> setCreditCardSingleAuthentication();

    boolean getEnableFingerprintPayment();

    Intent getBrandsWebViewIntent(Context context, String url);
}
