package com.tokopedia.sellerorder.orderextension.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class SomOrderExtensionModule {
    @SomOrderExtensionRequestScope
    @Provides
    fun getUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SomOrderExtensionRequestScope
    @Provides
    fun getGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}
