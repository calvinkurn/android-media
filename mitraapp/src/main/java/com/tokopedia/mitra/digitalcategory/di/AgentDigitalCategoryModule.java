package com.tokopedia.mitra.digitalcategory.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.mitra.digitalcategory.domain.usecase.AgentDigitalCategoryUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rizky on 30/08/18.
 */
@Module
public class AgentDigitalCategoryModule {

    @Provides
    @AgentDigitalCategoryScope
    AgentDigitalCategoryUseCase provideAgentDigitalCategoryUseCase(@ApplicationContext  Context context,
                                                                   GraphqlUseCase graphqlUseCase) {
        return new AgentDigitalCategoryUseCase(context, graphqlUseCase);
    }

}
