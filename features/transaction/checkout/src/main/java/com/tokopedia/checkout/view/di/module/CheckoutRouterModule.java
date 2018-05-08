package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.router.ICartCheckoutModuleRouter;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 02/05/18.
 */
@Module
public class CheckoutRouterModule {

    @Provides
    ICartCheckoutModuleRouter iCartCheckoutModuleRouter(@ApplicationContext Context context) {
        if (context instanceof ICartCheckoutModuleRouter) {
            return (ICartCheckoutModuleRouter) context;
        }
        return null;
    }
}
