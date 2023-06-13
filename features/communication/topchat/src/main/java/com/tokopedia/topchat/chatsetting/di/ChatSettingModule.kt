package com.tokopedia.topchat.chatsetting.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.network.TopchatCacheManagerImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ChatSettingModule(val context: Context) {

    @Provides
    @ChatSettingScope
    @TopchatContext
    fun provideContext(): Context = context

    @ChatSettingScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ChatSettingScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ChatSettingScope
    @Provides
    fun provideTopchatSharedPrefs(@TopchatContext context: Context): SharedPreferences {
        return context.getSharedPreferences("topchatSetting", Context.MODE_PRIVATE)
    }

    @ChatSettingScope
    @Provides
    fun provideTopchatCacheManager(@TopchatContext context: Context): TopchatCacheManager {
        val topchatCachePref = context.getSharedPreferences("topchatSetting", Context.MODE_PRIVATE)
        return TopchatCacheManagerImpl(topchatCachePref)
    }

    @ChatSettingScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}
