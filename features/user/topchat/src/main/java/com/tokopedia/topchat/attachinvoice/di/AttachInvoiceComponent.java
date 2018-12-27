package com.tokopedia.topchat.attachinvoice.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import dagger.Component;

/**
 * Created by Hendri on 05/04/18.
 */

@AttachInvoiceScope
@Component(modules = {AttachInvoiceModule.class}, dependencies = BaseAppComponent.class)
public interface AttachInvoiceComponent {

    @ApplicationContext
    Context context();
}
