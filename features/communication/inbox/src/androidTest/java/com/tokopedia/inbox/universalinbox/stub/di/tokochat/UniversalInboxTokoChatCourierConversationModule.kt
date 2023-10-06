package com.tokopedia.inbox.universalinbox.stub.di.tokochat

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
import com.tokopedia.inbox.universalinbox.stub.common.BabbleCourierClientStub
import com.tokopedia.inbox.universalinbox.stub.common.ConversationsPreferencesStub
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.remoteconfig.TokoChatCourierRemoteConfigImpl
import com.tokopedia.tokochat.config.repository.courier.TokoChatCourierClientProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UniversalInboxTokoChatCourierConversationModule(private val application: Application) {

    @Provides
    @TokoChatQualifier
    fun provideTokoChatCourierRemoteConfig(
        @TokoChatQualifier remoteConfig: RemoteConfig
    ): CourierRemoteConfig {
        return TokoChatCourierRemoteConfigImpl(remoteConfig)
    }

    @Provides
    @TokoChatQualifier
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
    @TokoChatQualifier
    fun provideTokoChatBabbleCourier(): BabbleCourierClient {
        return BabbleCourierClientStub()
    }

    @Provides
    @TokoChatQualifier
    fun provideConversationsPreferences(
        @TokoChatQualifier context: Context
    ): ConversationsPreferencesStub {
        return ConversationsPreferencesStub(context)
    }

    @Provides
    @TokoChatQualifier
    fun provideDatabase(
        @TokoChatQualifier preferences: ConversationsPreferencesStub
    ): ConversationsDatabase {
        return Room.databaseBuilder(
            application,
            ConversationsDatabase::class.java,
            "conversations-database"
        )
            .fallbackToDestructiveMigration()
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
