package com.tokopedia.oms.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;

import dagger.Component;

@OmsScope
@Component(modules = OmsModule.class, dependencies = BaseAppComponent.class)
public interface OmsComponent {
    PostVerifyCartUseCase getPostVerifyCartUseCase();
}
