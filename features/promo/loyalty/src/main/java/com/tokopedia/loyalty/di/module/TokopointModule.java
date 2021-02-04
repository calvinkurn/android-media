package com.tokopedia.loyalty.di.module;

import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.domain.usecase.GetTokopointUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by meta on 17/07/18.
 */
@Module(includes = {ServiceApiModule.class})
public class TokopointModule {

    @Provides
    GetTokopointUseCase getTokopointUseCase(ITokoPointRepository repository) {
        return new GetTokopointUseCase(repository);
    }
}
