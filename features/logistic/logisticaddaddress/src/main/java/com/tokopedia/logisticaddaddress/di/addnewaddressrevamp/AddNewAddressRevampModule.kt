package com.tokopedia.logisticaddaddress.di.addnewaddressrevamp

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class AddNewAddressRevampModule {

    @Provides
    @AddNewAddressRevampScope
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @AddNewAddressRevampScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}