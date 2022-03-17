package com.tokopedia.shopadmin.invitationconfirmation.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shopadmin.invitationaccepted.di.module.AdminInvitationAcceptedViewModelModule
import com.tokopedia.shopadmin.invitationconfirmation.di.scope.AdminInvitationConfirmationScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [AdminInvitationAcceptedViewModelModule::class])
class AdminInvitationConfirmationModule {

    @AdminInvitationConfirmationScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @AdminInvitationConfirmationScope
    @Provides
    fun provideUserGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}