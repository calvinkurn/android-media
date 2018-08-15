package com.tokopedia.common_digital.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common_digital.common.data.api.DigitalApi;
import com.tokopedia.common_digital.product.domain.usecase.DigitalGetHelpUrlUseCase;
import com.tokopedia.common_digital.product.domain.usecase.GetCategoryByIdUseCase;

import dagger.Component;

/**
 * Created by Rizky on 13/08/18.
 */
@DigitalScope
@Component(modules = DigitalModule.class, dependencies = BaseAppComponent.class)
public interface DigitalComponent {

    @ApplicationContext
    Context context();

    DigitalApi digitalApi();

    GetCategoryByIdUseCase getCategoryByIdUseCase();

    DigitalGetHelpUrlUseCase digitalGetHelpUrlUseCase();

//    void inject(DigitalProductBaseDaggerActivity digitalProductBaseDaggerActivity);

}

