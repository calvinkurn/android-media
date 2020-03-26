package com.tokopedia.product.addedit.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.common.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.common.domain.usecase.ProductAddUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by faisalramd on 2020-03-20.
 */
@Module
class ProductAddUseCaseModule {

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @Provides
    @AddProductQualifier
    fun provideProductAddUseCase(
            graphqlRepository: GraphqlRepository
    ): ProductAddUseCase {
        return ProductAddUseCase(graphqlRepository)
    }

    @Provides
    @AddProductQualifier
    fun provideAddProductInputMapper(): AddProductInputMapper {
        return AddProductInputMapper()
    }
}