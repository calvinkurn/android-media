package com.tokopedia.fcmcommon.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.FirebaseMessagingManagerImpl
import com.tokopedia.fcmcommon.R
import com.tokopedia.fcmcommon.data.UpdateFcmTokenResponse
import com.tokopedia.fcmcommon.domain.UpdateFcmTokenUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as loadRaw

@Module
class FcmModule(@ApplicationContext private val context: Context) {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context = context.applicationContext

    @Provides
    @FcmScope
    fun provideFcmManager(
            updateFcmTokenUseCase: UpdateFcmTokenUseCase,
            sharedPreferences: SharedPreferences,
            userSession: UserSessionInterface
    ): FirebaseMessagingManager {
        return FirebaseMessagingManagerImpl(
                updateFcmTokenUseCase,
                sharedPreferences,
                userSession
        )
    }

    @Provides
    @FcmScope
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @FcmScope
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @FcmScope
    fun provideGraphqlUseCase(repository: GraphqlRepository): GraphqlUseCase<UpdateFcmTokenResponse> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @FcmScope
    fun provideFcmTokenUseCase(
            useCase: GraphqlUseCase<UpdateFcmTokenResponse>
    ): UpdateFcmTokenUseCase {
        val query = loadRaw(context.resources, R.raw.query_update_fcm_token)
        return UpdateFcmTokenUseCase(useCase, query)
    }

    @Provides
    @FcmScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

}
