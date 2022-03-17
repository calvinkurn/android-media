package com.tokopedia.shopadmin.invitationaccepted.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shopadmin.invitationaccepted.di.scope.AdminInvitationAcceptedScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [AdminInvitationAcceptedViewModelModule::class])
class AdminInvitationAcceptedModule {

    @AdminInvitationAcceptedScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @AdminInvitationAcceptedScope
    @Provides
    fun provideUserGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}