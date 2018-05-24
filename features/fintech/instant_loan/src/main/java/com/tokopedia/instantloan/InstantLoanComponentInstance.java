package com.tokopedia.instantloan;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.instantloan.di.component.DaggerInstantLoanComponent;
import com.tokopedia.instantloan.di.component.InstantLoanComponent;
import com.tokopedia.instantloan.di.module.InstantLoanModule;

/**
 * Created by lavekush on 20/03/18.
 */

public class InstantLoanComponentInstance {

    private static InstantLoanComponent instantLoanComponent;

    public static InstantLoanComponent get(android.app.Application application) {
        if (instantLoanComponent == null) {
            instantLoanComponent = DaggerInstantLoanComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .instantLoanModule(new InstantLoanModule()).build();
        }
        return instantLoanComponent;
    }
}
