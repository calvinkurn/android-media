package com.tokopedia.instantloan.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.instantloan.di.module.InstantLoanModule;
import com.tokopedia.instantloan.di.scope.InstantLoanScope;
import com.tokopedia.instantloan.view.activity.InstantLoanActivity;

import dagger.Component;

/**
 * Created by lavekush on 19/03/18.
 */

@InstantLoanScope
@Component(modules = InstantLoanModule.class, dependencies = BaseAppComponent.class)
public interface InstantLoanComponent {

    @ApplicationContext
    Context context();

    void inject(InstantLoanActivity instantLoanActivity);
}

