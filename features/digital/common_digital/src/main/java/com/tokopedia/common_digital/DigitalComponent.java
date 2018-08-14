package com.tokopedia.common_digital;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import dagger.Component;

/**
 * Created by Rizky on 13/08/18.
 */
@DigitalScope
@Component(modules = DigitalModule.class, dependencies = BaseAppComponent.class)
public interface DigitalComponent {

    @ApplicationContext
    Context context();

//    void inject(DigitalProductBaseDaggerActivity digitalProductBaseDaggerActivity);

}

