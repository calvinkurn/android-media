package com.tokopedia.loyalty.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.di.module.TokopointModule;
import com.tokopedia.loyalty.domain.usecase.GetTokopointUseCase;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;

import dagger.Component;

/**
 * Created by meta on 17/07/18.
 */

@LoyaltyScope
@Component(modules = TokopointModule.class , dependencies = BaseAppComponent.class)
public interface TokopointComponent {
    GetTokopointUseCase getTokopointUseCase();
}
