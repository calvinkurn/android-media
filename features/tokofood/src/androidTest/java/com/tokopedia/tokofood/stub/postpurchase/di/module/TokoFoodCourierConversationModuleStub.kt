package com.tokopedia.tokofood.stub.postpurchase.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.database.ConversationsDatabase
import com.gojek.courier.CourierConnection
import com.gojek.courier.config.CourierRemoteConfig
import com.google.gson.Gson
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.remote_config.TokoChatCourierRemoteConfigImpl
import com.tokochat.tokochat_config_common.repository.courier.TokoChatCourierClientProvider
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokofood.feature.ordertracking.di.scope.TokoFoodOrderTrackingScope
import com.tokopedia.tokofood.stub.common.util.BabbleCourierClientStub
import com.tokopedia.tokofood.stub.common.util.ConversationsPreferencesStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class TokoFoodCourierConversationModuleStub(private val application: Application) {

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideTokoChatCourierRemoteConfig(
        @TokoChatQualifier remoteConfig: RemoteConfig
    ): CourierRemoteConfig {
        return TokoChatCourierRemoteConfigImpl(remoteConfig)
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideTokoChatCourierConnection(
        @TokoChatQualifier context: Context,
        @TokoChatQualifier gson: Gson,
        @TokoChatQualifier retrofit: Retrofit,
        @TokoChatQualifier userSession: UserSessionInterface,
        @TokoChatQualifier courierRemoteConfig: CourierRemoteConfig
    ): CourierConnection {
        val provider = TokoChatCourierClientProvider(
            context,
            gson,
            retrofit,
            userSession,
            courierRemoteConfig
        )
        return provider.getCourierConnection()
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideTokoChatBabbleCourier(): BabbleCourierClient {
        return BabbleCourierClientStub()
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideConversationsPreferences(
        @ApplicationContext context: Context
    ): ConversationsPreferencesStub {
        return ConversationsPreferencesStub(context)
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideDatabase(
        @TokoFoodOrderTrackingScope preferences: ConversationsPreferencesStub
    ): ConversationsDatabase {
        return Room.databaseBuilder(
            application,
            ConversationsDatabase::class.java,
            "conversations-database"
        ).fallbackToDestructiveMigration()
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        preferences.previousChannelsFetched = false
                    }
                }
            )
            .allowMainThreadQueries()
            .build()
    }
}
