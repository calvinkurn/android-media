package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 02/05/18.
 */
@Module
public class CheckoutRouterModule {

    @Provides
    Context provideContext(@ApplicationContext Context context) {
        return context;
    }

    @Provides
    ICheckoutModuleRouter iCartCheckoutModuleRouter(Context context) {
        if (context instanceof ICheckoutModuleRouter) {
            return (ICheckoutModuleRouter) context;
        }
        return null;
    }
}
