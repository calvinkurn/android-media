package com.tokopedia.digital.product.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.digital.product.view.activity.DigitalProductBaseDaggerActivity;

import dagger.Component;

/**
 * @author by furqan on 25/06/18.
 */

@DigitalProductScope
@Component (modules = DigitalProductModule.class, dependencies = BaseAppComponent.class)
public interface DigitalProductComponent {

    @ApplicationContext Context context();

    void inject(DigitalProductBaseDaggerActivity digitalProductBaseDaggerActivity);

}