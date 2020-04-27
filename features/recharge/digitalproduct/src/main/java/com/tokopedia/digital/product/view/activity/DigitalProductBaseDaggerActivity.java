package com.tokopedia.digital.product.view.activity;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital.product.di.DigitalProductComponent;
import com.tokopedia.digital.product.di.DigitalProductComponentInstance;

/**
 * @author by furqan on 25/06/18.
 */

public abstract class DigitalProductBaseDaggerActivity extends BaseSimpleActivity {

    private DigitalProductComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    private void initInjector() {
        getDigitalProductComponent().inject(this);
    }

    protected DigitalProductComponent getDigitalProductComponent() {
        if (component == null) {
            component = DigitalProductComponentInstance.getDigitalProductComponent(getApplication());
        }
        return component;
    }

}
