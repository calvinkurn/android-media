package com.tokopedia.digital.product.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.digital.common.di.DigitalComponent;
import com.tokopedia.digital.product.view.activity.DigitalProductBaseDaggerActivity;
import com.tokopedia.digital.product.view.fragment.DigitalChooserOperatorFragment;
import com.tokopedia.digital.product.view.fragment.DigitalChooserProductFragment;
import com.tokopedia.digital.product.view.fragment.DigitalProductFragment;

import dagger.Component;

/**
 * @author by furqan on 25/06/18.
 */

@DigitalProductScope
@Component(modules = DigitalProductModule.class, dependencies = DigitalComponent.class)
public interface DigitalProductComponent {

    @ApplicationContext
    Context context();

    void inject(DigitalProductBaseDaggerActivity digitalProductBaseDaggerActivity);

    void inject(DigitalProductFragment digitalProductFragment);

    void inject(DigitalChooserOperatorFragment digitalChooserOperatorFragment);

    void inject(DigitalChooserProductFragment digitalChooserProductFragment);
}