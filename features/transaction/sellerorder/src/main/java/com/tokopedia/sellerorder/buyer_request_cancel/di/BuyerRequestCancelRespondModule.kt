package com.tokopedia.sellerorder.buyer_request_cancel.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class BuyerRequestCancelRespondModule {
    @BuyerRequestCancelRespondScope
    @Provides
    fun getUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @BuyerRequestCancelRespondScope
    @Provides
    fun getGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}
