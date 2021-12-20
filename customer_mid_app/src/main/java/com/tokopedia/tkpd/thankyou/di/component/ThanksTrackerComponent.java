package com.tokopedia.tkpd.thankyou.di.component;

import com.tokopedia.app.common.di.CommonAppComponent;
import com.tokopedia.tkpd.thankyou.di.module.ThanksTrackerModule;
import com.tokopedia.tkpd.thankyou.di.scope.ThanksTrackerScope;
import com.tokopedia.tkpd.thankyou.view.ThanksTrackerService;

import dagger.Component;

/**
 * Created by okasurya on 12/4/17.
 */
// [Misael] Check apakah masih ada instance yang dipakai disini tapi belum di provide sama CommonAppComponent
@ThanksTrackerScope
@Component(modules = ThanksTrackerModule.class, dependencies = CommonAppComponent.class)
public interface ThanksTrackerComponent {
    void inject(ThanksTrackerService service);
}
