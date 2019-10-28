package com.tokopedia.tkpdreactnative.router;

import com.tokopedia.usecase.UseCase;

/**
 * Created by kris on 4/24/18. Tokopedia
 */

public interface ReactNativeRouter {

    UseCase<String> setCreditCardSingleAuthentication();

    boolean getEnableFingerprintPayment();

    void sendLoginEmitter(String userId);
}
