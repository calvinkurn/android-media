package com.tokopedia.managename.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.managename.constant.ManageNameConstants
import com.tokopedia.managename.data.model.UpdateNameResponse
import com.tokopedia.managename.di.scope.ManageNameContext
import com.tokopedia.managename.domain.AddNameUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ManageNameModule(val context: Context) {

    @Provides
    @ManageNameContext
    fun provideNotificationContext(): Context {
        return context
    }

    @Provides
    @ManageNameScope
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @Provides
    @ManageNameScope
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @ManageNameScope
    fun provideAddNameUseCase(
            @Named(ManageNameConstants.Query.UPDATE_NAME_QUERY)
            query: String,
            useCase: GraphqlUseCase<UpdateNameResponse>
    ): AddNameUseCase {
        return AddNameUseCase(query, useCase)
    }

}
